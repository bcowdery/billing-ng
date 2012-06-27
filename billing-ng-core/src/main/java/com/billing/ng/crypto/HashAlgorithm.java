/*
 BillingNG, a next-generation billing solution
 Copyright (C) 2011 Brian Cowdery

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

package com.billing.ng.crypto;

import com.billing.ng.crypto.profile.hash.*;
import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;

/**
 * Message digest algorithms available using the BouncyCastle JCE provider.
 *
 * @author Brian Cowdery
 * @since 13/01/11
 */
public enum HashAlgorithm {

    // message digest hash algorithms

    MD5         (new MessageDigestProfile("MD5"),           "MD5"),
    RipeMD128   (new MessageDigestProfile("RIPEMD128"),     "RIPEMD128"),
    RipeMD160   (new MessageDigestProfile("RIPEMD160"),     "RIPEMD160"),
    RipeMD256   (new MessageDigestProfile("RIPEMD256"),     "RIPEMD256"),
    RipeMD320   (new MessageDigestProfile("RIPEMD320"),     "RIPEMD320"),
    SHA1        (new MessageDigestProfile("SHA-1"),         "SHA-1"),
    SHA256      (new MessageDigestProfile("SHA-256"),       "SHA-256"),
    SHA384      (new MessageDigestProfile("SHA-384"),       "SHA-384"),
    SHA512      (new MessageDigestProfile("SHA-512"),       "SHA-512"),
    Tiger       (new MessageDigestProfile("Tiger"),         "Tiger"),
    GOST3411    (new MessageDigestProfile("GOST3411"),      "GOST3411"),
    Whirlpool   (new MessageDigestProfile("Whirlpool"),     "Whirlpool"),


    // secret key generator algorithms

    /** Password-Based Key Derivation Function 2, with Hash-based message authentication code (SHA-1) - 128 bit key */
    PBKDF2_128      (new PBEKeySpecProfile("PBKDF2WithHmacSHA1", 128),   "PBKDF2"),
    /** Password-Based Key Derivation Function 2, with Hash-based message authentication code (SHA-1) - 256 bit key */
    PBKDF2_256      (new PBEKeySpecProfile("PBKDF2WithHmacSHA1", 256),   "PBKDF2"),


    // 3rd party hash algorithms

    /** OpenBSD's Blowfish password hashing algorithm */
    BCrypt      (new BCryptProfile(), "BCrypt");


    static {
        Security.addProvider(new BouncyCastleProvider());
    }


    private HashProfile profile;
    private String algorithm;

    public HashProfile getProfile() { return profile; }
    public String getAlgorithm() { return algorithm; }

    HashAlgorithm(HashProfile profile, String algorithm) {
        this.profile = profile;
        this.algorithm = algorithm;
    }

    /**
     * Compute the hash digest of the given plain text string
     * and return the digested bytes.
     *
     * @param plainText plain text string
     * @return digested bytes
     */
    public byte[] digestBytes(String plainText) {
        return getProfile().digest(plainText);
    }

    /**
     * Compute the hash digest of the given plain text string and salt
     * and return the digested bytes.
     *
     * @param plainText plain text string
     * @return digested bytes
     */
    public byte[] digestBytes(String plainText, String salt) {
        return getProfile().digest(plainText, salt);
    }

    /**
     * Compute the hash of the given plain text string and return the hash value
     * as a base-64 encoded string.
     *
     * @param plainText plain text string
     * @return digested hash string
     */
    public String digest(String plainText) {
        byte[] digested = digestBytes(plainText);
        return Base64.encodeBase64String(digested);
    }

    /**
     * Compute the hash digest of the given plain text string + the given salt and
     * return the hash as a base-64 encoded string.
     *
     * @param plainText plain text string
     * @return digested hash string
     */
    public String digest(String plainText, String salt) {
        byte[] digested = digestBytes(plainText, salt);
        return Base64.encodeBase64String(digested);
    }

    /**
     * Compares a plain text string and it's salt to a previously hashed value to
     * see if the values are equal. Returns true if the plain-text matches the hash.
     *
     * @param plainText plain text to verify
     * @param salt salt value
     * @param hashed previously hashed value
     * @return true if plain text matches the hash, false otherwise.
     */
    public boolean compare(String plainText, String salt, String hashed) {
        if (getProfile().getClass().isAssignableFrom(HashComparator.class)) {
            HashComparator comparator = (HashComparator) getProfile();
            return comparator.compare(plainText, salt, hashed);
        }

        String hash = digest(plainText, salt);
        return hash.equals(hashed);
    }
}
