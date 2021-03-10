package com;

public class Config {

    // DATAGRAM SOCKET

    public static final int DATAGRAM_SOCKET = 2; // 1, 2 or 3 (1 default)
    public static final int CALL_LENGTH = 50; // Seconds

    // AUDIO RECEIVING BUFFER

    public static final double BUFFER_DELAY = 1.0; // Seconds
    public static final boolean REORDER = true; // Re-Order packets when receiving

    // AUDIO SOLUTIONS

    public enum PLOSS_SOLUTION{REPETITION, BLANK_FILL_IN, INTERPOLATION}

    public static final PLOSS_SOLUTION PACKET_LOSS_SOLUTION = PLOSS_SOLUTION.INTERPOLATION;
    public static final boolean INTERLEAVER = false; // Enable / Disable Block Interleaving
    public static final int INTERLEAVER_SIZE = 5; // Size of block interleaver (3*3, 4*4, etc.)

    // ENCRYPTION

    public static final boolean ENCRYPT = true;
}
