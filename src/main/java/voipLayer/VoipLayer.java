package voipLayer;

import audioLayer.AudioLayer;
import audioLayer.AudioUtils;
import com.Layer;
import utils.AudioBuffer;
import utils.CircularBuffer;

public class VoipLayer extends Layer {

    // Packet Numbers variables
    private byte packetNumber = 0;
    private static byte receivedPacketNumber = 0;
    private byte prevReceivedPacketNumber = 0;

    private static final long[] packetTimes = new long[256];

    private final AudioLayer audioLayer = new AudioLayer();

    public VoipLayer(){
        header = new byte[2];
    }

    /**
     * Adds the current packetNumber and last recieved packet number to the packet's payload. It increments packetNumber.
     * @param payload byte[]: The packet's payload that needs a header prepended to
     * @return
     */
    @Override
    public byte[] addHeader(byte[] payload) {
        // Store current time of this packet
        int intPacketNumber = getPacketTimeIndex(packetNumber); // Get Unsigned int
        packetTimes[intPacketNumber] = System.nanoTime(); // Store current time
        //System.out.println("SEND " + packetNumber);
        header[0] = packetNumber++; // Add packet number to header
        header[1] = receivedPacketNumber; // Add last received packet number to header
        return super.addHeader(payload);
    }

    /**
     * Extracts the header data from the received packet's payload; sets values for prevReceivedPacketNumber &
     * receivedPacketNumber variables. I currently also calculates the delay and packet loss.
     * @param payload byte[]: The packet's payload that needs a header removed from
     * @return
     */
    @Override
    public byte[] removeHeader(byte[] payload) {
        // Get packet number and store in header variable
        extractHeader(payload);
        // Set other clients packet number
        prevReceivedPacketNumber = receivedPacketNumber;
        receivedPacketNumber = header[0];

        // ADD TO BUFFER
        //buffer.addBlock(payload);

        //if(buffer.size() >= CircularBuffer.BUFFER_LENGTH){
        //    audioLayer.removeHeader(super.removeHeader(buffer.popBlock()));
        //}

        // DELAY
        calculateDelay();

        // PACKET LOSS
        calculatePacketLoss();

        return super.removeHeader(payload);
    }

    /**
     * Uses header data from the latest received packet to calculate the time delay between sending and receiving packets
     */
    private void calculateDelay(){
        // My packet number returned
        int returnedPacketNumberIndex = getPacketTimeIndex(header[1]);
        // Calculate Delay between sending and receiving packet.
        long packetTime = packetTimes[returnedPacketNumberIndex];
        double delay = ((System.nanoTime() - packetTime))*Math.pow(10,-6); // Delay in ms

        //System.out.println("Delay: "+delay);
    }

    /**
     * Uses header data from the latest received packet to calculate whether packets that were expected, haven't arrived.
     */
    private void calculatePacketLoss(){
        //System.out.println("RECEIVED " + receivedPacketNumber);
        int packetsLost = receivedPacketNumber - (prevReceivedPacketNumber+1);
        if(packetsLost > 0){
            System.out.println(receivedPacketNumber+ " "+ prevReceivedPacketNumber);
            System.out.println("Packets Lost : "+packetsLost+" packets");
        }
    }

    /**
     * Converts a byte to an unsigned int
     * @param packetNumber byte: The signed byte to convert to an int
     * @return
     */
    public static int getPacketTimeIndex(byte packetNumber){
        return packetNumber & 0xFF;
    }
}
