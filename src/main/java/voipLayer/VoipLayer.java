package voipLayer;

import audioLayer.AudioUtils;
import com.Layer;

public class VoipLayer implements Layer {

    public static final int HEADER_SIZE = 1;
    private static byte packetNumber = 0x0;

    @Override
    public byte[] addHeader(byte[] payload) {

        // Create new byte array
        byte[] newPayload = new byte[payload.length + HEADER_SIZE];

        // Prepend packet number
        newPayload[0] = packetNumber++;
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
