package audioLayer;

import com.Config;
import com.Layer;
import voipLayer.Interpolator;

import java.util.Arrays;
import java.util.Random;

public class AudioLayer extends Layer {

    public static final double BLOCK_LENGTH = 0.032;
    public static final int BLOCK_SIZE = 512;

    private byte[] prevAudio;
    private byte[] noiseBlock = AudioUtils.generateNoiseBlock();
    private final byte[] EMPTY_AUDIO_BLOCK = new byte[512];
    private int lossBurstLength = 0;
    private final int blocksPerPacket = Config.BLOCKS_PER_PACKET;


    public AudioLayer() {
        header = new byte[512];
        prevAudio = new byte[BLOCK_SIZE];
    }

    @Override
    public byte[] addHeader(byte[] payload) {
        header = AudioUtils.record();
        return addHeader(header, payload);
    }

    @Override
    public byte[] removeHeader(byte[] payload) {
        // If packet's audio data was lost
        if(payload == null){
            lossBurstLength++;
            if(Config.PACKET_LOSS_SOLUTION == Config.PLOSS_SOLUTION.REPETITION){
                payload = prevAudio;//reduceAudioVolume(prevAudio, 1/(float)lossBurstLength);
            }else if(Config.PACKET_LOSS_SOLUTION == Config.PLOSS_SOLUTION.BLANK_FILL_IN){
                payload = EMPTY_AUDIO_BLOCK; // FILL-IN Empty Audio
            }else{
                System.out.println("[ERROR] Audio Layer Payload=null");
            }
        }else{
            lossBurstLength = 0;
            prevAudio = payload;
        }

        // Get audio data from payload and add to the buffer
        extractHeader(payload);

        // Send audio data to Player
        AudioUtils.play(header);

        return null;
    }

    /**
     * DOESN'T WORK
     * @param audio
     * @param factor
     * @return
     */
    private byte[] reduceAudioVolume(byte[] audio, float factor){
        System.out.println(factor);
        byte[] newAudio = new byte[audio.length];
        for(int i = 0; i < audio.length; i+=2){
            System.out.println(audio[i] + ", " + audio[i+1] + ": " + Interpolator.blockToShort(audio[i], audio[i+1]));
            short sample = (short) ((float)Interpolator.blockToShort(audio[i], audio[i+1]) * factor);
            newAudio[i] = (byte) ((sample >> 8) & 0xff);
            newAudio[i+1] = (byte) (sample & 0xff);
            System.out.println(newAudio[i] + ", " + newAudio[i+1] + ": "+Interpolator.blockToShort(newAudio[i], newAudio[i+1]));
            System.out.println();
        }
        return newAudio;
    }
}
