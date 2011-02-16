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

import com.billing.ng.plugin.annotation.AnnotatedTestPlugin;
import com.billing.ng.plugin.type.PluginType;
import com.billing.ng.plugin.type.TypedTestPlugin;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
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
        Set<Class<?>> plugins = new PluginFactory().getAllPlugins();
        assertThat(plugins, Matchers.<Class<?>>hasItem(AnnotatedTestPlugin.class));
    }

    @Test
    public void testGetPlugins() throws Exception {
        Set<Class<? extends PluginType>> plugins = new PluginFactory().getPlugins(PluginType.class);
        assertThat(plugins, Matchers.<Class<? extends PluginType>>hasItem(TypedTestPlugin.class));
    }

}
