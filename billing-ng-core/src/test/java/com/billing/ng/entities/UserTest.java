package com.billing.ng.entities;

import com.billing.ng.test.Patterns;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.text.pattern.PatternMatcher.*;

/**
 * CustomerTest
 *
 * @author Brian Cowdery
 * @since 25-Apr-2010
 */
public class UserTest {

    @Test
    public void testSetPassword() throws Exception {
        String password = "My password";

        User user = new User();
        user.setPassword(password);

        assertThat(user.getPasswordHash(), is(not(nullValue())));
        assertThat(user.getPasswordHash(), matchesPattern(Patterns.URLSAFE_TOKEN));
        assertThat(user.getPasswordHash(), is(not(password)));

        User customer2 = new User();
        customer2.setPassword(password);
        
        assertThat(customer2.getPasswordHash(), is(not(user.getPasswordHash())));
    }

    @Test
    public void testGetHashSalt() throws Exception {
        User customer1 = new User();
        User customer2 = new User();

        String salt1 = customer1.getHashSalt();
        String salt2 = customer2.getHashSalt();        

        assertThat(salt1, is(not(salt2)));
        assertThat(salt1, is(customer1.getHashSalt()));
        assertThat(salt2, is(customer2.getHashSalt()));
    }
}
