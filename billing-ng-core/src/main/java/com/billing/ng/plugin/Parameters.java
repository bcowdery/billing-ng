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
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Parameter annotation helper. Processes a class annotated with the {@link Parameter} annotation
 * to allow the injection and extraction of parameter values.
 *
 * @author Brian Cowdery
 * @since 16/02/11
 */
public class Parameters<T> {

    private transient final Logger log = LoggerFactory.getLogger(Parameters.class);

    /**
     * Populates a plugin instances annotated parameters from the given map
     * of parameter values.
     *
     * Blank or empty parameter string values will not be set.
     *
     * @param plugin plugin instance to populate
     * @param parameters map of parameters
     */
    @SuppressWarnings("unchecked")
    public void populate(T plugin, Map<String, String> parameters) {
        Map<String, Parameter> fields = getAnnotatedFields((Class<T>) plugin.getClass());

        for (Map.Entry<String, Parameter> entry : fields.entrySet()) {
            String fieldName = entry.getKey();
            Parameter parameter = entry.getValue();

            String value = parameters.get(parameter.name());
            if (isBlank(value)) value = parameter.defaultValue();

            if (!isBlank(value)) {
                try {
                    BeanUtils.setProperty(plugin, fieldName, value);

                } catch (InvocationTargetException e) {
                    log.error("Unhandled exception occurred when setting parameter value.", e);
                } catch (IllegalAccessException e) {
                    log.error("Could not set parameter value, setter method is not accessible.", e);
                }
            }
        }
    }

    /**
     * Extracts a map of parameters from the given plugin instance.
     *
     * @param plugin plugin instance with annotated parameters
     * @return map of parameters
     */
    @SuppressWarnings("unchecked")
    public Map<String, String> extract(T plugin) {
        Map<String, String> parameters = new HashMap<String, String>();

        Map<String, Parameter> fields = getAnnotatedFields((Class<T>) plugin.getClass());

        for (Map.Entry<String, Parameter> entry : fields.entrySet()) {
            String fieldName = entry.getKey();
            Parameter parameter = entry.getValue();

            try {
                parameters.put(parameter.name(), BeanUtils.getProperty(plugin, fieldName));

            } catch (InvocationTargetException e) {
                log.error("Unhandled exception occurred when getting parameter value.", e);
            } catch (NoSuchMethodException e) {
                log.error("Could not get parameter value, getter method does not exist.", e);
            } catch (IllegalAccessException e) {
                log.error("Could not get parameter value, getter method is not accessible.", e);
            }
        }

        return parameters;
    }

    /**
     * Returns the names of javabeans property methods annotated with the
     * {@link Parameter} annotation.
     *
     * @param type plugin class with parameter annotations
     * @return map of collected parameter field names and the associated annotation
     */
    public Map<String, Parameter> getAnnotatedFields(Class<T> type) {
        Map<String, Parameter> fields = new HashMap<String, Parameter>();

        // collect method annotations
        for (Method method : type.getMethods()) {
            for (Annotation annotation : method.getDeclaredAnnotations()) {
                if (annotation instanceof Parameter) {
                    if (method.getName().startsWith("get") || method.getName().startsWith("set")) {
                        fields.put(getFieldName(method.getName()), (Parameter) annotation);
                    }
                }
            }
        }

        return fields;
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

    // todo: use Apache commons lang StringUtils.isBlank() instead
    private boolean isBlank(String value) {
        return value == null || value.trim().equals("");
    }
}
