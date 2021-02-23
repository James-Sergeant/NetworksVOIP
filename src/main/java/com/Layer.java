package com;

public interface Layer {
    byte[] addHeader(byte[] payload);
    byte[] removeHeader(byte[] payload);
    byte[] getHeader(byte[] payload);
}
