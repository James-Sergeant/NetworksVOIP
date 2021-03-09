package audioLayer;

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

    // Solutions
    private static final boolean REPETITION = true;

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
        if(payload == null && REPETITION){ // REPETITION
            payload = prevAudio;
        }else{
            prevAudio = EMPTY_AUDIO_BLOCK; // FILL-IN Empty Audio
        }

        // Get audio data from payload and add to the buffer
        extractHeader(payload);

        // PRINT SAMPLES OF AUDIO DATA

        for(int i = 0; i < 256; i++){
            System.out.println(Interpolator.blockToShort(header[i],header[i+1]));
        }


        // Send audio data to Player
        AudioUtils.play(header);

        return null;
    }
}
