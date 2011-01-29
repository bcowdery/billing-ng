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
public enum DigestAlgorithm {

    MD5         ("MD5"),
    RipeMD128   ("RIPEMD128"),
    RipeMD160   ("RIPEMD160"),
    RipeMD256   ("RIPEMD256"),
    RipeMD320   ("RIPEMD320"),
    SHA1        ("SHA-1"),
    SHA256      ("SHA-256"),
    SHA384      ("SHA-384"),
    SHA512      ("SHA-512"),
    Tiger       ("Tiger"),
    GOST3411    ("GOST3411"),
    Whirlpool   ("Whirlpool");


    static {
        Security.addProvider(new BouncyCastleProvider());
    }


    private String algorithm;
    public String getAlgorithm() { return algorithm; }
    DigestAlgorithm(String algorithm) { this.algorithm = algorithm; }

    /**
     * Returns a new MessageDigest instance for this algorithm.
     *
     * @return MessageDigest instance
     */
    public MessageDigest getMessageDigestInstance() {
        try {
            return MessageDigest.getInstance(getAlgorithm(), BouncyCastleProvider.PROVIDER_NAME);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Digest algorithm not supported by the JCE provider.");
        } catch (NoSuchProviderException e) {
            throw new RuntimeException("BouncyCastle JCE provider not configured or not loaded.");
        }
    }

    /**
     * Compute the hash digest of the given plain text string for this algorithm
     * and return the digested bytes.
     *
     * @param plainText plain text string
     * @return digested bytes
     */
    public byte[] digestBytes(String plainText) {
        MessageDigest digest = getMessageDigestInstance();
        digest.reset();
        digest.update(plainText.getBytes());

        return digest.digest();
    }

    /**
     * Compute the hash digest of the given plain text string for this algorithm
     * and return the hash as a base-64 encoded string.
     *
     * @param plainText plain text string
     * @return digested hash string
     */
    public String digest(String plainText) {
        byte[] digested = digestBytes(plainText);
        return Base64.encodeBase64String(digested);
    }
}
