package securityLayer.session;

import com.Config;
import com.Main;
import securityLayer.SecurityLayer;
import securityLayer.encryption.RSA;
import securityLayer.encryption.XOR;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class Session {
    //Colour codes:
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    //Public keys:
    public static final byte PUBLIC_KEY_REQUEST = 0;
    public static final byte PUBLIC_KEY_RESPONSE = 1;
    //Session keys:
    public static final byte SESSION_KEY = 2;
    //Packet Size:
    public static final int PACKET_SIZE = 64;

    public boolean sessionFinished = false;


    private RSA.KeyPair receiverPublicKey;
    private RSA.KeyPair localPublicKey;
    private final RSA rsa;

    public int sessionKey;

    private final SessionReceiver sessionReceiver;

    private final Thread receiverThread;

    private String IP;

    public Session() throws SocketException, InterruptedException {
        System.out.println(ANSI_BLUE+"Receiver session started..."+ANSI_RESET);
        rsa = new RSA();
        localPublicKey = rsa.publicKey;
        sessionReceiver = new SessionReceiver();
        //Starts a new receiver thread.
        receiverThread = new Thread(sessionReceiver);
        receiverThread.start();
        sessionReceiver();
        SecurityLayer.xor = new XOR(sessionKey);
        System.out.println("Using session key: "+sessionKey);
    }
    public Session(String IP) throws IOException, InterruptedException {
        System.out.println(ANSI_BLUE+"Sender session started..."+ANSI_RESET);
        rsa = new RSA();
        localPublicKey = rsa.publicKey;
        sessionReceiver = new SessionReceiver();
        //Starts a new receiver thread.
        receiverThread = new Thread(sessionReceiver);
        receiverThread.start();
        this.IP = IP;
        sessionInitiator();
        SecurityLayer.xor = new XOR(sessionKey);
        System.out.println("Using session key: "+sessionKey);
    }
    //Sender Side:
    private void sessionInitiator() throws IOException {
        //Send private key request:
        sendPublicKeyRequest();
        //Receive the key back:
        boolean keyReceived = false;
        while (!keyReceived){
            if(sessionReceiver.isNewPacket()){
                setReceiverPublicKey(sessionReceiver.getNewPacket());
                keyReceived = true;
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //Send session key:
        sendSessionKey();
        sessionReceiver.receiving =false;
        try {
            receiverThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void sendPublicKeyRequest() throws IOException {
        System.out.println(ANSI_YELLOW+"[>] "+ANSI_RESET+"Sending pubic key request...");
        // Create the packet with the request header:
        byte[] payload = new byte[PACKET_SIZE];
        payload[0] = PUBLIC_KEY_REQUEST;
        // Send of the the request.
        new SessionSender(IP,payload);
    }

    private void setReceiverPublicKey(DatagramPacket packet){
        System.out.println(ANSI_GREEN+"[<] "+ANSI_RESET+"Received public key!");
        byte[] data = packet.getData();
        byte[] n = new byte[Integer.BYTES];
        byte[] e =new byte[Integer.BYTES];
        //Sets byte array:
        System.arraycopy(data,1,n,0,n.length);
        System.arraycopy(data,5,e,0,e.length);
        //Makes keyPair:
        int intN = ByteBuffer.wrap(n).getInt();
        int intE = ByteBuffer.wrap(e).getInt();
        receiverPublicKey = new RSA.KeyPair(intE,intN);
    }

    private void sendSessionKey() throws IOException {
        System.out.println(ANSI_YELLOW+"[>] "+ANSI_RESET+"Sending session key...");
        int key = XOR.generateSessionKey();
        sessionKey = key;
        if(Config.preset.equals(Config.PRESET.XOR1)){
            this.sessionKey = 2063682301;
        }
        String encryptedKey = RSA.encrypt(key,receiverPublicKey);
        byte[] keyBytes = encryptedKey.getBytes(StandardCharsets.US_ASCII);
        byte[] payload = new byte[keyBytes.length+1];
        payload[0] = SESSION_KEY;
        System.arraycopy(keyBytes,0,payload,1,keyBytes.length);
        new SessionSender(IP,payload);
    }

    //Receiver side:

    private void sessionReceiver() throws InterruptedException {
        while (!sessionFinished){
            if(sessionReceiver.isNewPacket()){
                //Handel incoming packet:
                DatagramPacket packet = sessionReceiver.getNewPacket();
                byte[] payload = packet.getData();
                IP = packet.getAddress().toString();
                //Decide what type of request it is:
                byte head = payload[0];
                if(head == PUBLIC_KEY_REQUEST){
                    handlePublicKeyRequest();
                }else if(head == SESSION_KEY){
                    handleSessionKey(payload);
                }else {
                    System.out.println("Error with session creation!");
                }
            }else {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        sessionReceiver.receiving =false;
        receiverThread.join();
    }

    private void handlePublicKeyRequest(){
        System.out.println(ANSI_GREEN+"[<] "+ANSI_RESET+"Public key request received!");
        byte[] n = ByteBuffer.allocate(4).putInt(localPublicKey.getN().intValue()).array();
        byte[] e = ByteBuffer.allocate(4).putInt(localPublicKey.getExponent().intValue()).array();
        byte[] payload = new byte[PACKET_SIZE];
        //Set the head to identify a response.
        payload[0] = PUBLIC_KEY_RESPONSE;
        //Adds the n and e to it:
        System.arraycopy(n,0,payload,1,n.length);
        System.arraycopy(e,0,payload,5,e.length);
        //Sends off the public key:
        try {
           new SessionSender(IP,payload);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        System.out.println(ANSI_YELLOW+"[>] "+ANSI_RESET+"Public key sent...");
    }
    private void handleSessionKey(byte[] payload){
        System.out.println(ANSI_GREEN+"[<] "+ANSI_RESET+"Session key received!");
        byte[] data = new byte[payload.length-1];
        System.arraycopy(payload,1,data,0,data.length);
        String encryptedSessionKey = new String(data);
        int sessionKey = rsa.decrypt(encryptedSessionKey).intValue();
        this.sessionKey = sessionKey;
        if(Config.preset.equals(Config.PRESET.XOR1)){
            this.sessionKey = 2063682301;
        }
        Main.IP = this.IP.replace("/","");
        sessionFinished =true;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        new Session();
    }

}
