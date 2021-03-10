package voipLayer;

import java.nio.ByteBuffer;

public class Interpolator {

    public static short blockToShort(byte byte1, byte byte2){
        return (short) ((short) ((byte1) << 8) | (byte2));
    }

    public static short linearInterpolateShort(short leftShort, short rightShort, double percentage){
        short difference = (short) (rightShort - leftShort);
        short value = (short)((double)difference * percentage);
        return (short) (value + leftShort);
    }

    public static short cosineInterpolateShort(short leftShort, short rightShort, double percentage){
        double p2 = (1 - Math.cos(percentage * Math.PI)) / 2;
        return (short) (leftShort * (1 - p2) + rightShort * p2);
    }

    public static short getAverageSample(byte[] block){
        short total = 0;
        for(int i = 0; i < 256; i++){
            total += blockToShort(block[i], block[i+1]);
        }

        return (short) (total / 256);
    }

    public static byte[] getInterpolatedBlock(byte[] block1, byte[] block2, int numberOfNulls, int nullIndex){
        short leftShort = blockToShort(block1[510], block1[511]);
        System.out.println("L = "+leftShort);
        short rightShort = blockToShort(block2[0], block2[1]);
        System.out.println("R = "+rightShort);
        //leftShort = (short) (getAverageSample(block1));
        //rightShort = (short) (getAverageSample(block2));
        //System.out.println("LEFT AND RIGHT: "+leftShort +" "+rightShort);
        //System.out.println("NumNulls = "+numberOfNulls +", NullIndex = "+nullIndex);
        byte[] interpolatedBlock = new byte[512];
        System.out.print("{");
        for(int i = 0; i < 256; i++){
            double percentage = (((double)i) + ((nullIndex-1)*256))/(256.0*numberOfNulls);
            short sample = cosineInterpolateShort(leftShort, rightShort, percentage);

            System.out.print(sample+", ");

            interpolatedBlock[i*2] = (byte) (sample >> 8);
            interpolatedBlock[(i*2)+1] = (byte) (sample);
        }
        System.out.println("}");
        //System.out.println(blockToShort(interpolatedBlock[256],interpolatedBlock[257]));
        // PRINT SAMPLES OF AUDIO DATA
        System.out.print("{");
        for(int i = 0; i < 256; i++){
            System.out.print(Interpolator.blockToShort(interpolatedBlock[i],interpolatedBlock[i+1])+", ");
        }
        System.out.println("}");
        return interpolatedBlock;
    }

    public static void main(String[] args) {

        byte b1 = (byte)0;
        byte b2 = (byte)-1;
        byte[] s1 = ByteBuffer.allocate(2).putShort(blockToShort(b1, b2)).array();
        byte[] block1 = new byte[512];
        block1[510] = s1[0];
        block1[511] = s1[1];

        byte b3 = (byte)1;
        byte b4 = (byte)0;
        byte[] s2 = ByteBuffer.allocate(2).putShort(blockToShort(b3, b4)).array();
        byte[] block2 = new byte[512];
        block2[0] = s2[0];
        block2[1] = s2[1];

        byte[] i1 = getInterpolatedBlock(block1, block2, 1, 1);
        System.out.println("NEW BYTE: ");
        for(int i = 0; i < 256; i++){
            System.out.println(blockToShort(i1[i*2],i1[(i*2)+1]));
        }
        System.out.println("INTERPOLATE TEST");
        System.out.println(linearInterpolateShort((short)0,(short)100,0.1));
        System.out.println(cosineInterpolateShort((short)0,(short)100,0.1));
    }

}
