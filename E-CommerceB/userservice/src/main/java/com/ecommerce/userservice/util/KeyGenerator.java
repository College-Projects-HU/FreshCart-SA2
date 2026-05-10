package com.ecommerce.userservice.util;

import java.security.SecureRandom;
import java.util.Base64;

public class KeyGenerator {
    public static void main(String[] args) {
        byte[] key = new byte[32]; // 256 bits = 32 bytes
        new SecureRandom().nextBytes(key);
        System.out.println("Your AES-256 Key:");
        System.out.println(Base64.getEncoder().encodeToString(key));
    }
}