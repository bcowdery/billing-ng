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

import com.billing.ng.plugin.annotation.Plugin;
import org.apache.cxf.service.factory.ReflectionServiceFactoryBean;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.util.Set;

/**
 * PluginFactory
 *
 * @author Brian Cowdery
 * @since 15/02/11
 */
public class PluginFactory {

    private static class ReflectionsHolder {
        private static final Reflections REFLECTIONS = getReflections();

        private static Reflections getReflections() {
            return new Reflections(new ConfigurationBuilder()
                                           .setUrls(ClasspathHelper.getUrlsForPackagePrefix("com.billing.ng"))
                                           .setScanners(new TypeAnnotationsScanner(),
                                                        new SubTypesScanner()));
        }
    }

    /**
     * Returns all classes annotated with the {@link Plugin} annotation.
     * @return all plugin classes
     */
    public Set<Class<?>> getAllPlugins() {
        return ReflectionsHolder.REFLECTIONS.getTypesAnnotatedWith(Plugin.class);
    }

    /**
     * Returns all classes implement the given interface type.
     *
     * @param type interface
     * @return all plugin classes implementing the given interface
     */
    public <T> Set<Class<? extends T>> getPlugins(Class<T> type) {
        return ReflectionsHolder.REFLECTIONS.getSubTypesOf(type);
    }
}
