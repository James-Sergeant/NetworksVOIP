package securityLayer.encryption;

import audioLayer.AudioUtils;
import utils.Utils;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Random;

public class XOR {
    private int sessionKey;
    XOR(int sessionKey){
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
        XOR xor = new XOR(sessionKey);
        System.out.println(sessionKey);

        byte[] testArray = new byte[32];
        Random random = new Random();
        for(int i = 0; i < 32; i++){
            testArray[i] = (byte)random.nextInt(255);
        }
        Utils.printByteArray(testArray);
        byte[] cypherText = xor.encryptDecryptAudio(testArray);
        Utils.printByteArray(cypherText);
        Utils.printByteArray(xor.encryptDecryptAudio(cypherText));

        System.out.println("Test Sound");
        while (true){
            byte[] audio = xor.encryptDecryptAudio(AudioUtils.record());
            AudioUtils.play(xor.encryptDecryptAudio(audio));
        }
    }
}
