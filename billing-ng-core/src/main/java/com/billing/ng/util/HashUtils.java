package com.billing.ng.util;

import org.apache.commons.codec.binary.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * HashUtils
 *
 * @author Brian Cowdery
 * @since 25-Apr-2010
 */
public class HashUtils {
    private static final char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".toCharArray();

    /**
     * Generates an alphanumeric salted hash token using an SHA-256 hashing algorithm. The generated hash
     * is stripped of all non-alphanumeric characters to make it safe for use as an HTTP GET parameter value.
     *
     * @param base string to use as the primary basis of the hash
     * @param appends additional strings to append to the plain-text password before hashing
     * @return hash string
     */
    public static String generateHash(String base, String... appends) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Unsupported digest algorithm, check JDK deployemnt.", e);
        }

        StringBuffer plainText = new StringBuffer();
        plainText.append(base);
        for (String string : appends)
            plainText.append(string);

        digest.reset();
        digest.update(plainText.toString().getBytes());

        return Base64.encodeBase64URLSafeString(digest.digest());
    }

    /**
     * Generates a string of random alphanumeric characters of the given length. The generated salt can be
     * used to add entropy to a hashing algorithm or token generator, thus making the result harder to crack.
     *
     * @param length length of salt string to generate
     * @return salt string
     */
    public static String generateHashSalt(int length) {
        Random random = new Random();

        StringBuffer salt = new StringBuffer(length);
        for (int i = 0; i < length; i++)
            salt.append(chars[random.nextInt(chars.length)]);

        return salt.toString();
    }    
}
