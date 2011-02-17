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

import com.billing.ng.entities.validator.exception.ValidationException;
import com.billing.ng.plugin.annotation.Plugin;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Transient;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import java.util.Map;
import java.util.Set;

/**
 * PluginFactory
 *
 * @author Brian Cowdery
 * @since 15/02/11
 */
public class PluginFactory<T> {

    private static final ValidatorFactory VALIDATOR_FACTORY = Validation.buildDefaultValidatorFactory();

    /** Holder for the Reflections instance. The Reflections store is not instantiated until the field is accessed. */
    private static class ReflectionsHolder {
        private static final Reflections REFLECTIONS = new Reflections("com.billing.ng");
    }

    private transient final Logger log = LoggerFactory.getLogger(PluginFactory.class);

    private Class<T> type;

    public PluginFactory(Class<T> type) {
        this.type = type;
    }

    /**
     * Creates a new PluginFactory of the given type. This static factory method avoids the
     * annoying need to specify the same type twice when using the public constructor
     * (e.g., <code>new PluginFactory<Type>(Type.class)</code>).
     *
     * @param type type of plugin
     * @param <T> type T
     * @return new plugin factory of type T
     */
    public static <T> PluginFactory<T> createFactory(Class<T> type) {
        return new PluginFactory<T>(type);
    }

    /**
     * Produces a new instance of the plugin class populated with the given parameters.
     *
     * @param parameters map of parameters
     * @return new plugin instance
     */
    public T getInstance(Map<String, String> parameters) {
        T instance = null;
        try {
            instance = type.newInstance();
        } catch (InstantiationException e) {
            log.error("Could not produce an instance of '" + type + "', class must have a default constructor.", e);
        } catch (IllegalAccessException e) {
            log.error("Could not produce an instance of '" + type + "', default constructor is not accessible.", e);
        }

        if (instance != null) {
            // set parameters
            Parameters<T> injector = new Parameters<T>();
            injector.populate(instance, parameters);

            // run validations (like @NotNull for required parameters)
            validate(instance);
        }

        return instance;
    }

    /**
     * Validates all annotated fields and throws a {@link com.billing.ng.entities.validator.exception.ValidationException}
     * if any constraint violations are encountered.
     *
     * @param plugin plugin instance to validate
     * @throws com.billing.ng.entities.validator.exception.ValidationException thrown if constraint violations found
     */
    public void validate(T plugin) throws ValidationException {
        Set<? extends ConstraintViolation<?>> constraintViolations = VALIDATOR_FACTORY.getValidator().validate(plugin);

        if (!constraintViolations.isEmpty())
            throw new ValidationException(constraintViolations);
    }

    /**
     * Returns all classes annotated with the {@link Plugin} annotation.
     * @return all plugin classes
     */
    public static Set<Class<?>> getAllPlugins() {
        return ReflectionsHolder.REFLECTIONS.getTypesAnnotatedWith(Plugin.class);
    }

    /**
     * Returns all classes implement the given interface type.
     *
     * @param type interface
     * @return all plugin classes implementing the given interface
     */
    public static <T> Set<Class<? extends T>> getPlugins(Class<T> type) {
        return ReflectionsHolder.REFLECTIONS.getSubTypesOf(type);
    }
}
