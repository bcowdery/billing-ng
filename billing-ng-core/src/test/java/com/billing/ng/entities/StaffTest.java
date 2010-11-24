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
    public void testGenerateStaffId() {
        NumberPattern pattern = new NumberPattern();
        pattern.setPattern("No. ${staff.id}");

        Staff staff = new Staff();
        staff.setId(1L);
        staff.setNumberPattern(pattern);

        staff.generateStaffId();

        // generated staff id
        assertThat(staff.getStaffId(), is("No. 1"));        
    }

    @Test
    public void testGenerateStaffIdNullPattern() {
        Staff staff = new Staff();
        staff.setNumberPattern(null);

        staff.generateStaffId();

        // no pattern, staff id should remain null
        assertThat(staff.getStaffId(), nullValue());
    }

    @Test
    public void testGenerateExistingStaffId() {
        NumberPattern pattern = new NumberPattern();
        pattern.setPattern("No. ${staff.id}");

        Staff staff = new Staff();
        staff.setStaffId("Some ID");
        staff.setNumberPattern(pattern);

        staff.generateStaffId();

        // staff id was already set and shouldn't change
        assertThat(staff.getStaffId(), is("Some ID"));
    }
}
