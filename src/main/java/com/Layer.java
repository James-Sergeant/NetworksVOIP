package com;


public abstract class Layer {

    protected byte[] header = new byte[0];

    public Layer(){}

    /**
     * addHeader is used to prepend this layer's header data to a packet's payload. The altered payload with the new
     * header is returned
     * @param payload byte[]: The packet's payload that needs a header prepended to
     * @return byte[] payload with header prepended
     */
    public byte[] addHeader(byte[] payload){
        // Create new byte array
        byte[] newPayload = new byte[payload.length + header.length];

        // Prepend Audio data to the payload
        System.arraycopy(header, 0, newPayload, 0, header.length);
        System.arraycopy(payload, 0, newPayload, header.length, payload.length);

        return newPayload;
    }

    /**
     * Protected addHeader is used by layers that require explicitly stating the header to add as a parameter.
     * @param header byte[]: The header data to prepend to payload
     * @param payload byte[]: The packet's payload that needs a header prepended to
     * @return byte[] payload with header prepended
     */
    protected byte[] addHeader(byte[] header, byte[] payload){
        // Create new byte array
        byte[] newPayload = new byte[payload.length + header.length];

        // Prepend Audio data to the payload
        System.arraycopy(header, 0, newPayload, 0, header.length);
        System.arraycopy(payload, 0, newPayload, header.length, payload.length);

        return newPayload;
    }

    /**
     * Removes this layer's header data from the payload parameter. It returns the payload provided without this layer's
     * header data
     * @param payload byte[]: The packet's payload that needs a header removed from
     * @return byte[] payload without header
     */
    public byte[] removeHeader(byte[] payload){
        // Create new byte array
        byte[] newPayload = new byte[payload.length - header.length];

        // Remove Header from payload
        System.arraycopy(payload, header.length, newPayload, 0, payload.length - header.length);

        return newPayload;
    }

    /**
     * extractHeader obtains the header data from a payload and sets the 'header' variable to the data obtained.
     * @param payload byte[]: The packet's payload to extract the header from
     */
    protected void extractHeader(byte[] payload){
        // Set header to the data extracted from the payload
        System.arraycopy(payload, 0, header, 0, header.length);
    }
}
