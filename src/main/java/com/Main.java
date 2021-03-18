package com;

import audioLayer.AudioUtils;
import utils.Analyzer;
import voipLayer.Receiver;
import voipLayer.Sender;

import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Main {
    /**
     * TODO: LOWEST DELAY FOR SOCKET 1
     * TODO: QUALITY SOCKET 2
     * TODO: QUALITY SOCKET 3
     * TODO: ENCRYPT AUDIO & PLAY
     * TODO: DECRYPT AUDIO & PLAY
     *
     * TODO: SHOW BEST SYSTEM FOR EACH DATAGRAMSOCKET
     * TODO: 10 MINUTE
     *
     * TODO: ASKED TO COUNT DIGITS "ONE TWO THREE FOUR"
     * TODO: PLAY THROUGH
     */

    // IP ADDRESSES
    public static final String SERGEANT_IP = "109.147.42.239";
    public static final String BURLING_IP = "86.154.116.23";
    private static final int CALL_LENGTH = 10; // Time in seconds

    public static void main(String[] args) throws LineUnavailableException, IOException, InterruptedException {

        Config.preset = Config.PRESET.SOCKET2;

        Analyzer.setup(false); // Setup static Analyser

        // Create Receiver & Sender
        Sender sender = new Sender();
        Receiver receiver = new Receiver();

        // Create Threads
        Thread receiverThread = new Thread(receiver);
        Thread senderThread = new Thread(sender);

        // Start Threads
        receiverThread.start();
        senderThread.start();

        // Wait until Threads finish (after CALL_LENGTH ms)
        Thread.sleep(CALL_LENGTH * 1000);

        sender.toggleSending();
        receiver.toggleReceiving();

        receiverThread.join();
        senderThread.join();

        Analyzer.close(); // Safely close static Analyzer

        System.out.println("Call Ended after "+ CALL_LENGTH+" seconds");
    }
}
