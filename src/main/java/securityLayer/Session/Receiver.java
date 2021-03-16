package securityLayer.Session;

import uk.ac.uea.cmp.voip.DatagramSocket3;

import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class Receiver{
    private final DatagramSocket SOCKET;
    private final int PORT;
    private boolean receiving = false;
    private int TIMEOUT = 32;
    private int packetsReceived = 0;

    public static boolean receive = true;


    /**
     * The basic receiver
     * @throws LineUnavailableException
     * @throws SocketException
     */
    public Receiver() throws LineUnavailableException, SocketException {
        this.PORT = 55555;
        this.SOCKET = new DatagramSocket3(this.PORT);
        this.SOCKET.setSoTimeout(TIMEOUT);
    }

    public Receiver(int PORT) throws LineUnavailableException, SocketException {
        this.PORT = PORT;
        this.SOCKET = new DatagramSocket3(this.PORT);
        this.SOCKET.setSoTimeout(TIMEOUT);
    }

    public byte[] getPacket(){
        byte[] buffer = new byte[255];
            DatagramPacket packet = new DatagramPacket(buffer,0,buffer.length);

            try {
                SOCKET.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
        return buffer;
    }
}
