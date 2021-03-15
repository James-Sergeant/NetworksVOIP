package securityLayer.Session;

import audioLayer.AudioUtils;
import utils.Utils;

import javax.sound.sampled.LineUnavailableException;
import java.math.BigInteger;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class Session {
    public static final byte PUBLIC_KEY_REQUEST = 0;
    public static final byte PUBLIC_KEY_RESPONSE = 1;
    public static final byte SESSION_KEY = 2;
    public static final int REQUEST_PACKET_SIZE = 9;

    private static Sender sender;
    private static Receiver receiver;

    static {
        try {
            receiver = new Receiver();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    private static int sessionKey;
    private static RSA rsa = new RSA();
    private static RSA.KeyPair receiverPublicKey;

    public static void makeSession(String ip) throws SocketException, UnknownHostException, LineUnavailableException {
        sender = new Sender(ip);
        sessionKey = XOR.generateSessionKey();

        receiverPublicKey = requestPublicKey();
        double encryptedSessionKey = RSA.encrypt(sessionKey,receiverPublicKey);
        sendSessionKey(encryptedSessionKey);

    }

    public static void sendSessionKey(double sessionKey){
        byte[] key = ByteBuffer.allocate(8).putDouble(sessionKey).array();
        sender.send(makePacketData(SESSION_KEY,key));
    }

    private static RSA.KeyPair requestPublicKey(){
        //Send request for public key
        String hi = "HELLO";
        byte[] hello = hi.getBytes(StandardCharsets.US_ASCII);
        sender.send(makePacketData(PUBLIC_KEY_REQUEST,hello));

        //Wait for the key to be received.
        byte[] response = receiver.getPacket();

        return extractKeyPair(response);
    }

    private static RSA.KeyPair extractKeyPair(byte[] data){
        byte[] e = new byte[4];
        byte[] n = new byte[4];

        System.arraycopy(data,1,e,0,e.length);
        System.arraycopy(data,5,n,0,n.length);

        int intE = ByteBuffer.wrap(e).getInt();
        int intN = ByteBuffer.wrap(n).getInt();

        return new RSA.KeyPair(intN, intE);
    }


    private static byte[] makePacketData(byte header, byte[] data){
        byte[] arr = new byte[data.length + 1];
        arr[0] = header;
        System.arraycopy(data,0,arr,1,data.length);
        return arr;
    }
}
