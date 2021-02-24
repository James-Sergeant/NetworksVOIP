package voipLayer;

import com.Layer;

public class VoipLayer extends Layer {

    private byte packetNumber = 0;
    private static byte receivedPacketNumber = 0;
    private byte prevReceivedPacketNumber = 0;

    private static final long[] packetTimes = new long[256];

    public VoipLayer(){
        header = new byte[2];
    }

    @Override
    public byte[] addHeader(byte[] payload) {
        // Store current time of this packet
        int intPacketNumber = getPacketTimeIndex(packetNumber); // Get Unsigned int
        packetTimes[intPacketNumber] = System.nanoTime(); // Store current time
        System.out.println("SEND " + packetNumber);
        header[0] = packetNumber++; // Add packet number to header
        header[1] = receivedPacketNumber; // Add last received packet number to header
        return super.addHeader(payload);
    }

    @Override
    public byte[] removeHeader(byte[] payload) {
        // Get packet number and store in header variable
        extractHeader(payload);
        // Set other clients packet number
        prevReceivedPacketNumber = receivedPacketNumber;
        receivedPacketNumber = header[0];

        // DELAY
        calculateDelay();

        // PACKET LOSS
        calculatePacketLoss();

        return super.removeHeader(payload);
    }

    private void calculateDelay(){
        // My packet number returned
        int returnedPacketNumberIndex = getPacketTimeIndex(header[1]);
        // Calculate Delay between sending and receiving packet.
        long packetTime = packetTimes[returnedPacketNumberIndex];
        double delay = ((System.nanoTime() - packetTime))*Math.pow(10,-6); // Delay in ms

        //System.out.println("Delay: "+delay);
    }

    private void calculatePacketLoss(){
        System.out.println("RECEIVED " + receivedPacketNumber);
        int packetsLost = receivedPacketNumber - (prevReceivedPacketNumber+1);
        if(packetsLost > 0){
            System.out.println(receivedPacketNumber+ " "+ prevReceivedPacketNumber);
            System.out.println("Packets Lost : "+packetsLost+" packets");
        }
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
