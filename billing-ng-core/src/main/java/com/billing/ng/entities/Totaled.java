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

package com.billing.ng.entities;

/**
 * Interface for classes that calculate a total.
 *
 * Implementing classes should ensure that they always return the most up-to-date total
 * possible by re-calculating the total when relevant attributes are updated. Check the
 * implementing class documentation for possible caveats.
 * 
 * @author Brian Cowdery
 * @since 2-Dec-2010
 */
public interface Totaled {

    public Money getTotal();
    
    public void calculateTotal();

}
