package com;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

public class Analyzer {

    private static final String logDirectory = "logs/";
    private static BufferedWriter writer;

    public static void setup() throws IOException {
        // Get a new log file handle
        File logFile = createLogFile();
        // Initialise the file writer
        writer = new BufferedWriter(new FileWriter(logFile));
    }

    public static void logPacket(DatagramPacket packet){
        InetAddress senderAddress = packet.getAddress();
        SocketAddress receiverAddress = packet.getSocketAddress();
        long currentTime = System.currentTimeMillis();

        writeLine(senderAddress+" -> "+receiverAddress+", "+currentTime);
    }

    public static void close(){
        try{
            writer.close();
        }catch(IOException e){
            System.out.println("[ERROR] BufferedWriter failed to close");
        }
    }

    private static File createLogFile() throws IOException {
        // Get current date and time
        //DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MM yyyy HH mm ss LOG");
        //LocalDateTime now = LocalDateTime.now();

        // Specify the file name and path
        File file = new File(logDirectory+"Test_LOG.txt");

        // Create file if not already exists
        if (!file.exists()) {
            file.createNewFile();
        }

        return file;
    }

    private static void writeLine(String line){
        try {
            writer.write(line);
            writer.newLine();
        }catch(IOException e){
            System.out.println("[ERROR] BufferedWriter failed to write line:\n"+line);
        }
    }

}
