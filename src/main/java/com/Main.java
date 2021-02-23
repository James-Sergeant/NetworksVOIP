package com;

import networking.Receiver;
import networking.Sender;

import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Main {

    // IP ADDRESSES
    private static final String LOCALHOST = "localhost";
    private static final String SERGEANT_IP = "109.147.42.239";
    private static final String BURLING_IP = "86.154.116.23";

    private static final int CALL_LENGTH = 50; // Seconds

    public static void main(String[] args) throws LineUnavailableException, SocketException, UnknownHostException, IOException, InterruptedException {

        Analyzer.setup(false); // Setup static Analyser

        // Create Threads
        Thread thread = new Thread(new Receiver());
        Thread thread1 = new Thread(new Sender(LOCALHOST));

        // Start Threads
        thread.start();
        thread1.start();

        // Wait until Threads finish (after CALL_LENGTH ms)
        thread.join(CALL_LENGTH * 1000);
        thread1.join(CALL_LENGTH * 1000);

        thread.stop();
        thread1.stop();

        Analyzer.close(); // Safely close static Analyzer
    }
}
