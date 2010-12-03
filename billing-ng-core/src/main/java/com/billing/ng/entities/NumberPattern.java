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

package com.billing.ng.entities;

import freemarker.template.Configuration;
import freemarker.template.ObjectWrapper;
import freemarker.template.SimpleHash;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlTransient;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

/**
 * Number patterns for user facing entities. Each number pattern represents a
 * way to generate a human readable identifying "number" for a user facing entity,
 * such as order numbers, invoice numbers, and customer numbers.
 *
 * @author Brian Cowdery
 * @since 23-Nov-2010
 */
@Entity
@XmlTransient
public class NumberPattern extends BaseEntity {

    private transient final Logger log = LoggerFactory.getLogger(NumberPattern.class);

    public enum Type { ORDER, INVOICE, ACCOUNT, CUSTOMER, STAFF }

    @Id @Enumerated(EnumType.STRING)
    private Type type;
    @Column @NotNull
    private String pattern;

    public NumberPattern() {        
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    /**
     * Generates a string for the set Freemarker template pattern. This method accepts
     * an object and key name that are used to populate the Freemarker data-model. 
     *
     * @param key key of object name reference in template
     * @param object object for template
     * @return generated string
     */
    public String generate(String key, Object object) {
        if (pattern == null || key == null || object == null)
            return null;

        SimpleHash root = new SimpleHash();
        root.put(key, object);

        Writer out = new StringWriter();

        try {
            Template template = new Template(getTemplateName(), new StringReader(pattern), getConfiguration());
            template.process(root, out);
            out.close();

        } catch (IOException e) {
            log.debug("IOException occurred processing number pattern template.", e);
        } catch (TemplateException e) {
            log.debug("Exception parsing number pattern template.", e);
        }

        return out.toString();
    }
    
    private String getTemplateName() {
        return type != null ? type.name() : "DEFAULT"; 
    }

    private Configuration getConfiguration() {
        Configuration cfg = new Configuration();
        cfg.setObjectWrapper(ObjectWrapper.DEFAULT_WRAPPER);
        return cfg;
    }
}
