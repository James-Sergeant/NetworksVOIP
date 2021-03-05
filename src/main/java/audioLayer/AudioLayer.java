package audioLayer;

import com.Layer;

public class AudioLayer extends Layer {
    public static final double BLOCK_LENGTH = 0.032;
    public static final int BLOCK_SIZE = 512;

    public AudioLayer() {
        header = new byte[512];
    }

    @Override
    public byte[] addHeader(byte[] payload) {
        header = AudioUtils.record();
        return addHeader(header, payload);
    }

    @Override
    public byte[] removeHeader(byte[] payload) {
        // Get audio data from payload and add to the buffer
        extractHeader(payload);

        // Send audio data to Player
        AudioUtils.play(header);

        return super.removeHeader(payload);
    }
}
