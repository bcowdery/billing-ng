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

import com.billing.ng.entities.NumberPatternTest;
import com.billing.ng.entities.validator.exception.ValidationException;
import com.billing.ng.plugin.test.TestPlugin;
import com.billing.ng.plugin.test.TestPluginImpl;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import javax.validation.ConstraintViolation;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.*;

/**
 * PluginFactoryTest
 *
 * @author Brian Cowdery
 * @since 15/02/11
 */
@Test(groups = {"quick", "plugin"})
public class PluginFactoryTest {

    @Test
    public void testGetAllPlugins() throws Exception {
        Set<Class<?>> plugins = PluginFactory.getAllPlugins();
        assertThat(plugins, Matchers.<Class<?>>hasItem(TestPluginImpl.class));
    }

    @Test
    public void testGetPlugins() throws Exception {
        Set<Class<? extends TestPlugin>> plugins = PluginFactory.getPlugins(TestPlugin.class);
        assertThat(plugins, Matchers.<Class<? extends TestPlugin>>hasItem(TestPluginImpl.class));
    }

    @Test
    public void testGetPluginInstance() throws Exception {
        PluginFactory<TestPluginImpl> factory = PluginFactory.createFactory(TestPluginImpl.class);

        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("string", "some string value");

        TestPluginImpl plugin = factory.getInstance(parameters);

        assertNotNull(plugin);
    }

    @Test
    public void testGetPluginInstanceParameterInjection() {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("string", "some string value");
        parameters.put("number", "123");
        parameters.put("decimal", null); // use annotation defaultValue

        PluginFactory<TestPluginImpl> factory = PluginFactory.createFactory(TestPluginImpl.class);
        TestPluginImpl plugin = factory.getInstance(parameters);

        assertNotNull(plugin);

        assertThat(plugin.getString(), is("some string value"));
        assertThat(plugin.getNumber(), is(123));
        assertThat(plugin.getDecimal(), is(new BigDecimal("0.00")));
    }

    @Test(groups = { "validation" })
    @SuppressWarnings("unchecked")
    public void testGetPluginInstanceValidations() {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("string", null);  // @NotNull
        parameters.put("number", "-10"); // @Min(0) @Max(999)

        PluginFactory<TestPluginImpl> factory = PluginFactory.createFactory(TestPluginImpl.class);

        try {
            factory.getInstance(parameters);
            fail("#getInstance() should have thrown a ValidationException!");

        } catch (ValidationException e) {
            Set<ConstraintViolation<TestPluginImpl>> constraintViolations = (Set<ConstraintViolation<TestPluginImpl>>) e.getConstraintViolations();

            assertThat(constraintViolations.size(), is(2));

            assertThat(constraintViolations, hasItem(Matchers.<ConstraintViolation<TestPluginImpl>>hasProperty("message", is("may not be null"))));
            assertThat(constraintViolations, hasItem(Matchers.<ConstraintViolation<TestPluginImpl>>hasProperty("message", is("must be greater than or equal to 0"))));
        }
    }
}
