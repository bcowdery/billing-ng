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
import org.mindrot.jbcrypt.BCrypt;

/**
 * Created by IntelliJ IDEA.
 * User: bcowdery
 * Date: 21/06/12
 * Time: 3:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class BCryptProfile implements HashProfile, HashComparator {

    private static Integer workFactor;

    private Integer getWorkFactor() {
        if (workFactor == null) {
            String prop = System.getProperty("crypto.hash.work_factor", "12");
            workFactor = StringUtils.isNotBlank(prop) ? Integer.valueOf(prop) : 12;
        }
        return workFactor;
    }

    @Override
    public byte[] digest(String plainText) {
        String hash = BCrypt.hashpw(plainText, BCrypt.gensalt(getWorkFactor()));
        return hash.getBytes();
    }

    @Override
    public byte[] digest(String plainText, String salt) {
        return digest(plainText);
    }

    @Override
    public boolean compare(String plainText, String salt, String hashed) {
        return BCrypt.checkpw(plainText, hashed);
    }
}
