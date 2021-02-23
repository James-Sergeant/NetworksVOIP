package audioLayer;

import com.Analyzer;
import com.Layer;

import java.io.IOException;
import java.util.Vector;

public class AudioLayer extends Layer {

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
        AudioUtils.PLAYER.storeAudioBlock(header);

        return super.removeHeader(payload);
    }
}
