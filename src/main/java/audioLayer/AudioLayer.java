package audioLayer;

import com.Analyzer;
import com.Layer;

public class AudioLayer implements Layer {

    public static final int HEADER_SIZE = 512;

    @Override
    public byte[] addHeader(byte[] payload) {
        // Get Audio Data
        byte[] body = AudioUtils.record();

        // Create new byte array
        byte[] newPayload = new byte[payload.length + HEADER_SIZE];

        // Prepend Audio data to the payload
        System.arraycopy(body, 0, newPayload, 0, HEADER_SIZE);
        System.arraycopy(payload, 0, newPayload, HEADER_SIZE, payload.length);

        return newPayload;
    }

    @Override
    public byte[] removeHeader(byte[] payload) {
        // Create new byte array
        byte[] newPayload = new byte[payload.length - HEADER_SIZE];

        // Remove
        System.arraycopy(payload, HEADER_SIZE, newPayload, 0, payload.length - HEADER_SIZE);

        return newPayload;
    }

    @Override
    public byte[] getHeader(byte[] payload) {
        // Create new byte array
        byte[] header = new byte[HEADER_SIZE];

        // Remove
        System.arraycopy(payload, HEADER_SIZE, header, 0, HEADER_SIZE);

        return header;
    }
}
