/*
 BillingNG, a next-generation billing solution
 Copyright (C) 2011 Brian Cowdery

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

package com.billing.ng.plugin;

import com.billing.ng.plugin.annotation.Parameter;
import com.billing.ng.plugin.test.TestPlugin;
import com.billing.ng.plugin.test.TestPluginImpl;
import com.billing.ng.util.ClassUtils;
import org.hamcrest.Matchers;
import org.joda.time.DateMidnight;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.*;

/**
 * ParametersTest
 *
 * @author Brian Cowdery
 * @since 19/02/11
 */
@Test(groups = {"quick", "plugin"})
public class ParametersTest {

    @Test
    public void testPopulate() throws Exception {
        TestPluginImpl plugin = new TestPluginImpl();

        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("string", "some string value");
        parameters.put("number", "123");
        parameters.put("decimal", "99.99");

        // inject!
        Parameters<TestPluginImpl> injector = new Parameters<TestPluginImpl>();
        injector.populate(plugin, parameters);

        assertThat(plugin.getString(), is("some string value"));
        assertThat(plugin.getNumber(), is(123));
        assertThat(plugin.getDecimal(), is(new BigDecimal("99.99")));
    }

    @Test
    public void testPopulateDefaultValues() throws Exception {
        TestPluginImpl plugin = new TestPluginImpl();

        // inject with empty parameters
        Parameters<TestPluginImpl> injector = new Parameters<TestPluginImpl>();
        injector.populate(plugin, Collections.<String, String>emptyMap());

        assertThat(plugin.getString(), nullValue());
        assertThat(plugin.getNumber(), nullValue());
        assertThat(plugin.getDecimal(), is(new BigDecimal("0.00")));
    }

    @Test
    public void testPopulateNullValues() throws Exception {
        TestPluginImpl plugin = new TestPluginImpl();

        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("string", null);
        parameters.put("number", null);
        parameters.put("decimal", null);

        // inject with null parameters
        Parameters<TestPluginImpl> injector = new Parameters<TestPluginImpl>();
        injector.populate(plugin, parameters);

        assertThat(plugin.getString(), nullValue());
        assertThat(plugin.getNumber(), nullValue());
        assertThat(plugin.getDecimal(), is(new BigDecimal("0.00")));
    }

    @Test
    public void testExtract() throws Exception {
        TestPluginImpl plugin = new TestPluginImpl();

        plugin.setString("some string value");
        plugin.setNumber(123);
        plugin.setDecimal(null);

        // extract parameter map
        Parameters<TestPluginImpl> extractor = new Parameters<TestPluginImpl>();
        Map<String, String> parameters = extractor.extract(plugin);

        assertThat(parameters.size(), is(3));

        assertThat(parameters.get("string"), is("some string value"));
        assertThat(parameters.get("number"), is("123"));
        assertThat(parameters.get("decimal"), nullValue());
    }

    @Test
    public void testGetAnnotatedFields() throws Exception {
        Parameters<TestPluginImpl> parameters = new Parameters<TestPluginImpl>();
        Map<String, Parameter> fields = parameters.getAnnotatedFields(TestPluginImpl.class);

        assertThat(fields.size(), is(3));

        // found all annotated field names
        Set<String> fieldNames = fields.keySet();
        assertThat(fieldNames, Matchers.<String>hasItem("string"));
        assertThat(fieldNames, Matchers.<String>hasItem("number"));
        assertThat(fieldNames, Matchers.<String>hasItem("decimal"));

        // @Parameter annotations present with names and default values
        assertThat(fields.get("string").name(), is("string"));
        assertThat(fields.get("string").defaultValue(), is(""));

        assertThat(fields.get("number").name(), is("number"));
        assertThat(fields.get("number").defaultValue(), is(""));

        assertThat(fields.get("decimal").name(), is("decimal"));
        assertThat(fields.get("decimal").defaultValue(), is("0.00"));
    }
}
