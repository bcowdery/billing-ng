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
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.KeyGenerator;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.Security;

/**
 * CipherType
 *
 * @author Brian Cowdery
 * @since 16/01/11
 */
public enum CipherAlgorithmKeyType {

    /** Symmetric block ciphers that use the same key for encrypting and decrypting data. */
    SYMMETRIC_KEY {
        KeyPair generateKey(String algorithm, Integer keySize) {
            KeyGenerator keyGen;
            try {
                keyGen = KeyGenerator.getInstance(algorithm, BouncyCastleProvider.PROVIDER_NAME);
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException("Key generator algorithm not supported by the JCE provider.");
            } catch (NoSuchProviderException e) {
                throw new RuntimeException("BouncyCastle JCE provider not configured or not loaded.");
            }

            if (keySize != null) {
                keyGen.init(keySize, new SecureRandom());
            } else {
                keyGen.init(new SecureRandom());
            }

            return new KeyPair(null, keyGen.generateKey());
        }
    },

    /** Public-key ciphers that use a separate public and private key for encrypting and decrypting data. */
    PUBLIC_KEY {
        KeyPair generateKey(String algorithm, Integer keySize) {
            KeyPairGenerator keyGen;
            try {
                keyGen = KeyPairGenerator.getInstance(algorithm, BouncyCastleProvider.PROVIDER_NAME);
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException("Key generator algorithm not supported by the JCE provider.");
            } catch (NoSuchProviderException e) {
                throw new RuntimeException("BouncyCastle JCE provider not configured or not loaded.");
            }

            if (keySize != null)
                keyGen.initialize(keySize, new SecureRandom());

            return new KeyPair(keyGen.generateKeyPair());
        }
    };


    static {
        Security.addProvider(new BouncyCastleProvider());
    }


    /**
     * Generates a random key pair for the given algorithm. Depending on the key type (SYMMETRIC_KEY or
     * PUBLIC_KEY), a single key will be generated or a private/public key pair will generated. See the
     * documentation for {@link KeyPair} for more information.
     *
     * @param algorithm cipher algorithm name
     * @param keySize optional key size in bytes
     * @return generated key pair
     */
    abstract KeyPair generateKey(String algorithm, Integer keySize);
}
