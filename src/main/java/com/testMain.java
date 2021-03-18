package com;

import securityLayer.SecurityLayer;
import securityLayer.encryption.XOR;
import utils.Analyzer;
import voipLayer.Receiver;
import voipLayer.Sender;

import javax.sound.sampled.LineUnavailableException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Locale;
import java.util.Scanner;

public class testMain {
    private static String IP;
    private static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) throws InterruptedException, UnknownHostException, LineUnavailableException, SocketException {

        //Takes in the user input for the system.
        int i = 0;
        for(String arg: args){
            if(arg.equals("-ip")){
                IP = args[i+1];
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
            SecurityLayer.sessionKey = XOR.generateSessionKey();
            runVOIP(IP);
        }else{

        }
    }

    private static void runVOIP(String IP) throws InterruptedException, SocketException, UnknownHostException, LineUnavailableException {
        // Create Receiver & Sender
        Sender sender = new Sender();
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

    }
}

