package com;

import com.google.common.hash.Hashing;

import javax.sound.sampled.LineUnavailableException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class Main {
    public static void main(String[] args) throws LineUnavailableException, SocketException, UnknownHostException {
        Thread thread = new Thread(new Receiver());
        Thread thread1 = new Thread(new Sender());

        thread.start();
        thread1.start();
    }
}
