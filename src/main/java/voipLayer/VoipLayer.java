package voipLayer;

import audioLayer.AudioUtils;
import com.Layer;

public class VoipLayer extends Layer {

    private byte packetNumber = 0x0;

    public VoipLayer(){
        header = new byte[1];
    }

    @Override
    public byte[] addHeader(byte[] payload) {
        header[0] = packetNumber++;
        return super.addHeader(payload);
    }
}
