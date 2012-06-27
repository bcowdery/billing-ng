/*
 BillingNG, a next-generation billing solution
 Copyright (C) 2012 Brian Cowdery

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

package com.billing.ng.crypto.profile.hash;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

/**
 * Created by IntelliJ IDEA.
 * User: bcowdery
 * Date: 21/06/12
 * Time: 11:43 AM
 * To change this template use File | Settings | File Templates.
 */
public class MessageDigestProfile implements HashProfile {

    private String algorithm;

    public MessageDigestProfile(String algorithm) {
        this.algorithm = algorithm;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    /**
     * Returns a new MessageDigest instance for this algorithm.
     *
     * @return MessageDigest instance
     */
    public MessageDigest getMessageDigestInstance() {
        try {
            return MessageDigest.getInstance(getAlgorithm());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Digest algorithm not supported by the JCE provider.");
        }
    }

    /**
     * Compute the hash digest of the given plain text string for this algorithm
     * and return the digested bytes.
     *
     * @param plainText plain text string
     * @return digested bytes
     */
    public byte[] digest(String plainText) {
        MessageDigest digest = getMessageDigestInstance();
        digest.reset();
        digest.update(plainText.getBytes());

        return digest.digest();
    }

    /**
     * Compute the hash digest of the given plain text string and salt for
     * this algorithm and return the digested bytes.
     *
     * @param plainText plain text string
     * @return digested bytes
     */
    public byte[] digest(String plainText, String salt) {
        return digest(plainText + salt);
    }
}
