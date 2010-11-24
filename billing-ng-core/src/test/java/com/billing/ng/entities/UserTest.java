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

import com.billing.ng.test.Patterns;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.text.pattern.PatternMatcher.*;

/**
 * CustomerTest
 *
 * @author Brian Cowdery
 * @since 25-Apr-2010
 */
@Test(groups = {"entity", "quick"})
public class UserTest {

    /**
     * Class under test
     */
    private class TestUser extends User {
    }

    @Test
    public void testSetPassword() {
        String password = "My password";

        User user = new TestUser();
        user.setPassword(password);

        assertThat(user.getPasswordHash(), is(not(nullValue())));
        assertThat(user.getPasswordHash(), matchesPattern(Patterns.URLSAFE_TOKEN));
        assertThat(user.getPasswordHash(), is(not(password)));

        User customer2 = new TestUser();
        customer2.setPassword(password);
        
        assertThat(customer2.getPasswordHash(), is(not(user.getPasswordHash())));
    }

    @Test
    public void testGetHashSalt() {
        User customer1 = new TestUser();
        User customer2 = new TestUser();

        String salt1 = customer1.getHashSalt();
        String salt2 = customer2.getHashSalt();        

        assertThat(salt1, is(not(salt2)));
        assertThat(salt1, is(customer1.getHashSalt()));
        assertThat(salt2, is(customer2.getHashSalt()));
    }
}
