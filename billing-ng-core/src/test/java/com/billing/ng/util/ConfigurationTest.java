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

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.text.pattern.PatternMatcher.*;

/**
 * ConfigurationTest
 *
 * @author Brian Cowdery
 * @since 16-Aug-2010
 */
public class ConfigurationTest {
    
    // class under test
    private static Configuration configuration = Configuration.getInstance();

    @Before
    public void loadTestProperties() {
        File fromRoot = new File("billing-ng-core/src/test/resources/test-billing-ng.properties");
        File fromCore = new File("src/test/resources/test-billing-ng.properties");

        configuration.setPath(fromRoot.exists() ? fromRoot.getAbsolutePath() : fromCore.getAbsolutePath());
        configuration.load();
    }

    @Test
    public void testGetProperty() {
        assertThat(configuration.getProperty("test.property.string"), is("string value"));
        assertThat(configuration.getProperty("test.property.integer"), is("123"));
        assertThat(configuration.getProperty("test.property.decimal"), is("99.99"));
    }

    @Test
    public void testGetPropertyAsInteger() {
        assertThat(configuration.getPropertyAsInteger("test.property.integer"), is(123));
        assertThat(configuration.getPropertyAsInteger("test.property.string"), is(nullValue()));
    }

    @Test
    public void testGetPropertyAsDecimal() {
        assertThat(configuration.getPropertyAsDecimal("test.property.decimal"), is(new BigDecimal("99.99")));
        assertThat(configuration.getPropertyAsDecimal("test.property.string"), is(nullValue()));
    }

    @Test
    public void testGetPropertyAsDate() {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.clear();
        calendar.set(2010, Calendar.AUGUST, 17, 22, 30);

        assertThat(configuration.getPropertyAsDate("test.property.date"), is(calendar.getTime()));
        assertThat(configuration.getPropertyAsDate("test.property.string"), is(nullValue()));
    }

    @Test
    public void testGetPropertyAsBoolean() {
        assertTrue(configuration.getPropertyAsBoolean("test.property.boolean.on"));
        assertTrue(configuration.getPropertyAsBoolean("test.property.boolean.yes"));
        assertTrue(configuration.getPropertyAsBoolean("test.property.boolean.true"));
        assertFalse(configuration.getPropertyAsBoolean("test.property.string"));
    }

    @Test
    public void testSetProperty() {
        configuration.setProperty("test.property.setter", "set property string");
        assertThat(configuration.getProperty("test.property.setter"), is("set property string"));
    }

    // todo: test all "setProperty" types.

    @Test
    public void testFlush() {
        Date flushdate = new Date();
        configuration.setProperty("test.property.flush", "flushed property on " + flushdate);
        configuration.flush();
        configuration.load();
        assertThat(configuration.getProperty("test.property.flush"), is("flushed property on " + flushdate));
    }
}
