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

import org.apache.commons.lang.StringUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

/**
 * Created by IntelliJ IDEA.
 * User: bcowdery
 * Date: 21/06/12
 * Time: 11:43 AM
 * To change this template use File | Settings | File Templates.
 */
public class PBEKeySpecProfile implements HashProfile {

    private static Integer workFactor;

    private Integer getWorkFactor() {
        if (workFactor == null) {
            String prop = System.getProperty("crypto.hash.work_factor", "1024");
            workFactor = StringUtils.isNotBlank(prop) ? Integer.valueOf(prop) : 1024;
        }
        return workFactor;
    }

    private String algorithm;
    private int keyLength;

    public PBEKeySpecProfile(String algorithm, int keyLength) {
        this.algorithm = algorithm;
        this.keyLength = keyLength;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public int getKeyLength() {
        return keyLength;
    }

    public SecretKeyFactory getSecretKeyFactory() {
        try {
            return SecretKeyFactory.getInstance(getAlgorithm());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Secret Key algorithm not supported by the JCE provider.");
        }
    }

    @Override
    public byte[] digest(String plainText) {
        throw new UnsupportedOperationException("Cannot generate a PBE secret key without a salt.");
    }

    @Override
    public byte[] digest(String plainText, String salt) {
        KeySpec spec = new PBEKeySpec(plainText.toCharArray(), salt.getBytes(), getWorkFactor(), getKeyLength());

        SecretKey key;
        try {
            key = getSecretKeyFactory().generateSecret(spec);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException("Key generator algorithm not supported by the JCE provider.");
        }
        return key.getEncoded();
    }
}
