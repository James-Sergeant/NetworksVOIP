package com;

import com.google.common.hash.Hashing;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class Main {
    public static void main(String[] args) {
        String test = "Sodetsd17";
        System.out.println(Hashing.md5().hashString(test, Charset.defaultCharset()));
    }
}
