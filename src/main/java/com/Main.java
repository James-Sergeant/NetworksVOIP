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

    // IP ADDRESSES
    private static final String SERGEANT_IP = "109.147.42.239";
    private static final String BURLING_IP = "86.154.116.23";

    private static final int CALL_LENGTH = 50; // Seconds

    public static void main(String[] args) throws LineUnavailableException, SocketException, UnknownHostException, IOException, InterruptedException {

        Analyzer.setup(false); // Setup static Analyser

        // Create Receiver & Sender
        Sender sender = new Sender(BURLING_IP);
        Receiver receiver = new Receiver();

        // Create Threads
        Thread receiverThread = new Thread(receiver);
        Thread senderThread = new Thread(sender);
        Thread audioThread = new Thread(AudioUtils.PLAYER);

        // Start Threads
        receiverThread.start();
        senderThread.start();
        audioThread.start();

        // Wait until Threads finish (after CALL_LENGTH ms)
        Thread.sleep(CALL_LENGTH * 1000);

        sender.toggleSending();
        receiver.toggleReceiving();
        AudioUtils.PLAYER.togglePlaying();

        receiverThread.join();
        senderThread.join();
        audioThread.join();

        Analyzer.close(); // Safely close static Analyzer
    }
}
