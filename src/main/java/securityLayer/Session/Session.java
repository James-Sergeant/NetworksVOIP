package securityLayer.Session;

import audioLayer.AudioUtils;
import utils.Utils;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Random;

public class Session {
    private int sessionKey;
    Session(int sessionKey){
        this.sessionKey = sessionKey;
    }

    public static int generateSessionKey(){
        return BigInteger.probablePrime(Integer.SIZE-1, new Random()).intValue();
    }

    public byte[] encryptDecryptAudio(byte[] audio){
        ByteBuffer unwrapEncrypt = ByteBuffer.allocate(audio.length);
        ByteBuffer plainText = ByteBuffer.wrap(audio);
        for(int i = 0; i< audio.length/4; i++){
            int fourByte = plainText.getInt();
            fourByte = fourByte ^ sessionKey;
            unwrapEncrypt.putInt(fourByte);
        }
        return unwrapEncrypt.array();
    }

    public static void main(String[] args) {
        int sessionKey = generateSessionKey();
        Session session = new Session(sessionKey);
        System.out.println(sessionKey);

        byte[] testArray = new byte[32];
        Random random = new Random();
        for(int i = 0; i < 32; i++){
            testArray[i] = (byte)random.nextInt(255);
        }
        Utils.printByteArray(testArray);
        byte[] cypherText = session.encryptDecryptAudio(testArray);
        Utils.printByteArray(cypherText);
        Utils.printByteArray(session.encryptDecryptAudio(cypherText));

        System.out.println("Test Sound");
        while (true){
            AudioUtils.play(session.encryptDecryptAudio(AudioUtils.record()));
        }
    }
}
