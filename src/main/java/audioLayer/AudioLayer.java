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
            if(Config.PACKET_LOSS_SOLUTION == Config.PLOSS_SOLUTION.REPETITION){
                payload = prevAudio;
            }else if(Config.PACKET_LOSS_SOLUTION == Config.PLOSS_SOLUTION.BLANK_FILL_IN){
                payload = EMPTY_AUDIO_BLOCK; // FILL-IN Empty Audio
            }else{
                System.out.println("[ERROR] Audio Layer Payload=null");
            }
        }else{
            prevAudio = payload;
        }

        // Get audio data from payload and add to the buffer
        extractHeader(payload);

        // Send audio data to Player
        AudioUtils.play(header);

        return null;
    }
}
