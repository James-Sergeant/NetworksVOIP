package securityLayer;

import com.Config;
import com.Layer;
import securityLayer.encryption.XOR;
import utils.Utils;

public class SecurityLayer extends Layer {
    public static XOR xor;

    public SecurityLayer(){
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
        if(Config.preset.isDECRYPTION()) {
            return xor.encryptDecryptAudio(payload);
        }
        return payload;
    }
}
