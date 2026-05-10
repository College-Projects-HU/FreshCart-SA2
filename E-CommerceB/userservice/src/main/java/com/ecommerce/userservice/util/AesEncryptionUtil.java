package com.ecommerce.userservice.util;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

@Component("aesEncryptionUtil")
public class AesEncryptionUtil {

    private static final String ALGORITHM    = "AES/GCM/NoPadding";
    private static final int    GCM_IV_LENGTH  = 12;  // 96 bits
    private static final int    GCM_TAG_LENGTH = 128; // bits

    @Value("${encryption.secret-key}")
    private String secretKeyBase64;

    private SecretKeySpec secretKey;

    @PostConstruct
    public void init() {
        byte[] keyBytes = Base64.getDecoder().decode(secretKeyBase64);
        if (keyBytes.length != 32) {
            throw new IllegalArgumentException(
                "AES-256 requires a 32-byte key. Got: " + keyBytes.length
            );
        }
        secretKey = new SecretKeySpec(keyBytes, "AES");
    }

    // ── Encrypt ───────────────────────────────────────────────────────────────

    public String encrypt(String plainText) {
        if (plainText == null) return null;
        try {
            byte[] iv = new byte[GCM_IV_LENGTH];
            new SecureRandom().nextBytes(iv);

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey,
                new GCMParameterSpec(GCM_TAG_LENGTH, iv));

            byte[] encrypted = cipher.doFinal(plainText.getBytes());

            // Prepend IV to encrypted bytes → Base64 encode
            byte[] combined = new byte[iv.length + encrypted.length];
            System.arraycopy(iv, 0, combined, 0, iv.length);
            System.arraycopy(encrypted, 0, combined, iv.length, encrypted.length);

            return Base64.getEncoder().encodeToString(combined);

        } catch (Exception e) {
            throw new RuntimeException("Encryption failed", e);
        }
    }

    // ── Decrypt ───────────────────────────────────────────────────────────────

    public String decrypt(String encryptedText) {
        if (encryptedText == null) return null;
        try {
            byte[] combined = Base64.getDecoder().decode(encryptedText);

            // Extract IV from first 12 bytes
            byte[] iv        = new byte[GCM_IV_LENGTH];
            byte[] encrypted = new byte[combined.length - GCM_IV_LENGTH];
            System.arraycopy(combined, 0, iv, 0, iv.length);
            System.arraycopy(combined, iv.length, encrypted, 0, encrypted.length);

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey,
                new GCMParameterSpec(GCM_TAG_LENGTH, iv));

            return new String(cipher.doFinal(encrypted));

        } catch (Exception e) {
            throw new RuntimeException("Decryption failed", e);
        }
    }
}