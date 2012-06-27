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

package com.billing.ng.entities.context;

import java.math.RoundingMode;

/**
 * Stores the preferred {@link RoundingMode} to be used by the system for all financial rounding operations.
 *
 * Common settings for financial rounding are:
 * <ul>
 *     <li>HALF_UP - round up to the 'nearest neighbour' if >= 0.5</li>
 *     <li>HALF_DOWN - round down to 'nearest neighbour' if > 0.5</li>
 *     <li>HALF_EVEN - bankers rounding</li>
 * </ul>
 *
 * The default is HALF_EVEN (Used primarily in the USA, aka. "Banker's Rounding") as it statistically minimizes
 * cumulative errors when repeatedly applied to a sequence of calculations.
 *
 * @see com.billing.ng.entities.Money
 *
 * @author Brian Cowdery
 * @since 21-June-2012
 */
public class MoneyRoundingModeHolder {

    // default to bankers rounding unless otherwise set
    private static RoundingMode holder = RoundingMode.HALF_EVEN;

    public static void SetRoundingMode(RoundingMode roundingMode) {
        holder = roundingMode;
    }

    public static RoundingMode GetRoundingMode() {
        return holder;
    }
}
