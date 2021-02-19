package com;

import CMPC3M06.AudioPlayer;

import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class Receiver implements Runnable{
    public static byte DATAGRAM_SOCKET_ONE = 0;
    public static byte DATAGRAM_SOCKET_TWO = 1;
    public static byte DATAGRAM_SOCKET_THREE = 2;

    public final AudioPlayer PLAYER;
    public final DatagramSocket SOCKET;
    public final int PORT;
    public boolean receiving = false;

    /**
     * The basic receiver
     * @throws LineUnavailableException
     * @throws SocketException
     */
    public Receiver() throws LineUnavailableException, SocketException {
        this.PORT = 55555;
        this.PLAYER = new AudioPlayer();
        this.SOCKET = new DatagramSocket(this.PORT);
    }
    public Receiver(int PORT) throws LineUnavailableException, SocketException {
        this.PORT = PORT;
        this.PLAYER = new AudioPlayer();
        this.SOCKET = new DatagramSocket(this.PORT);
    }

    public void toggleReceiving(){
        receiving ^= true;
    }


    @Override
    public void run() {
        if(receiving){
            try {
                throw new AlreadyReceivingException();
            } catch (AlreadyReceivingException e) {
                System.out.println(e);
            }
        }
        
        toggleReceiving();
        while (receiving){
            System.out.println("Running Receiver...");
            playAudio(getPacket());
        }
    }

    private byte[] getPacket(){
        byte[] buffer = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buffer,0,buffer.length);

        try {
            SOCKET.receive(packet);
            System.out.println("Packed Received");
        } catch (IOException e) {
            System.out.println("Error getting received packet");
        }
        return buffer;
    }

    private void playAudio(byte[] block){
        try {
            PLAYER.playBlock(block);
        } catch (IOException e) {
            System.out.println("Error playing audio");
        }
    }
    /**
     * Is thrown if audio is already receiving.
     */
    class AlreadyReceivingException extends Exception{
        AlreadyReceivingException(){
            super("Audio already sending");
        }
    }
}
