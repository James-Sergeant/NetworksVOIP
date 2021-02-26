package utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketAddress;

public class Analyzer {

    // QoS VARIABLES
    public static final int HEADER_LENGTH = 8; // 4 Bytes
    private static int totalBytes = 0;

    // LOGGING VARIABLES
    private static boolean consoleOutput;
    private static final String logDirectory = "logs/";
    private static BufferedWriter writer;
    private static InetAddress LOCAL_ADDRESS;

    // PACKET LOGGING INFO
    private static int sendPacketNumber;
    private static int receivePacketNumber;

    /**
     * Method to initialise Analyzer variables to enable logging of packets.
     * @param consoleOutput Boolean: Enable/Disable printing to the console
     * @throws IOException Thrown when log file creation fails
     */
    public static void setup(boolean consoleOutput) throws IOException {
        // Sets console output
        Analyzer.consoleOutput = consoleOutput;

        // Get a new log file handle
        File logFile = createLogFile();
        // Initialise the file writer
        writer = new BufferedWriter(new FileWriter(logFile));

        LOCAL_ADDRESS = InetAddress.getLocalHost();
    }


    public static void logIncomingPacket(DatagramPacket packet){
        SocketAddress senderAddress = packet.getSocketAddress();
        String line = String.format("%5s : %30s <- %30s, %4s bytes", receivePacketNumber,LOCAL_ADDRESS , senderAddress, packet.getLength());
        writeLine(line);
    }


    public static void logOutgoingPacket(DatagramPacket packet){
        SocketAddress sentToAddress = packet.getSocketAddress();
        String line = String.format("%5s : %30s -> %30s, %4s bytes", sendPacketNumber, LOCAL_ADDRESS , sentToAddress, packet.getLength());
        writeLine(line);
    }
    
    /**
     * Logs data about a packet to the log file. Data that is logged is:
     * Unique packet number, src & dst IP addresses, payload size, time
     * @param packet DatagramPacket: The packet to be logged
     */
    public static void logPacket(DatagramPacket packet){
        InetAddress senderAddress = packet.getAddress();
        SocketAddress receiverAddress = packet.getSocketAddress();

        // Extracts analyser data from packet
        byte[] bytesTime = new byte[Analyzer.HEADER_LENGTH];
        System.arraycopy(packet.getData(),0, bytesTime, 0, Analyzer.HEADER_LENGTH);
        // Delay
        long longTime = Utils.bytesToLong(bytesTime);
        long delay = System.currentTimeMillis() - longTime;

        String line = String.format("%10s : %30s, %30s, %4s bytes, %6s ms", packet.getData()[0]*packet.getData()[50],senderAddress,receiverAddress,packet.getLength(), delay);
        //writeLine(line);
    }

    /**
     * Safely closes the BufferedReader. Should be called at the end of the program
     */
    public static void close(){
        try{
            writer.close();
        }catch(IOException e){
            System.out.println("[ERROR] BufferedWriter failed to close");
        }
    }

    public static void setSendPacketNumber(int sendPacketNumber) {
        Analyzer.sendPacketNumber = sendPacketNumber;
    }

    public static void setReceivePacketNumber(int receivePacketNumber) {
        Analyzer.receivePacketNumber = receivePacketNumber;
    }

    /**
     * Toggles whether logging should also output lines to the console
     */
    public static void toggleConsoleOutput(){
        consoleOutput ^= true;
    }

    /**
     * Private method used to generate a new text file to store the log data.
     * @return File: The newly create file handle.
     * @throws IOException Thrown if file fails to be found or created.
     */
    private static File createLogFile() throws IOException {

        // Specify the file name and path
        File file = new File(logDirectory+"Test_LOG.txt");

        // Create file if not already exists
        if(!file.exists()){
            file.getParentFile().mkdirs();
            file.createNewFile();
        }

        return file;
    }

    /**
     * Private method that uses BufferedWriter to write a line to the log file.
     * @param line String: The line to write.
     */
    private static void writeLine(String line){
        try {
            writer.write(line+"\n");
            if(consoleOutput) System.out.println(line);
        }catch(IOException e){
            System.out.println("[ERROR] BufferedWriter failed to write line:\n"+line);
        }
    }

}
