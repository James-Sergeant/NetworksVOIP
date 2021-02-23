package com;


public abstract class Layer {

    protected byte[] header = new byte[0];

    public Layer(){}

    public byte[] addHeader(byte[] payload){
        // Create new byte array
        byte[] newPayload = new byte[payload.length + header.length];

        // Prepend Audio data to the payload
        System.arraycopy(header, 0, newPayload, 0, header.length);
        System.arraycopy(payload, 0, newPayload, header.length, payload.length);

        return newPayload;
    }

    protected byte[] addHeader(byte[] header, byte[] payload){
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

        // Remove Header from payload
        System.arraycopy(payload, header.length, newPayload, 0, payload.length - header.length);

        return newPayload;
    }

    protected void extractHeader(byte[] payload){
        // Set header to the data extracted from the payload
        System.arraycopy(payload, 0, header, 0, header.length);
    }
}
