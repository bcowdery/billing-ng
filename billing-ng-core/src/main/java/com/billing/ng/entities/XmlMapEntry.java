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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * XmlMapElement
 *
 * @author Brian Cowdery
 * @since 28-Apr-2010
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "entry")
@XmlType(name = "entry")
public class XmlMapEntry {

    @XmlAttribute
    private final String key;
    @XmlAttribute
    private final String value;

    public XmlMapEntry() {
        this.key = null;
        this.value = null;                
    }

    public XmlMapEntry(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }
    
    public String getValue() {
        return value;
    }
}
