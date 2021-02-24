package voipLayer;

import com.Layer;

public class VoipLayer extends Layer {

    private byte packetNumber = 0;
    private byte receivedPacketNumber = 0x0;

    private static final long[] packetTimes = new long[255];

    public VoipLayer(){
        header = new byte[2];
    }

    @Override
    public byte[] addHeader(byte[] payload) {
        // Store current time of this packet
        int intPacketNumber = getPacketTimeIndex(packetNumber); // Get Unsigned int
        packetTimes[intPacketNumber] = System.nanoTime(); // Store current time

        header[0] = packetNumber++; // Add packet number to header
        header[1] = receivedPacketNumber; // Add last received packet number to header
        return super.addHeader(payload);
    }

    @Override
    public byte[] removeHeader(byte[] payload) {
        // Get packet number and store in header variable
        extractHeader(payload);
        // Other clients packet number
        receivedPacketNumber = header[0];
        // My packet number returned
        int returnedPacketNumberIndex = getPacketTimeIndex(header[1]);
        // Calculate Delay between sending and receiving packet.
        long packetTime = packetTimes[returnedPacketNumberIndex];
        long delay = (long) ((System.nanoTime() - packetTime)*Math.pow(10,-6));

        System.out.println("Delay: "+delay*Math.pow(10,-6));

        return super.removeHeader(payload);
    }

    /**
     * Converts a byte to an unsigned int
     * @param packetNumber
     * @return
     */
    public static int getPacketTimeIndex(byte packetNumber){
        return packetNumber & 0xFF;
    }
}
