package com.cdac.mumbai.common;


import java.io.IOException;
import java.io.UnsupportedEncodingException;

import lombok.extern.slf4j.*;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;

import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RSACipher {

	  // Public Key
    private static final String PUBLIC_KEY_STRING = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAK6r9oOLqaUEzIA0W9LNnmPtP2YePfWNtiU32dKHCNdQrGyl2mx6xUcxUWD7dWwPAqLfwZVVqK8jwgPk6KkdJKkCAwEAAQ==";

    // Private Key
    private static final String PRIVATE_KEY_STRING = "MIIBVQIBADANBgkqhkiG9w0BAQEFAASCAT8wggE7AgEAAkEArqv2g4uppQTMgDRb0s2eY+0/Zh499Y22JTfZ0ocI11CsbKXabHrFRzFRYPt1bA8Cot/BlVWoryPCA+ToqR0kqQIDAQABAkAUB9VbrXr1WREIjLiQ+pJ03CKel848KBWgvABnM6QQaNd+c/PZMwD2OJuX3aUkmGesCBVICR2KvG/opzdBcIMZAiEAuCvwnEzdmkayDHsonPnKHSBQsITkOUGtaGZwmeD7LyUCIQDyy4iGWUmbKd088ehoM2eDLCZHRXcwc0VPwDG8HlY6NQIgTaA/lzGSyd74VuhmMtvaA7LGL9CjfTqgg3uCBGnelX0CIQCQNJ0xFqUmOJZD1XZxNV7cxQMzP6oMJ9raBFBF6ErUpQIhALYCqJSiS+qIwKP1qJRbQKvL0EbaRp+weCThEd0qbHaj";

    public String encrypt(String rawText) throws GeneralSecurityException, UnsupportedEncodingException {
        // Convert public key string to PublicKey object
        byte[] publicKeyBytes = Base64.getDecoder().decode(PUBLIC_KEY_STRING);
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);

        // Encrypt raw text
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedBytes = cipher.doFinal(rawText.getBytes("ISO-8859-1"));

        // Return Base64 encoded string
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public String decrypt(String cipherText) throws GeneralSecurityException, UnsupportedEncodingException {
        // Convert private key string to PrivateKey object
        byte[] privateKeyBytes = Base64.getDecoder().decode(PRIVATE_KEY_STRING);
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);

        // Decrypt cipher text
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(cipherText));

        // Return decrypted text
        return new String(decryptedBytes, "ISO-8859-1");
    }


    public String encrypt(String rawText, PublicKey publicKey, String transformation, String encoding)
            throws IOException, GeneralSecurityException {

        Cipher cipher = Cipher.getInstance(transformation);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        return Base64.getEncoder().encodeToString(cipher.doFinal(rawText.getBytes(encoding)));
    }

    public String decrypt(String cipherText, PrivateKey privateKey, String transformation, String encoding)
            throws IOException, GeneralSecurityException {

        Cipher cipher = Cipher.getInstance(transformation);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        return new String(cipher.doFinal(Base64.getDecoder().decode(cipherText)), encoding);
    }
}