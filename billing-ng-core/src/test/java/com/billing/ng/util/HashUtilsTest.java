package com.billing.ng.util;

import com.billing.ng.test.Patterns;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.text.pattern.PatternMatcher.*;

/**
 * HashUtilsTest
 *
 * @author Brian Cowdery
 * @since 25-Apr-2010
 */
public class HashUtilsTest {
    private static final int UNIQUE_TESTS = 10000;
    private static final String BASE_STRING = "Some generic base string.";

    @Test
    public void testGenerateHash() throws Exception {
        String hash = HashUtils.generateHash(BASE_STRING);
        assertThat(hash, is(not(nullValue())));
        assertThat(hash, is(not(BASE_STRING)));
        assertThat(hash, matchesPattern(Patterns.URLSAFE_TOKEN));
    }

    @Test
    public void testGenerateHashSalt() throws Exception {
        String salt = HashUtils.generateHashSalt(10);
        assertThat(salt, is(not(nullValue())));
        assertThat(salt.length(), is(10));
        assertThat(salt, matchesPattern(Patterns.ALPHANUMERIC));
    }

    @Test
    public void testGenerateHashSaltUniqueness() throws Exception {
        List<String> salts = new ArrayList<String>(UNIQUE_TESTS);
       
        for (int i = 0; i < UNIQUE_TESTS; i++) {
            String salt = HashUtils.generateHashSalt(10); // salts should be unique, even in large batches
            assertThat(salt, is(not(nullValue())));
            assertThat(salt, is(not(BASE_STRING)));
            assertThat(salts, not(hasItem(salt)));
            
            salts.add(salt);
        }

        assertEquals(UNIQUE_TESTS, salts.size());
    }

    @Test
    public void testGenerateHashSaltLength() throws Exception {
        List<String> salts = new ArrayList<String>(UNIQUE_TESTS);

        Random generator = new Random();
        for (int i = 0; i < UNIQUE_TESTS; i++) {
            int length = generator.nextInt(9990) + 10; // random length of salt, 10-1000 characters

            String salt = HashUtils.generateHashSalt(length);
            assertThat(salt, is(not(nullValue())));
            assertThat(salt.length(), is(length));
            assertThat(salt, is(not(BASE_STRING)));
            assertThat(salts, not(hasItem(salt)));

            salts.add(salt);
        }
    }
}
