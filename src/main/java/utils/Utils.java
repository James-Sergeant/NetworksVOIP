package utils;

import audioLayer.AudioUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class Utils {
    /**
     * Method from https://stackoverflow.com/questions/4485128/how-do-i-convert-long-to-byte-and-back-in-java/29132118#29132118
     * @param l
     * @return
     */
    public static byte[] longToBytes(long l) {
        byte[] result = new byte[Long.BYTES];
        for (int i = Long.BYTES - 1; i >= 0; i--) {
            result[i] = (byte)(l & 0xFF);
            l >>= Byte.SIZE;
        }
        return result;
    }

    /**
     * https://stackoverflow.com/questions/4485128/how-do-i-convert-long-to-byte-and-back-in-java/29132118#29132118
     * @param b
     * @return
     */
    public static long bytesToLong(final byte[] b) {
        long result = 0;
        for (int i = 0; i < Long.BYTES; i++) {
            result <<= Byte.SIZE;
            result |= (b[i] & 0xFF);
        }
        return result;
    }

    public static void printByteArray(byte[] x){
        for(int i = 0; i<x.length; i++){
            System.out.print(x[i]+", ");
        }
        System.out.println();
    }


    public static void main(String[] args) throws InterruptedException {
        byte b = 0;
        while(true) {
            System.out.println(b++);
        }
    }

    public static short blockToShort(byte byte1, byte byte2){
        return (short) ( ((byte2 & 0xFF) << 8) | (byte1 & 0xFF));
    }


    public static void writeToFile(String fileName, String stringToWrite){
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File(fileName + ".txt")));
            writer.write(stringToWrite);
            writer.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
