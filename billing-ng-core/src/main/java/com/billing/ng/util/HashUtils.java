/*
 BillingNG, a next-generation billing solution
 Copyright (C) 2010 Brian Cowdery

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU Affero General Public License as
 published by the Free Software Foundation, either version 3 of the
 License, or (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU Affero General Public License for more details.
 You should have received a copy of the GNU Affero General Public License
 along with this program.  If not, see http://www.gnu.org/licenses/agpl-3.0.html
 */

package com.billing.ng.util;

import com.billing.ng.crypto.DigestAlgorithm;
import com.billing.ng.crypto.context.DigestAlgorithmHolder;
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
     * Generates an alphanumeric salted hash token using the configured digest algorithm. The generated hash
     * is stripped of all non-alphanumeric characters to make it safe for use as an HTTP GET parameter value.
     *
     * @see DigestAlgorithmHolder
     *
     * @param base string to use as the primary basis of the hash
     * @param appends additional strings to append to the plain-text password before hashing
     * @return hash string
     */
    public static String generateHash(String base, String... appends) {
        StringBuffer plainText = new StringBuffer();
        plainText.append(base);
        for (String string : appends)
            plainText.append(string);

        DigestAlgorithm algorithm = DigestAlgorithmHolder.getAlgorithm();

        byte[] bytes = algorithm.digestBytes(plainText.toString());
        return Base64.encodeBase64URLSafeString(bytes);
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
