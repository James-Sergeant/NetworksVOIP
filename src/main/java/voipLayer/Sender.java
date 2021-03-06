package voipLayer;

import audioLayer.AudioLayer;
import com.Main;
import securityLayer.Securitylayer;
import uk.ac.uea.cmp.voip.DatagramSocket2;
import uk.ac.uea.cmp.voip.DatagramSocket3;
import voipLayer.solutions.BlockInterleaver;

import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;
import java.net.*;

public class Sender implements Runnable{

    private boolean sending = true;
    private final int PORT;
    private final InetAddress IP;
    private DatagramSocket SENDER_SOCKET;

    // Layers
    private final AudioLayer audioLayer = new AudioLayer();
    private final VoipLayer voipLayer = new VoipLayer();
    private final Securitylayer securitylayer = new Securitylayer();

    // Interleaving
    private final BlockInterleaver interleaver = new BlockInterleaver(4);
    private boolean interleave = false;

    /**
     * Creates a default sender using localhost and the port 55555
     * @throws UnknownHostException
     * @throws SocketException
     * @throws LineUnavailableException
     */
    public Sender() throws UnknownHostException, SocketException, LineUnavailableException {
        PORT = 55555;
        IP = InetAddress.getByName("localhost");
        setSocket();
    }

    /**
     * Creates a sender with a chosen client IP, with the default port 55555
     * @param IP String: the users IP address.
     * @throws UnknownHostException
     * @throws SocketException
     * @throws LineUnavailableException
     */
    public Sender(String IP) throws UnknownHostException, SocketException, LineUnavailableException {
        PORT = 55555;
        this.IP = InetAddress.getByName(IP);
        setSocket();
    }

    /**
     * Creates a sender with the IP and the port configured.
     * @param IP String: The clients IP.
     * @param PORT Int: The port to be used.
     * @throws UnknownHostException
     * @throws SocketException
     * @throws LineUnavailableException
     */
    public Sender(String IP,int PORT) throws UnknownHostException, SocketException, LineUnavailableException {
        this.PORT = PORT;
        this.IP = InetAddress.getByName(IP);
        setSocket();
    }

    private void setSocket() throws SocketException {
        switch(Main.DATAGRAM_SOCKET){
            case 2:
                SENDER_SOCKET = new DatagramSocket2();
                break;
            case 3:
                SENDER_SOCKET = new DatagramSocket3();
                break;
            default:
                SENDER_SOCKET = new DatagramSocket();
        }
    }

    /**
     * Method for Runnable interface, is used when a User wishes to send voice to a client.
     */
    @Override
    public void run() {
        System.out.println("Running Sender...");
        while(sending){
            DatagramPacket packet = createPacket();
            try {
                if(interleave){
                    DatagramPacket packetToSend = interleaver.popPacket();
                    if(packetToSend != null) SENDER_SOCKET.send(packetToSend);
                    interleaver.addPacket(packet);
                }else {
                    SENDER_SOCKET.send(packet);
                }
            } catch (IOException e) {
                System.out.println("Failed to send packet...");
            }
        }
    }

    /**
     * Used to toggle if the object is currently sending.
     */
    public void toggleSending(){
        sending ^= true;
    }


    private DatagramPacket createPacket(){
        // Create Payload Buffer
        byte[] payload = new byte[0];

        payload = audioLayer.addHeader(payload);
        payload = voipLayer.addHeader(payload);
        payload = securitylayer.addHeader(payload);

        return new DatagramPacket(payload, payload.length,IP,PORT);
    }


    /**
     * Is thrown if audio is already transmitting.
     */
    class AlreadySendingException extends Exception{
        AlreadySendingException(){
            super("Audio already transmitting");
        }
    }
}
