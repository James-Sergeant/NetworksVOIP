package voipLayer;

import audioLayer.AudioLayer;
import com.Layer;
import utils.AudioBuffer;

public class VoipLayer extends Layer {

    // Packet Numbers variables
    private byte packetNumber = 0;
    private static byte receivedPacketNumber = 0;
    private byte prevReceivedPacketNumber = 0;
    private static final long[] packetTimes = new long[256];

    // Solutions
    private final AudioBuffer BUFFER = new AudioBuffer(1.0, 255);

    private final Interpolator INTERPOLATOR = new Interpolator();
    private byte[] lastPoppedBlock = null;

    public VoipLayer(){
        header = new byte[2];
    }

    /**
     * Adds the current packetNumber and last received packet number to the packet's payload. It increments packetNumber.
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
        BUFFER.insertBlock(getPacketTimeIndex(receivedPacketNumber), super.removeHeader(payload));

        // DELAY
        calculateDelay();

        // PACKET LOSS
        calculatePacketLoss();

        return super.removeHeader(payload);
    }

    public byte[] getAudioBlock(){
        byte[] audioBlock = BUFFER.popBlock();

        if(audioBlock == null){
            // Get number of null until next audio block. Get next audio block
            byte[] nextBlock = null;
            int numberOfNulls = 1;
            while(nextBlock == null && numberOfNulls < BUFFER.getLength()){
                nextBlock = BUFFER.getBlock(numberOfNulls++);
            }
            if(nextBlock != null) {
                // Interpolate
                audioBlock = new byte[512];
                for (int i = 0; i < AudioLayer.BLOCK_SIZE/2; i++) {
                    byte[] sample = Interpolator.calculateInterpolatedBlock(lastPoppedBlock, nextBlock, 256, i);
                    audioBlock[i*2] = sample[2];
                    audioBlock[(i*2)+1] = sample[3];
                    System.out.println(Interpolator.blockToInt(sample[0], sample[1]));

                }
            }
        }else{
            lastPoppedBlock = audioBlock;
        }

        return audioBlock;
    }

    public boolean allowPlaying(){
        return !BUFFER.isRefilling();
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
