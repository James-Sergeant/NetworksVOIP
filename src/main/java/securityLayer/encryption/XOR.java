package securityLayer.encryption;

import audioLayer.AudioUtils;
import utils.Utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Random;

public class XOR {
    private final int sessionKey;
    public XOR(int sessionKey){
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

    public static void main(String[] args) throws IOException {
        int sessionKey= 15; //2063682301;//generateSessionKey();
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

        File encrypted = new File("encrypted.csv");
        File decrypted = new File("decrypted.csv");

        BufferedWriter writer1 = new BufferedWriter(new FileWriter(encrypted));
        BufferedWriter writer2 = new BufferedWriter(new FileWriter(decrypted));

        String encryptedString = "";
        String decryptedString = "";

        for(int i = 0; i < 10/0.032; i++){
            byte[] audio = xor.encryptDecryptAudio(AudioUtils.record());

            for(int j = 0; j < 512; j+=2){
                encryptedString += (Utils.blockToShort(audio[j], audio[j+1]))+"\n";
            }

            audio = xor.encryptDecryptAudio(audio);

            for(int j = 0; j < 512; j+=2){
                decryptedString += (Utils.blockToShort(audio[j], audio[j+1]))+"\n";
            }

            //AudioUtils.play(audio);
        }

        writer1.write(encryptedString);
        writer2.write(decryptedString);

        writer1.close();
        writer2.close();
    }
}
