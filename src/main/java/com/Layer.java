package com;


public abstract class Layer {

    protected byte[] header = new byte[0];

    public Layer(){};

    public byte[] addHeader(byte[] payload){
        // Create new byte array
        byte[] newPayload = new byte[payload.length + header.length];

        // Prepend Audio data to the payload
        System.arraycopy(header, 0, newPayload, 0, header.length);
        System.arraycopy(payload, 0, newPayload, header.length, payload.length);

        return newPayload;
    }

    public byte[] removeHeader(byte[] payload){
        // Create new byte array
        byte[] newPayload = new byte[payload.length - header.length];

        // Remove
        System.arraycopy(payload, header.length, newPayload, 0, payload.length - header.length);

        return newPayload;
    }

    protected byte[] extractHeader(byte[] payload){
        // Create new byte array
        byte[] payloadHeader = new byte[header.length];

        // Remove
        System.arraycopy(payload, header.length, header, 0, header.length);

        return header;
    }
}
