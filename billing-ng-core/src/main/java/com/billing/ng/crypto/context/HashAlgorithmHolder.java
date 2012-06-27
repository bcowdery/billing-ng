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

package com.billing.ng.crypto.context;

import com.billing.ng.crypto.HashAlgorithm;

import java.io.Serializable;

/**
 * Stores the {@link com.billing.ng.crypto.HashAlgorithm} being used by the system.
 *
 * @author Brian Cowdery
 * @since 16/01/11
 */
public class HashAlgorithmHolder implements Serializable {

    // default to SHA-512 unless otherwise set
    private static HashAlgorithm holder = HashAlgorithm.BCrypt;

    public static void setAlgorithm(HashAlgorithm algorithm) {
        holder = algorithm;
    }

    public static HashAlgorithm getAlgorithm() {
        return holder;
    }
}
