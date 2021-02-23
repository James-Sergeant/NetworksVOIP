package audioLayer;

import com.Analyzer;
import com.Layer;

import java.io.IOException;
import java.util.Vector;

public class AudioLayer extends Layer {

    @Override
    public byte[] addHeader(byte[] payload) {
        header = AudioUtils.record();
        return super.addHeader(payload);
    }

    @Override
    public byte[] removeHeader(byte[] payload) {
        byte[] audioData = extractHeader(payload);

        // BUFFER AND PLAY AUDIO

        return super.removeHeader(payload);
    }

    private Vector<byte[]> buffer(){
        Vector<byte[]> buffer = new Vector<>();
        for(int i = 0; i < 8; i++){
            //buffer.add(getPacket());
        }
        return buffer;
    }

    private void playBuffer(Vector<byte[]> buffer){
        for (byte[] frame:buffer) {
            try {
                AudioUtils.playBlock(frame);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
