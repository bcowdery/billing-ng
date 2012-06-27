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

package com.billing.ng.crypto.util;

import com.billing.ng.crypto.HashAlgorithm;
import com.billing.ng.crypto.context.HashAlgorithmHolder;
import org.apache.commons.lang.StringUtils;

import java.security.SecureRandom;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: bcowdery
 * Date: 21/06/12
 * Time: 4:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class PasswordUtils {

    private static Integer saltLength;
    private static String secretKey;

    public static Integer getSaltLength() {
        if (saltLength == null) {
            String prop = System.getProperty("crypto.hash.salt_length", "256");
            saltLength = StringUtils.isNotBlank(prop) ? Integer.valueOf(prop) : 256;
        }
        return saltLength;
    }

    public static void setSaltLength(Integer saltLength) {
        PasswordUtils.saltLength = saltLength;
    }

    public static String getSecretKey() {
        if (secretKey == null) {
            secretKey = System.getProperty("crypto.secret_key", "");
        }
        return secretKey;
    }

    public static void setSecretKey(String secretKey) {
        PasswordUtils.secretKey = secretKey;
    }

    public static String hashPassword(String password, String salt) {
        String combinedSalt = getSecretKey() + salt;
        return HashUtils.generateHash(password, combinedSalt);
    }

    public static String generateHashSalt() {
        return HashUtils.generateHashSalt(getSaltLength());
    }

    public static boolean checkPassword(String plainText, String salt, String hashed) {
        String combinedSalt = getSecretKey() + salt;
        return HashAlgorithmHolder.getAlgorithm().compare(plainText, combinedSalt, hashed);
    }
}
