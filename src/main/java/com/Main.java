package com;

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

    private static final int CALL_LENGTH = 5; // Seconds

    public static void main(String[] args) throws LineUnavailableException, SocketException, UnknownHostException, IOException, InterruptedException {

        Analyzer.setup(false); // Setup static Analyser

        // Create Receiver & Sender
        Sender sender = new Sender();
        Receiver receiver = new Receiver();

        // Create Threads
        Thread thread = new Thread(receiver);
        Thread thread1 = new Thread(sender);

        // Start Threads
        thread.start();
        thread1.start();

        // Wait until Threads finish (after CALL_LENGTH ms)
        Thread.sleep(CALL_LENGTH * 1000);

        sender.toggleSending();
        receiver.toggleReceiving();

        thread.join();
        thread1.join();

        Analyzer.close(); // Safely close static Analyzer
    }
}
