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

package com.billing.ng.util;

/**
 * ClassUtils
 *
 * @author Brian Cowdery
 * @since 19-08-2011
 */
public class ClassUtils {

    /**
     * Returns the <code>Class</code> object associated with the class or
     * interface with the given string name.
     *
     * This method attempts to load the class using the current thread context
     * class loader before the local caller class loader in case the target class
     * has been loaded by a different context (e.g, a web-application).
     *
     * @param className class name to load
     * @return class
     * @throws ClassNotFoundException thrown if class is not found
     */
    public static Class forName(String className) throws ClassNotFoundException {
        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            return loader.loadClass(className);

        } catch (ClassNotFoundException e) {
            try {
                return Class.forName(className);
            } catch (ClassNotFoundException ignore) {
                /* ignore and rethrow original */
            }

            throw e;
        }
    }

    /**
     * Converts a method name to a javabeans field name.
     *
     * @param methodName method name
     * @return field name
     */
    public static String getFieldName(String methodName) {
        return methodName.substring(3, 4).toLowerCase() + methodName.substring(4);
    }
}
