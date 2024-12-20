package com.cdac.mumbai.common;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

public class CredentialsForUser {

    private static final Random RANDOM = new SecureRandom();
    private static final int PASSWORD_LENGTH = 12;
    private static final String SYMBOLS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789$&@?!%#";

    /**
     * Generates a unique username based on userType and a random sequence.
     * 
     * @param userType the type of user
     * @return a unique username
     */
    public static String getUsername(String userType) {
        int randomSequence = RANDOM.nextInt(10000);
        String sequence = String.format("%05d", randomSequence);
        return userType + sequence;
    }

    /**
     * Generates a strong random password of length 12 with uppercase, lowercase, digits, and special characters.
     * 
     * @return a strong password
     */
    public static String genPassword() {
        while (true) {
            char[] password = new char[PASSWORD_LENGTH];
            boolean hasUpper = false, hasLower = false, hasDigit = false, hasSpecial = false;

            for (int i = 0; i < password.length; i++) {
                char ch = SYMBOLS.charAt(RANDOM.nextInt(SYMBOLS.length()));
                if (Character.isUpperCase(ch)) {
                    hasUpper = true;
                } else if (Character.isLowerCase(ch)) {
                    hasLower = true;
                } else if (Character.isDigit(ch)) {
                    hasDigit = true;
                } else {
                    hasSpecial = true;
                }
                password[i] = ch;
            }

            if (hasUpper && hasLower && hasDigit && hasSpecial) {
                return new String(password);
            }
        }
    }

    /**
     * Generates a random string using SHA-1 and a given input string.
     * 
     * @param input the input string
     * @return a substring of the SHA-1 hash of the input
     */
    public static String generateRandomString(String input) {
        try {
            SecureRandom prng = SecureRandom.getInstance("SHA1PRNG");
            String randomNum = Integer.toString(prng.nextInt()) + input;
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            byte[] result = sha.digest(randomNum.getBytes());
            return hexEncode(result).substring(0, 16);  // Extract a substring of the hash
        } catch (NoSuchAlgorithmException e) {
            return System.currentTimeMillis() + "_" + input;
        }
    }

    /**
     * Encodes a byte array into a hex string.
     * 
     * @param input the byte array to encode
     * @return a hex-encoded string
     */
    private static String hexEncode(byte[] input) {
        StringBuilder result = new StringBuilder();
        char[] digits = "0123456789abcdef".toCharArray();
        for (byte b : input) {
            result.append(digits[(b >> 4) & 0xF]);
            result.append(digits[b & 0xF]);
        }
        return result.toString();
    }

    /**
     * Generates a SHA-256 hash of the given message.
     * 
     * @param message the input message
     * @return a SHA-256 hash of the input message
     * @throws Exception if hashing fails
     */
    public static String generateSHA256(String message) throws Exception {
        return hashString(message, "SHA-256");
    }

    /**
     * Hashes a string using the specified algorithm.
     * 
     * @param message the input message
     * @param algorithm the hashing algorithm
     * @return a hashed string
     * @throws Exception if hashing fails
     */
    private static String hashString(String message, String algorithm) throws Exception {
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            byte[] hashedBytes = digest.digest(message.getBytes("UTF-8"));
            return convertByteArrayToHexString(hashedBytes).substring(0, 45);
        } catch (Exception ex) {
            throw new Exception("Could not generate hash from String", ex);
        }
    }

    /**
     * Converts a byte array to a hex string.
     * 
     * @param arrayBytes the byte array
     * @return a hex string
     */
    private static String convertByteArrayToHexString(byte[] arrayBytes) {
        StringBuilder stringBuffer = new StringBuilder();
        for (byte b : arrayBytes) {
            stringBuffer.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        }
        return stringBuffer.toString();
    }
}
