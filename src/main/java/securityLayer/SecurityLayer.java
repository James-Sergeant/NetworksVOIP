package securityLayer;

import com.Config;
import com.Layer;
import securityLayer.encryption.XOR;

public class SecurityLayer extends Layer {
    public static int sessionKey;
    private final XOR xor;

    public SecurityLayer(){
        xor = new XOR(sessionKey);
    }

    @Override
    public byte[] addHeader(byte[] payload) {
        if(Config.preset.isENCRYPTION()) {
            return xor.encryptDecryptAudio(payload);
        }
        return payload;
    }

    @Override
    public byte[] removeHeader(byte[] payload) {
        return addHeader(payload);
    }
}
