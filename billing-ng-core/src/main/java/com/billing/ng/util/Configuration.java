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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.regex.Pattern;

/**
 * Configuration
 *
 * @author Brian Cowdery
 * @since 16-Aug-2010
 */
public enum Configuration {

    /** Singleton Configuration instance. */
    INSTANCE;
    public static Configuration getInstance() {
        return INSTANCE;
    }

    private static final Properties DEFAULTS;
    static {
        DEFAULTS = new Properties();
    }

    private static final String BLANK_VALUE = "";
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy HH:mm");
    private static final Pattern BOOLEAN_PATTERN = Pattern.compile("^(yes|on|true)$", Pattern.CASE_INSENSITIVE);

    private final Logger log = LoggerFactory.getLogger(Configuration.class);

    private String path = "./billing-ng.properties";
    private String header = "Billing NG configuration properties";
    private Properties properties;

    Configuration() {
        load();
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public Properties getProperties() {
        return properties;
    }

    /**
     * Returns the property for the given key. If the value does not exist in the
     * external properties file, a default value will be returned, or null if no default
     * is defined.
     *
     * @param key property key
     * @return property value, null if property and default not found.
     */
    public String getProperty(String key) {
        String value = properties.getProperty(key);
        return BLANK_VALUE.equals(value) ? null : value;
    }

    /**
     * Returns the property for the given key as an Integer. If the value does not
     * exist in the external properties file, a default value will be returned, or
     * null if no default is defined.
     *
     * This method will return null if the property value cannot be parsed as an Integer.
     *
     * @param key property key
     * @return property value, null if property and default not found.
     */
    public Integer getPropertyAsInteger(String key) {
        String value = getProperty(key);
        try {            
            return (value != null ? Integer.valueOf(value) : null);
        } catch (NumberFormatException e) {
            log.debug("Property '{}' could not be parsed as an Integer.", value);
            return null;
        }        
    }

    /**
     * Returns the property for the given key as a BigDecimal. If the value does not
     * exist in the external properties file, a default value will be returned, or
     * null if no default is defined.
     *
     * This method will return null if the property value cannot be parsed as a BigDecimal.
     *
     * @param key property key
     * @return property value, null if property and default not found.
     */
    public BigDecimal getPropertyAsDecimal(String key) {
        String value = getProperty(key);
        try {
            return (value != null ? new BigDecimal(value) : null);
        } catch (NumberFormatException e) {
            log.debug("Property '{}' could not be parsed as a BigDecimal.", value);
            return null;
        }
    }

    /**
     * Returns the property for the given key as a Date. If the value does not
     * exist in the external properties file, a default value will be returned, or
     * null if no default is defined.
     *
     * This method will return null if the property value cannot be parsed as a Date.
     *
     * Date values are parsed using the format "dd-MM-yyyy HH:mm".
     *
     * @param key property key
     * @return property value, null if property and default not found.
     */
    public Date getPropertyAsDate(String key) {
        String value = getProperty(key);
        try {
            return (value != null ? DATE_FORMAT.parse(value) : null);
        } catch (ParseException e) {
            log.debug("Property '{}' could not be parsed as a Date.", value);
            return null;
        }
    }

    /**
     * Returns the property for the given key as a Date. If the value does not
     * exist in the external properties file, a default value will be returned, or
     * null if no default is defined.
     * 
     * Property values matching "on", "yes", or "true" (case insensitive) are parsed
     * as Boolean.TRUE, all other values are parsed as Boolean.FALSE.
     *
     * @param key property key
     * @return property value, null if property and default not found.
     */
    public Boolean getPropertyAsBoolean(String key) {
        String value = getProperty(key);
        return (value != null ? BOOLEAN_PATTERN.matcher(value.trim()).find() : null);
    }

    /**
     * Sets the property to the given String value.
     *
     * @param key property key
     * @param value property value to set
     */
    public void setProperty(String key, String value) {
        properties.setProperty(key, (value == null ? BLANK_VALUE : value));
    }

    /**
     * Sets the property to the given Integer value.
     *
     * @param key property key
     * @param value property value to set.
     */
    public void setProperty(String key, Integer value) {
        setProperty(key, (value != null ? value.toString() : null));
    }

    /**
     * Sets the property to the given BigDecimal value.
     *
     * @param key property key
     * @param value property value to set.
     */
    public void setProperty(String key, BigDecimal value) {
        setProperty(key, (value != null ? value.toString() : null));
    }

    /**
     * Sets the property to the given Date value. Date values are written to
     * the external properties file in the format "dd-MM-yyyy HH:mm".
     *
     * @param key property key
     * @param value property value to set.
     */
    public void setProperty(String key, Date value) {
        setProperty(key, (value != null ? DATE_FORMAT.format(value) : null));
    }

    /**
     * Sets the property to the given Boolean value.
     *
     * @param key property key
     * @param value property value to set.
     */
    public void setProperty(String key, Boolean value) {
        setProperty(key, (value != null ? value.toString() : null));
    }

    /**
     * Load configuration properties from the external properties file.
     */
    public void load() {
        properties = new Properties(DEFAULTS);
        try {
            properties.load(new FileInputStream(getPath()));
            log.debug("Read {} properties from file {}", properties.size(), getPath());
        } catch (IOException e) {
            log.error("Properties file '{}' could not be read. Using default configuration.", getPath());
        }
    }

    /**
     * Write configuration properties to the external properties file.
     */
    public void flush() {
        try {
            properties.store(new FileOutputStream(getPath()), getHeader());
            log.debug("Wrote {} properties to file {}", properties.size(), getPath());
        } catch (IOException e) {
            log.error("Properties file '{}' could not be written.", getPath());
        }
    }
}
