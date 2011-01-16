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

import com.billing.ng.crypto.key.KeyPair;
import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;

import static com.billing.ng.crypto.CipherAlgorithmKeyType.*;

/**
 * Cipher algorithms available using the BouncyCastle JCE provider.
 *
 * @author Brian Cowdery
 * @since 11/01/11
 */
public enum CipherAlgorithm {


    // symmetric block ciphers

    /** Data Encryption Standard */
    DES         (SYMMETRIC_KEY, "DES"),
    /** Triple DES */
    DESede      (SYMMETRIC_KEY, "DESede"),
    /** Advanced Encryption Standard */
    AES         (SYMMETRIC_KEY, 128, "AES"),
    /** Blowfish */
    Blowfish    (SYMMETRIC_KEY, "Blowfish"),


    // public key ciphers

    /** RSA */
    RSA                 (PUBLIC_KEY, "RSA"),
    /** RSA-SHA256 Signature with Optimal Asymmetric Encryption Padding */
    RSA_OAEP_SHA256     (PUBLIC_KEY, "RSA", "NONE", "OAEPWithSHA256AndMGF1Padding"),
    /** RSA-SHA384 Signature with Optimal Asymmetric Encryption Padding */
    RSA_OAEP_SHA384     (PUBLIC_KEY, "RSA", "NONE", "OAEPWithSHA384AndMGF1Padding"),
    /** RSA-SHA512 Signature with Optimal Asymmetric Encryption Padding */
    RSA_OAEP_SHA512     (PUBLIC_KEY, "RSA", "NONE", "OAEPWithSHA512AndMGF1Padding"),
    /** RSA-MD5 Signature with Optimal Asymmetric Encryption Padding */
    RSA_OAEP_MD5        (PUBLIC_KEY, "RSA", "NONE", "OAEPWithMD5AndMGF1Padding");


    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    private CipherAlgorithmKeyType type;
    private Integer keySize;
    private String algorithm;
    private String mode;
    private String padding;

    public CipherAlgorithmKeyType getType() { return type; }
    public Integer getKeySize() { return keySize; }
    public String getAlgorithm() { return algorithm; }
    public String getMode() { return mode; }
    public String getPadding() { return padding; }

    CipherAlgorithm(CipherAlgorithmKeyType type, String algorithm) {
        this.type = type;
        this.algorithm = algorithm;
    }

    CipherAlgorithm(CipherAlgorithmKeyType type, Integer keySize, String algorithm) {
        this.type = type;
        this.keySize = keySize;
        this.algorithm = algorithm;
    }

    CipherAlgorithm(CipherAlgorithmKeyType type, String algorithm, String mode, String padding) {
        this.type = type;
        this.algorithm = algorithm;
        this.mode = mode;
        this.padding = padding;
    }

    CipherAlgorithm(CipherAlgorithmKeyType type, Integer keySize, String algorithm, String mode, String padding) {
        this.type = type;
        this.keySize = keySize;
        this.algorithm = algorithm;
        this.mode = mode;
        this.padding = padding;
    }

    /**
     * Builds and returns the algorithm specification string for this CipherAlgorithm. Algorithm
     * specification strings are in the format "ALGORITHM/MODE/PADDING".
     *
     * @return algorithm specification string
     */
    public String getAlgorithmSpecification() {
        StringBuilder builder = new StringBuilder();
        builder.append(algorithm);

        if (mode != null)
            builder.append('/').append(mode);

        if (padding != null)
            builder.append('/').append(padding);

        return builder.toString();
    }

    /**
     * Returns a new Cipher instance from this CipherAlgorithm.
     * @return Cipher instance
     */
    public Cipher getCipherInstance() {
        try {
            return Cipher.getInstance(getAlgorithmSpecification(), BouncyCastleProvider.PROVIDER_NAME);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Cipher algorithm not supported by the JCE provider.");
        } catch (NoSuchProviderException e) {
            throw new RuntimeException("BouncyCastle JCE provider not configured or not loaded.");
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException("Cipher padding algorithm not supported by the JCE provider.");
        }
    }

    /**
     * Generates a random key for this CipherAlgorithm.
     * @return key pair
     */
    public KeyPair generateKey() {
        return getType().generateKey(getAlgorithm(), getKeySize());
    }

    /**
     * Encrypt the given plain text string using this Cipher algorithm.
     *
     * @param key secure key
     * @param plainText plain text string to encrypt
     * @return encrypted string
     */
    public synchronized String encrypt(Key key, String plainText) {
        Cipher cipher = getCipherInstance();
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key);
        } catch (InvalidKeyException e) {
            throw new RuntimeException("Security key is not valid.");
        }

        try {
            byte[] encrypted = cipher.doFinal(plainText.getBytes());
            return Base64.encodeBase64String(encrypted);

        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException("Length of plain text data does not match the block size of this algorithm.");
        } catch (BadPaddingException e) {
            throw new RuntimeException("Padding mechanism of this algorithm cannot be used with the given data.");
        }
    }

    /**
     * Decrypts an encrypted string using the given Cipher algorithm.
     *
     * @param key secure key
     * @param encryptedText encrypted string to decrypt
     * @return decrypted string
     */
    public synchronized String decrypt(Key key, String encryptedText) {
        Cipher cipher = getCipherInstance();
        try {
            cipher.init(Cipher.DECRYPT_MODE, key);
        } catch (InvalidKeyException e) {
            throw new RuntimeException("Security key is not valid.");
        }

        try {
            byte[] decrypted = cipher.doFinal(Base64.decodeBase64(encryptedText));
            return new String(decrypted);

        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException("Encrypted text does not match the block size of this algorithm.");
        } catch (BadPaddingException e) {
            throw new RuntimeException("Padding mechanism of this algorithm does not match the encrypted text.");
        }
    }
}
