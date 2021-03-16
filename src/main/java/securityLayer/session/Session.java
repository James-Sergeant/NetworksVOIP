package securityLayer.session;

import securityLayer.encryption.RSA;

public class Session {
    //Public keys:
    public static final byte PUBLIC_KEY_REQUEST = 0;
    public static final byte PUBLIC_KEY_RESPONSE = 1;
    //Session keys:
    public static final byte SESSION_KEY = 2;

    public static boolean initiator = false;

    private RSA.KeyPair receiverPublicKey;
    private final RSA rsa;

    Session(){
        rsa = new RSA();
    }

}
