package securityLayer.session;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class SessionReceiver implements Runnable {

    public static final int PORT = 55555;
    public static final int PACKET_SIZE = 9;

    public boolean receiving;

    private final DatagramSocket RECEIVER_SOCKET;
    private boolean newPacket;
    private final byte[] BUFFER;
    private final DatagramPacket PACKET;

    public SessionReceiver() throws SocketException {
        receiving = false;
        RECEIVER_SOCKET = new DatagramSocket(PORT);
        newPacket = false;
        BUFFER = new byte[PACKET_SIZE];
        PACKET = new DatagramPacket(BUFFER,0,BUFFER.length);
    }

    //Looks for new requests, when one comes in the packet is saved and the newPacket flag is set for the session class.
    @Override
    public void run() {
        receiving = true;
        System.out.println("Listening for Session Packet");
        while (receiving){
            try {
                RECEIVER_SOCKET.receive(PACKET);
                newPacket = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public DatagramPacket getNewPacket(){
        newPacket =false;
        return PACKET;
    }

    public boolean isNewPacket(){
        return newPacket;
    }

}
