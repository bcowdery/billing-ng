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

package com.billing.ng.crypto.key;

import java.security.Key;

/**
 * KeyPair is a simple holder for generated keys. Like javax.crypto.KeyPair, it does
 * not enforce any security and should be treated as a PrivateKey once initialized.
 *
 * This KeyPair holder stores keys using the top-level Key interface so that
 * it can store keys regardless of the generating class (KeyGenerator, KeyPairGenerator, KeyFactory etc.).
 *
 * @author Brian Cowdery
 * @since 14/01/11
 */
public class KeyPair {

    private final Key publicKey;
    private final Key privateKey;

    public KeyPair(java.security.KeyPair keyPair) {
        this.publicKey = keyPair.getPublic();
        this.privateKey = keyPair.getPrivate();
    }

    public KeyPair(Key publicKey, Key privateKey) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    public Key getPublicKey() {
        return publicKey;
    }

    /**
     * Returns the key used for encryption. For a KeyPair generated for a symmetric block
     * cipher, the encryption and decryption key will be the same.
     *
     * @return key for encryption
     */
    public Key getEncryptionKey() {
        if (publicKey != null) return publicKey;
        return privateKey;
    }

    public Key getPrivateKey() {
        return privateKey;
    }

    /**
     * Returns the key used for decryption.
     *
     * @return key for decryption
     */
    public Key getDecryptionKey() {
        return privateKey;
    }

}
