package com;

import securityLayer.SecurityLayer;
import securityLayer.encryption.XOR;
import securityLayer.session.Session;
import utils.Analyzer;
import voipLayer.Receiver;
import voipLayer.Sender;

import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Locale;
import java.util.Scanner;

public class Main {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_BLUE = "\u001B[34m";

    public static String IP= "";
    private static final Scanner scanner = new Scanner(System.in);

    public static final String SERGEANT_IP = "109.147.42.239";
    public static final String BURLING_IP = "86.154.116.23";

    public static void main(String[] args) throws InterruptedException, IOException, LineUnavailableException {
        /*System.setErr(new PrintStream(new OutputStream() {
            @Override
            public void write(int b) throws IOException {
            }
        }));
         */
        //Takes in the user input for the system.
        int i = 0;
        for(String arg: args){
            if(arg.equals("-ip")){
                IP = args[i+1];
                if(IP.equals("b")){
                    IP = BURLING_IP;
                }
                if (IP.equals("s")){
                    IP=SERGEANT_IP;
                }
            }else if(arg.equals("-s")){
                String x = args[i+1];
                for(Config.PRESET preset: Config.PRESET.values()){
                    if(x.toUpperCase(Locale.ROOT).equals(preset.name())){
                        Config.preset = preset;
                        break;
                    }
                }
            }
            i++;
        }

        if(IP.toLowerCase(Locale.ROOT).equals("localhost")){
            SecurityLayer.xor = new XOR(XOR.generateSessionKey());
            runVOIP(IP);
        }else if(IP.equals("")){
            new Session();
            runVOIP(IP);
        }else{
            new Session(IP);
            runVOIP(IP);
        }
    }

    private static void runVOIP(String senderIP) throws InterruptedException, SocketException, UnknownHostException, LineUnavailableException {
        System.out.println(ANSI_GREEN+"Connection Established, running VOIP system!"+ANSI_RESET);
        // Create Receiver & Sender
        Sender sender = new Sender(senderIP);
        Receiver receiver = new Receiver();

        // Create Threads
        Thread receiverThread = new Thread(receiver);
        Thread senderThread = new Thread(sender);

        // Start Threads
        receiverThread.start();
        senderThread.start();
        String exit = "";

        while(!(exit.toLowerCase(Locale.ROOT).equals("exit"))) exit = scanner.nextLine();

        sender.toggleSending();
        receiver.toggleReceiving();

        receiverThread.join();
        senderThread.join();
        System.out.println(ANSI_BLUE+"Connection Closed, Goodbye!"+ANSI_RESET);
    }
}

