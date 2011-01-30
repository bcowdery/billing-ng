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

import org.joda.time.DateMidnight;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * CurrentBillingCycleTest
 *
 * @author Brian Cowdery
 * @since 29/01/11
 */
@Test(groups = {"entity", "quick", "billing"})
public class CurrentBillingCycleTest {

    @Test
    public void testCalculateCycleNumberDays() {
        BillingPeriod period = new BillingPeriod(BillingPeriod.Type.DAY, 5);
        DateMidnight start = new DateMidnight(2010, 1, 1);

        // January 11 2010 = 10 days, 2 cycles
        assertThat(CurrentBillingCycle.calculateCycleNumber(period, start, new DateMidnight(2010, 1, 11)), is(2));

        // January 15 2010 = 14 days, 2 cycles (still within the 2nd cycle)
        assertThat(CurrentBillingCycle.calculateCycleNumber(period, start, new DateMidnight(2010, 1, 15)), is(2));

        // January 16 2010 = 15 days, 3 cycles
        assertThat(CurrentBillingCycle.calculateCycleNumber(period, start, new DateMidnight(2010, 1, 16)), is(3));
    }

    @Test
    public void testCalculateCycleNumberWeeks() {
        BillingPeriod period = new BillingPeriod(BillingPeriod.Type.WEEK, 1);
        DateMidnight start = new DateMidnight(2010, 1, 1);

        // January 29 2010 = 4 weeks
        assertThat(CurrentBillingCycle.calculateCycleNumber(period, start, new DateMidnight(2010, 1, 29)), is(4));

        // February 4 2010 = 4 weeks (still within the 4th cycle)
        assertThat(CurrentBillingCycle.calculateCycleNumber(period, start, new DateMidnight(2010, 2, 4)), is(4));

        // February 5 2010 = 5 weeks
        assertThat(CurrentBillingCycle.calculateCycleNumber(period, start, new DateMidnight(2010, 2, 5)), is(5));
    }

    @Test
    public void testCalculateCycleNumberMonths() {
        BillingPeriod period = new BillingPeriod(BillingPeriod.Type.MONTH, 1);
        DateMidnight start = new DateMidnight(2010, 1, 1);

        // January 1 2011 = 12 months
        assertThat(CurrentBillingCycle.calculateCycleNumber(period, start, new DateMidnight(2011, 1, 1)), is(12));

        // January 31 2011 = 12 months (still within the 12th cycle)
        assertThat(CurrentBillingCycle.calculateCycleNumber(period, start, new DateMidnight(2011, 1, 31)), is(12));

        // February 1 2011 = 13 months
        assertThat(CurrentBillingCycle.calculateCycleNumber(period, start, new DateMidnight(2011, 2, 1)), is(13));
    }

    @Test
    public void testCalculateCycleNumberYears() {
        BillingPeriod period = new BillingPeriod(BillingPeriod.Type.YEAR, 1);
        DateMidnight start = new DateMidnight(2010, 1, 1);

        // January 1 2011 = 1 year
        assertThat(CurrentBillingCycle.calculateCycleNumber(period, start, new DateMidnight(2011, 1, 1)), is(1));

        // December 31 2011 = 1 year (still within the 1st cycle)
        assertThat(CurrentBillingCycle.calculateCycleNumber(period, start, new DateMidnight(2011, 12, 31)), is(1));

        // January 1 2012 = 2 years
        assertThat(CurrentBillingCycle.calculateCycleNumber(period, start, new DateMidnight(2012, 1, 1)), is(2));
    }

    @Test
    public void testCalculateCycleStartDays() {
        DateMidnight start = new DateMidnight(2010, 1, 1);

        // 25th single day cycle; Jan 1 + 25 days = Jan 26th
        assertThat(
                CurrentBillingCycle.calculateCycleStart(new BillingPeriod(BillingPeriod.Type.DAY, 1), start, 25),
                is(new DateMidnight(2010, 1, 26))
        );

        // 3rd 10 day cycle; Jan 1 + 30 days = Jan 31st
        assertThat(
                CurrentBillingCycle.calculateCycleStart(new BillingPeriod(BillingPeriod.Type.DAY, 10), start, 3),
                is(new DateMidnight(2010, 1, 31))
        );
    }

    @Test
    public void testCalculateCycleStartWeeks() {
        DateMidnight start = new DateMidnight(2010, 1, 1);

        // 3rd 2 week cycle; Jan 1 + 6 weeks = February 12th
        // Friday January 1st 2010 -> Friday February 12th 2010
        assertThat(
                CurrentBillingCycle.calculateCycleStart(new BillingPeriod(BillingPeriod.Type.WEEK, 2), start, 3),
                is(new DateMidnight(2010, 2, 12))
        );

        // 15, 3 week cycle; Jan 1 + 45 weeks = November 12th
        // Friday January 1st 2010 -> Friday November 12th
        assertThat(
                CurrentBillingCycle.calculateCycleStart(new BillingPeriod(BillingPeriod.Type.WEEK, 3), start, 15),
                is(new DateMidnight(2010, 11, 12))
        );
    }

    @Test
    public void testCalculateCycleStartMonths() {
        DateMidnight start = new DateMidnight(2010, 1, 1);

        // 10, 1 month cycle; Jan 1 2010 + 10 months = November 1st 2010
        assertThat(
                CurrentBillingCycle.calculateCycleStart(new BillingPeriod(BillingPeriod.Type.MONTH, 1), start, 10),
                is(new DateMidnight(2010, 11, 1))
        );

        // 3, 6 month cycle; Jan 1 2010 + 18 months = July 1st 2011
        assertThat(
                CurrentBillingCycle.calculateCycleStart(new BillingPeriod(BillingPeriod.Type.MONTH, 6), start, 3),
                is(new DateMidnight(2011, 7, 1))
        );
    }

    @Test
    public void testCalculateCycleStartYears() {
        DateMidnight start = new DateMidnight(2010, 1, 1);

        // 2nd 1 year cycle; Jan 1 2010 + 2 years = Jan 1 2012
        assertThat(
                CurrentBillingCycle.calculateCycleStart(new BillingPeriod(BillingPeriod.Type.YEAR, 1), start, 2),
                is(new DateMidnight(2012, 1, 1))
        );

        // 5th 2 year cycle; Jan 1 2010 + 10 years = Jan 1 2020
        assertThat(
                CurrentBillingCycle.calculateCycleStart(new BillingPeriod(BillingPeriod.Type.YEAR, 2), start, 5),
                is(new DateMidnight(2020, 1, 1))
        );
    }

    @Test
    public void testCalculateCycleEndDays() {
        DateMidnight end = new DateMidnight(2010, 1, 1);

        // 3 day cycle
        assertThat(
                CurrentBillingCycle.calculateCycleEnd(new BillingPeriod(BillingPeriod.Type.DAY, 3), end),
                is(new DateMidnight(2010, 1, 4))
        );

        // 13 day cycle
        assertThat(
                CurrentBillingCycle.calculateCycleEnd(new BillingPeriod(BillingPeriod.Type.DAY, 13), end),
                is(new DateMidnight(2010, 1, 14))
        );
    }

    @Test
    public void testCalculateCycleEndWeeks() {
        DateMidnight end = new DateMidnight(2010, 1, 1);

        // 2 week cycle
        // January 1 + 2 week period = January 15th
        assertThat(
                CurrentBillingCycle.calculateCycleEnd(new BillingPeriod(BillingPeriod.Type.WEEK, 2), end),
                is(new DateMidnight(2010, 1, 15))
        );

        // 6 week cycle
        // January 1 + 6 week period = February 12th
        assertThat(
                CurrentBillingCycle.calculateCycleEnd(new BillingPeriod(BillingPeriod.Type.WEEK, 6), end),
                is(new DateMidnight(2010, 2, 12))
        );
    }

    @Test
    public void testCalculateCycleEndMonths() {
        DateMidnight end = new DateMidnight(2010, 1, 1);

        // 1 month cycle
        assertThat(
                CurrentBillingCycle.calculateCycleEnd(new BillingPeriod(BillingPeriod.Type.MONTH, 1), end),
                is(new DateMidnight(2010, 2, 1))
        );

        // 7 month cycle (fun with odd numbers!)
        assertThat(
                CurrentBillingCycle.calculateCycleEnd(new BillingPeriod(BillingPeriod.Type.MONTH, 7), end),
                is(new DateMidnight(2010, 8, 1))
        );
    }

    @Test
    public void testCalculateCycleEndYears() {
        DateMidnight end = new DateMidnight(2010, 1, 1);

        // 1 year cycle
        assertThat(
                CurrentBillingCycle.calculateCycleEnd(new BillingPeriod(BillingPeriod.Type.YEAR, 1), end),
                is(new DateMidnight(2011, 1, 1))
        );

        // 37 year cycle
        assertThat(
                CurrentBillingCycle.calculateCycleEnd(new BillingPeriod(BillingPeriod.Type.YEAR, 37), end),
                is(new DateMidnight(2047, 1, 1))
        );
    }
}
