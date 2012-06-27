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

/**
 * Created by IntelliJ IDEA.
 * User: bcowdery
 * Date: 21/06/12
 * Time: 3:41 PM
 * To change this template use File | Settings | File Templates.
 */
public interface HashComparator {

    public boolean compare(String plainText, String salt, String hash);

}
