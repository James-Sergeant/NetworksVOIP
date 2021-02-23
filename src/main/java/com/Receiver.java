package com;

import CMPC3M06.AudioPlayer;

import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Vector;

public class Receiver implements Runnable{
    public static byte DATAGRAM_SOCKET_ONE = 0;
    public static byte DATAGRAM_SOCKET_TWO = 1;
    public static byte DATAGRAM_SOCKET_THREE = 2;

    private final AudioPlayer PLAYER;
    private final DatagramSocket SOCKET;
    private final int PORT;
    private boolean receiving = false;
    private int packetsReceived = 0;

    /**
     * The basic receiver
     * @throws LineUnavailableException
     * @throws SocketException
     */
    public Receiver() throws LineUnavailableException, SocketException {
        this.PLAYER = new AudioPlayer();
        this.PORT = 55555;
        this.SOCKET = new DatagramSocket(this.PORT);
    }
    public Receiver(int PORT) throws LineUnavailableException, SocketException {
        this.PLAYER = new AudioPlayer();
        this.PORT = PORT;
        this.SOCKET = new DatagramSocket(this.PORT);
    }

    public void toggleReceiving(){
        receiving ^= true;
    }


    @Override
    public void run() {
        toggleReceiving();
        while (receiving) {
            play();
        }
    }

    private void play(){
        playBuffer(buffer());
    }

    private byte[] getPacket(){
        byte[] buffer = new byte[512 + Analyzer.HEADER_LENGTH];
        byte[] audioData = new byte[512];
        DatagramPacket packet = new DatagramPacket(buffer,0,buffer.length);

        try {
            // Receives packet
            SOCKET.receive(packet);
            // Extracts audio data from packet
            System.arraycopy(buffer,Analyzer.HEADER_LENGTH, audioData, 0, 512);
            // Log Packet
            Analyzer.logPacket(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return audioData;
    }

    private Vector<byte[]> buffer(){
        Vector<byte[]> buffer = new Vector<>();
        for(int i = 0; i < 8; i++){
            buffer.add(getPacket());
        }
        return buffer;
    }

    private void playBuffer(Vector<byte[]> buffer){
        for (byte[] frame:buffer) {
            try {
                PLAYER.playBlock(frame);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
