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

package com.billing.ng.util;

import com.billing.ng.test.Patterns;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.text.pattern.PatternMatcher.*;
import static org.testng.Assert.*;

/**
 * HashUtilsTest
 *
 * @author Brian Cowdery
 * @since 25-Apr-2010
 */
@Test(groups = { "utils", "quick" })
public class HashUtilsTest {
    private static final int UNIQUE_TESTS = 10000;
    private static final String BASE_STRING = "Some generic base string.";

    @Test
    public void testGenerateHash() {
        String hash = HashUtils.generateHash(BASE_STRING);
        assertThat(hash, is(not(nullValue())));
        assertThat(hash, is(not(BASE_STRING)));
        assertThat(hash, matchesPattern(Patterns.URLSAFE_TOKEN));
    }

    @Test
    public void testGenerateHashSalt() {
        String salt = HashUtils.generateHashSalt(10);
        assertThat(salt, is(not(nullValue())));
        assertThat(salt.length(), is(10));
        assertThat(salt, matchesPattern(Patterns.ALPHANUMERIC));
    }

    @Test
    public void testGenerateHashSaltUniqueness() {
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
    public void testGenerateHashSaltLength() {
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
