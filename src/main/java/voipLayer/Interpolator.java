package voipLayer;

import java.nio.ByteBuffer;

public class Interpolator {
    public static byte[] calculateInterpolatedBlock(byte[] block1, byte[] block2, int numberOfNulls, int nullIndex){
        int leftValue = blockToInt(block1[block1.length-2],block1[block1.length-1]);
        int rightValue = blockToInt(block2[block2.length-2],block2[block2.length-1]);
        System.out.println(leftValue+" "+rightValue);
        int difference = rightValue - leftValue;
        float perc = (float)nullIndex/(float)(numberOfNulls+1);
        int value = (int)(difference * perc);
        int interpolatedValue = value + leftValue;
        System.out.println(interpolatedValue);
        return ByteBuffer.allocate(4).putInt(interpolatedValue).array();
    }

    public static int blockToInt(byte byte1, byte byte2){
        return ((byte1 & 0xFF) << 8) | (byte2 & 0xFF);
    }

    public static void main(String[] args) {
        byte b1 = (byte)1;
        byte b2 = (byte)0;
        byte[] byte1 = ByteBuffer.allocate(4).putInt(blockToInt(b1, b2)).array();

        byte b3 = (byte)1;
        byte b4 = (byte)10;
        byte[] byte2 = ByteBuffer.allocate(4).putInt(blockToInt(b3, b4)).array();

        byte[] i1 = calculateInterpolatedBlock(byte1, byte2, 1, 1);
        int int1 = blockToInt(i1[2], i1[3]);
        System.out.println(int1);
    }
}
