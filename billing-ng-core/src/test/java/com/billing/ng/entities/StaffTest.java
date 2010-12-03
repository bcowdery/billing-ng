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
 * StaffTest
 *
 * @author Brian Cowdery
 * @since 23-Nov-2010
 */
@Test(groups = {"entity", "quick"})
public class StaffTest {

    @Test
    public void testGenerateNumber() {
        NumberPattern pattern = new NumberPattern();
        pattern.setPattern("No. ${staff.id}");

        Staff staff = new Staff();
        staff.setId(1L);
        staff.setNumberPattern(pattern);

        staff.generateNumber();

        // generated staff number
        assertThat(staff.getNumber(), is("No. 1"));        
    }

    @Test
    public void testGenerateNumberNullPattern() {
        Staff staff = new Staff();
        staff.setNumberPattern(null);

        staff.generateNumber();

        // no pattern, staff number should remain null
        assertThat(staff.getNumber(), nullValue());
    }

    @Test
    public void testGenerateExistingNumber() {
        NumberPattern pattern = new NumberPattern();
        pattern.setPattern("No. ${staff.id}");

        Staff staff = new Staff();
        staff.setNumber("Some ID");
        staff.setNumberPattern(pattern);

        staff.generateNumber();

        // staff number was already set and shouldn't change
        assertThat(staff.getNumber(), is("Some ID"));
    }
}
