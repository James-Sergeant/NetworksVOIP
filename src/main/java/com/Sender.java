package com;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Sender {
    public static byte DATAGRAM_SOCKET_ONE = 0;
    public static byte DATAGRAM_SOCKET_TWO = 1;
    public static byte DATAGRAM_SOCKET_THREE = 2;

    private final int PORT;
    private final InetAddress IP;
    private final DatagramSocket SENDER_SOCKET;

    public Sender() throws UnknownHostException, SocketException {
        PORT = 55555;
        IP = InetAddress.getByName("localhost");
        SENDER_SOCKET = new DatagramSocket();
    }

    public Sender(String IP) throws UnknownHostException, SocketException {
        PORT = 55555;
        this.IP = InetAddress.getByName(IP);
        SENDER_SOCKET = new DatagramSocket();
    }

    public Sender(String IP,int PORT) throws UnknownHostException, SocketException {
        this.PORT = PORT;
        this.IP = InetAddress.getByName(IP);
        SENDER_SOCKET = new DatagramSocket();
    }

}
