package securityLayer;

import com.Config;
import com.Layer;
import securityLayer.encryption.XOR;
import utils.Utils;

public class SecurityLayer extends Layer {
    public static int sessionKey;
    private final XOR xor;

    public SecurityLayer(){
        xor = new XOR(sessionKey);
    }

    @Override
    public byte[] addHeader(byte[] payload) {
        if(Config.preset.isENCRYPTION()) {
            System.out.println("Before encrypt");
            Utils.printByteArray(payload);
            System.out.println();
            return xor.encryptDecryptAudio(payload);
        }
        return payload;
    }

    @Override
    public byte[] removeHeader(byte[] payload) {
        if(Config.preset.isENCRYPTION()) {
            System.out.println("After decrypt");
            Utils.printByteArray(payload);
            System.out.println();
            return xor.encryptDecryptAudio(payload);
        }
        return payload;
    }
}
