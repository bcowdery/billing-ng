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
import com.billing.ng.crypto.profile.CipherProfile;
import com.billing.ng.crypto.profile.PublicKeyProfile;
import com.billing.ng.crypto.profile.SymmetricKeyProfile;
import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jboss.serial.finalcontainers.FinalContainer;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Cipher algorithms available using the BouncyCastle JCE provider.
 *
 * @author Brian Cowdery
 * @since 11/01/11
 */
public enum CipherAlgorithm {


    // symmetric block ciphers

    /** Data Encryption Standard */
    DES         (new SymmetricKeyProfile("DES", 64),        "DES"),
    /** Triple DES */
    DESede      (new SymmetricKeyProfile("DESede", 192),    "DESede"),
    /** Advanced Encryption Standard */
    AES         (new SymmetricKeyProfile("AES", 128),       "AES"),
    /** Blowfish */
    Blowfish    (new SymmetricKeyProfile("Blowfish", 128),  "Blowfish"),
    /** Twofish */
    Twofish     (new SymmetricKeyProfile("Twofish", 128),   "Twofish"),


    // public key ciphers

    /** RSA */
    RSA                 (new PublicKeyProfile("RSA"), "RSA"),
    /** RSA-SHA256 Signature with Optimal Asymmetric Encryption Padding */
    RSA_OAEP_SHA256     (new PublicKeyProfile("RSA"), "RSA/NONE/OAEPWithSHA256AndMGF1Padding"),
    /** RSA-SHA384 Signature with Optimal Asymmetric Encryption Padding */
    RSA_OAEP_SHA384     (new PublicKeyProfile("RSA"), "RSA/NONE/OAEPWithSHA384AndMGF1Padding"),
    /** RSA-SHA512 Signature with Optimal Asymmetric Encryption Padding */
    RSA_OAEP_SHA512     (new PublicKeyProfile("RSA"), "RSA/NONE/OAEPWithSHA512AndMGF1Padding"),
    /** RSA-MD5 Signature with Optimal Asymmetric Encryption Padding */
    RSA_OAEP_MD5        (new PublicKeyProfile("RSA"), "RSA/NONE/OAEPWithMD5AndMGF1Padding");


    private static final Map<Class<? extends CipherProfile>, CipherAlgorithm[]> valuesCache
            = Collections.synchronizedMap(new HashMap<Class<? extends CipherProfile>, CipherAlgorithm[]>());


    static {
        Security.addProvider(new BouncyCastleProvider());
    }


    private CipherProfile profile;
    private String algorithm;

    public CipherProfile getProfile() { return profile; }
    public String getAlgorithm() { return algorithm; }

    CipherAlgorithm(CipherProfile profile, String algorithm) {
        this.profile = profile;
        this.algorithm = algorithm;
    }

    /**
     * Returns an array of CipherAlgorithms using the specified type of CipherProfile.
     * @param profileType type of CipherProfile to fetch
     * @return array of cipher algorithms using the given CipherProfile type
     */
    public static CipherAlgorithm[] values(Class<? extends CipherProfile> profileType) {
        if (!valuesCache.containsKey(profileType)) {
            List<CipherAlgorithm> algorithms = new ArrayList<CipherAlgorithm>();

            for (CipherAlgorithm algorithm : values()) {
                if (algorithm.getProfile().getClass().equals(profileType)) {
                    algorithms.add(algorithm);
                }
            }

            valuesCache.put(profileType, algorithms.toArray(new CipherAlgorithm[algorithms.size()]));
        }

        return valuesCache.get(profileType);
    }

    /**
     * Returns a new Cipher instance from this CipherAlgorithm.
     * @return Cipher instance
     */
    public Cipher getCipherInstance() {
        try {
            return Cipher.getInstance(getAlgorithm(), BouncyCastleProvider.PROVIDER_NAME);
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
        return getProfile().generateKey();
    }

    /**
     * Creates a new secret key for this CipherAlgorithm using the given password.
     * @param password password for the key
     * @return key pair
     */
    public KeyPair generateKey(String password) {
        return getProfile().generateKey(password);
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
            cipher.init(Cipher.ENCRYPT_MODE, key, getProfile().getAlgorithmParameterSpec());
        } catch (InvalidKeyException e) {
            throw new RuntimeException("Security key is not valid.", e);
        } catch (InvalidAlgorithmParameterException e) {
            throw new RuntimeException("Invalid cipher algorithm parameter.", e);
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
            cipher.init(Cipher.DECRYPT_MODE, key, getProfile().getAlgorithmParameterSpec());
        } catch (InvalidKeyException e) {
            throw new RuntimeException("Security key is not valid.", e);
        } catch (InvalidAlgorithmParameterException e) {
            throw new RuntimeException("Invalid cipher algorithm parameter.", e);
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
