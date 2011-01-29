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

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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

    private Calendar calendar = GregorianCalendar.getInstance();

    /* Convenience method to produce a date instance for the given year/month/day */
    private Date getDate(int year, int month, int day) {
        calendar.clear();
        calendar.set(year, month, day);
        return calendar.getTime();
    }

    @Test
    public void testCalculateCycleNumberDays() {
        BillingPeriod period = new BillingPeriod(BillingPeriod.Type.DAY, 5);
        DateMidnight start = new DateMidnight(2010, 1, 1);

        CurrentBillingCycle current = new CurrentBillingCycle();

        // January 11 2010 = 10 days, 2 cycles
        assertThat(current.calculateCycleNumber(period, start, new DateMidnight(2010, 1, 11)), is(2));

        // January 15 2010 = 14 days, 2 cycles (still within the 2nd cycle)
        assertThat(current.calculateCycleNumber(period, start, new DateMidnight(2010, 1, 15)), is(2));

        // January 16 2010 = 15 days, 3 cycles
        assertThat(current.calculateCycleNumber(period, start, new DateMidnight(2010, 1, 16)), is(3));
    }

    @Test
    public void testCalculateCycleNumberWeeks() {
        BillingPeriod period = new BillingPeriod(BillingPeriod.Type.WEEK, 1);
        DateMidnight start = new DateMidnight(2010, 1, 1);

        CurrentBillingCycle current = new CurrentBillingCycle();

        // January 29 2010 = 4 weeks
        assertThat(current.calculateCycleNumber(period, start, new DateMidnight(2010, 1, 29)), is(4));

        // February 4 2010 = 4 weeks (still within the 4th cycle)
        assertThat(current.calculateCycleNumber(period, start, new DateMidnight(2010, 2, 4)), is(4));

        // February 5 2010 = 5 weeks
        assertThat(current.calculateCycleNumber(period, start, new DateMidnight(2010, 2, 5)), is(5));
    }

    @Test
    public void testCalculateCycleNumberMonths() {
        BillingPeriod period = new BillingPeriod(BillingPeriod.Type.MONTH, 1);
        DateMidnight start = new DateMidnight(2010, 1, 1);

        CurrentBillingCycle current = new CurrentBillingCycle();

        // January 1 2011 = 12 months
        assertThat(current.calculateCycleNumber(period, start, new DateMidnight(2011, 1, 1)), is(12));

        // January 31 2011 = 12 months (still within the 12th cycle)
        assertThat(current.calculateCycleNumber(period, start, new DateMidnight(2011, 1, 31)), is(12));

        // February 1 2011 = 13 months
        assertThat(current.calculateCycleNumber(period, start, new DateMidnight(2011, 2, 1)), is(13));
    }

    @Test
    public void testCalculateCycleNumberYears() {
        BillingPeriod period = new BillingPeriod(BillingPeriod.Type.YEAR, 1);
        DateMidnight start = new DateMidnight(2010, 1, 1);

        CurrentBillingCycle current = new CurrentBillingCycle();

        // January 1 2011 = 1 year
        assertThat(current.calculateCycleNumber(period, start, new DateMidnight(2011, 1, 1)), is(1));

        // December 31 2011 = 1 year (still within the 1st cycle)
        assertThat(current.calculateCycleNumber(period, start, new DateMidnight(2011, 12, 31)), is(1));

        // January 1 2012 = 2 years
        assertThat(current.calculateCycleNumber(period, start, new DateMidnight(2012, 1, 1)), is(2));
    }

}
