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

package com.billing.ng.crypto.profile;

import com.billing.ng.crypto.key.KeyPair;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.spec.PBEParameterSpec;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.AlgorithmParameterSpec;

/**
 * A simple {@link CipherProfile} for public-key ciphers. Public-key ciphers use separate keys
 * (a key pair) for encryption and decryption of data. Data encrypted with a public-key cipher
 * can only be decrypted using the private key. It is safe to share the public key with
 * clients so that they can encrypt data before sending it to the system as the public key
 * cannot be used to decrypt information.
 *
 * @author Brian Cowdery
 * @since 16/01/11
 */
public class PublicKeyProfile implements CipherProfile {

    private final String identifier;
    private final Integer keysize;

    public PublicKeyProfile() {
        this.identifier = null;
        this.keysize = null;
    }

    public PublicKeyProfile(String identifier) {
        this.identifier = identifier;
        this.keysize = null;
    }

    public PublicKeyProfile(String identifier, Integer keysize) {
        this.identifier = identifier;
        this.keysize = keysize;
    }

    public AlgorithmParameterSpec getAlgorithmParameterSpec() {
        return null;
    }

    public KeyPair generateKey() {
        KeyPairGenerator keyGen;
        try {
            keyGen = KeyPairGenerator.getInstance(identifier, BouncyCastleProvider.PROVIDER_NAME);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Key generator algorithm not supported by the JCE provider.");
        } catch (NoSuchProviderException e) {
            throw new RuntimeException("BouncyCastle JCE provider not configured or not loaded.");
        }

        if (keysize != null) keyGen.initialize(keysize);

        return new KeyPair(keyGen.generateKeyPair());
    }

    public KeyPair getKey(String password) {
        // todo: http://stackoverflow.com/questions/5127379/how-to-generate-a-rsa-keypair-with-a-privatekey-encrypted-with-password
        throw new UnsupportedOperationException("PublicKeyProfile currently does not support password based encryption keys.");
    }
}
