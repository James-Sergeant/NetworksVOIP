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
        payload = new byte[512 * blocksPerPacket];
        for(int i = 0; i < blocksPerPacket; i++) {
            header = AudioUtils.record();
            System.arraycopy(header,0, payload, i*512, header.length);
        }
        return payload;
    }

    @Override
    public byte[] removeHeader(byte[] payload) {
        for(int i = 0; i < blocksPerPacket; i++) {
            byte[] audioBlock = new byte[512];

            // If packet's audio data was lost
            if (payload == null) {
                lossBurstLength++;
                if (Config.PACKET_LOSS_SOLUTION == Config.PLOSS_SOLUTION.REPETITION) {
                    audioBlock = prevAudio;//reduceAudioVolume(prevAudio, 1/(float)lossBurstLength);
                } else if (Config.PACKET_LOSS_SOLUTION == Config.PLOSS_SOLUTION.BLANK_FILL_IN) {
                    audioBlock = EMPTY_AUDIO_BLOCK; // FILL-IN Empty Audio
                } else {
                    System.out.println("[ERROR] Audio Layer Payload=null");
                }
            } else {
                System.arraycopy(payload,512 * i, audioBlock, 0, audioBlock.length);
                lossBurstLength = 0;
                prevAudio = audioBlock;
            }

            // Get audio data from payload and add to the buffer
            extractHeader(audioBlock);

            // Send audio data to Player
            AudioUtils.play(header);
        }
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
