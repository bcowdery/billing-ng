/*
 BillingNG, a next-generation billing solution
 Copyright (C) 2010 Brian Cowdery

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

import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * BillingPeriodTest
 *
 * @author Brian Cowdery
 * @since 27-Oct-2010
 */
@Test(groups = {"entity", "quick", "billing"})
public class BillingPeriodTest {

    @Test
    public void testGetPeriodOfTime() throws Exception {
        BillingPeriod period = new BillingPeriod();
        period.setInterval(5);

        period.setType(BillingPeriod.Type.DAY);
        assertThat(period.getPeriodOfTime().getValues(), is(new int[] {0, 0, 0, 5, 0, 0, 0, 0})); // 5 days

        period.setType(BillingPeriod.Type.WEEK);
        assertThat(period.getPeriodOfTime().getValues(), is(new int[] {0, 0, 5, 0, 0, 0, 0, 0})); // 5 weeks

        period.setType(BillingPeriod.Type.MONTH);
        assertThat(period.getPeriodOfTime().getValues(), is(new int[] {0, 5, 0, 0, 0, 0, 0, 0})); // 5 months

        period.setType(BillingPeriod.Type.YEAR);
        assertThat(period.getPeriodOfTime().getValues(), is(new int[] {5, 0, 0, 0, 0, 0, 0, 0})); // 5 years
    }

    @Test
    public void testGetDefaultPeriodOfTime() throws Exception {
        BillingPeriod period = new BillingPeriod();
        assertThat(period.getPeriodOfTime().getValues(), is(new int[] {0, 1, 0, 0, 0, 0, 0, 0})); // default 1 month
    }

    @Test
    public void testToString() throws Exception {
        BillingPeriod period = new BillingPeriod();

        // days
        period.setInterval(1);
        period.setType(BillingPeriod.Type.DAY);
        assertThat(period.toString(), is("1 day"));

        period.setInterval(2);
        assertThat(period.toString(), is("2 days"));

        // weeks
        period.setInterval(1);
        period.setType(BillingPeriod.Type.WEEK);
        assertThat(period.toString(), is("1 week"));

        period.setInterval(3);
        assertThat(period.toString(), is("3 weeks"));

        // months
        period.setInterval(1);
        period.setType(BillingPeriod.Type.MONTH);
        assertThat(period.toString(), is("1 month"));

        period.setInterval(4);
        assertThat(period.toString(), is("4 months"));

        // years
        period.setInterval(1);
        period.setType(BillingPeriod.Type.YEAR);
        assertThat(period.toString(), is("1 year"));
                
        period.setInterval(5);
        assertThat(period.toString(), is("5 years"));
    }
}
