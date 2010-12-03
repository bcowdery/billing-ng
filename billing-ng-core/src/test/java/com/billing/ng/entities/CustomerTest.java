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
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

/**
 * CustomerTest
 *
 * @author Brian Cowdery
 * @since 24-Nov-2010
 */
@Test(groups = {"entity", "quick"})
public class CustomerTest {

    @Test
    public void testGenerateCustomerNumber() {
        NumberPattern pattern = new NumberPattern();
        pattern.setPattern("No. ${customer.id}");

        Customer customer = new Customer();
        customer.setId(1L);
        customer.setNumberPattern(pattern);

        customer.generateNumber();

        // generated customer number
        assertThat(customer.getNumber(), is("No. 1"));        
    }

    @Test
    public void testGenerateCustomerNumberNullPattern() {
        Customer customer = new Customer();
        customer.setNumberPattern(null);

        customer.generateNumber();

        // no pattern, customer number should remain null
        assertThat(customer.getNumber(), nullValue());
    }

    @Test
    public void testGenerateExistingCustomerNumber() {
        NumberPattern pattern = new NumberPattern();
        pattern.setPattern("No. ${customer.id}");

        Customer customer = new Customer();
        customer.setNumber("Some ID");
        customer.setNumberPattern(pattern);

        customer.generateNumber();

        // customer number was already set and shouldn't change
        assertThat(customer.getNumber(), is("Some ID"));
    }
}
