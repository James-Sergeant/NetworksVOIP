package com;

import com.google.common.hash.Hashing;

import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class Main {

    public static void main(String[] args) throws LineUnavailableException, SocketException, UnknownHostException, IOException, InterruptedException {

        long runTime = 50000; // in milliseconds
        Analyzer.setup(false); // Setup static Analyser

        // Create Threads
        Thread thread = new Thread(new Receiver());
        Thread thread1 = new Thread(new Sender("109.147.42.239"));

        // Start Threads
        thread.start();
        thread1.start();

        // Wait until Threads finish (after runTime ms)
        thread.join(runTime);
        thread1.join(runTime);

        thread.stop();
        thread1.stop();

        Analyzer.close(); // Safely close static Analyzer
    }
}
