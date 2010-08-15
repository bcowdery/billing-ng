package com.billing.ng.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * XmlMapElement
 *
 * @author Brian Cowdery
 * @since 28-Apr-2010
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "entry")
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
