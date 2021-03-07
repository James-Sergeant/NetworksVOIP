package voipLayer;

import java.nio.ByteBuffer;

public class Interpolator {

    public static short blockToShort(byte byte1, byte byte2){
        return (short) (((byte1 & 0xFF) << 8) | (byte2 & 0xFF));
    }

    public static short interpolateShort(short leftShort, short rightShort, double perc){
        short difference = (short) (rightShort - leftShort);
        short value = (short)((double)difference * perc);
        return (short) (value + leftShort);
    }

    public static byte[] getInterpolatedBlock(byte[] block1, byte[] block2, int numberOfNulls, int nullIndex){
        short leftShort = blockToShort(block1[510], block1[511]);
        short rightShort = blockToShort(block2[0], block2[1]);

        byte[] interpolatedBlock = new byte[512];
        for(int i = 0; i < 256; i++){
            double percentage = (((double)i) + ((nullIndex-1)*256))/(256.0*numberOfNulls);
            short sample = interpolateShort(leftShort, rightShort, percentage);
            interpolatedBlock[i*2] = (byte) (sample >> 8);
            interpolatedBlock[(i*2)+1] = (byte) sample;
        }

        return interpolatedBlock;
    }

    public static void main(String[] args) {
        byte b1 = (byte)1;
        byte b2 = (byte)0;
        byte[] s1 = ByteBuffer.allocate(2).putShort(blockToShort(b1, b2)).array();
        byte[] block1 = new byte[512];
        block1[510] = s1[0];
        block1[511] = s1[1];

        byte b3 = (byte)0;
        byte b4 = (byte)0;
        byte[] s2 = ByteBuffer.allocate(2).putShort(blockToShort(b3, b4)).array();
        byte[] block2 = new byte[512];
        block2[0] = s2[0];
        block2[1] = s2[1];

        byte[] i1 = getInterpolatedBlock(block1, block2, 4, 4);
        System.out.println("NEW BYTE: ");
        for(int i = 0; i < 256; i++){
            System.out.println(blockToShort(i1[i*2],i1[(i*2)+1]));
        }
    }
}
