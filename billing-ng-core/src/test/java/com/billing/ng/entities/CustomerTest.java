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
public class CustomerTest {

    @Test
    public void testSetPassword() throws Exception {
        String password = "My password";

        Customer customer = new Customer();
        customer.setPassword(password);

        assertThat(customer.getPasswordHash(), is(not(nullValue())));
        assertThat(customer.getPasswordHash(), matchesPattern(Patterns.URLSAFE_TOKEN));
        assertThat(customer.getPasswordHash(), is(not(password)));

        Customer customer2 = new Customer();
        customer2.setPassword(password);
        
        assertThat(customer2.getPasswordHash(), is(not(customer.getPasswordHash())));
    }

    @Test
    public void testGetHashSalt() throws Exception {
        Customer customer1 = new Customer();
        Customer customer2 = new Customer();

        String salt1 = customer1.getHashSalt();
        String salt2 = customer2.getHashSalt();        

        assertThat(salt1, is(not(salt2)));
        assertThat(salt1, is(customer1.getHashSalt()));
        assertThat(salt2, is(customer2.getHashSalt()));
    }
}
