package com.billing.ng.entities;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * XmlMap
 *
 * @author Brian Cowdery
 * @since 28-Apr-2010
 */
@XmlRootElement(name = "map")
public class XmlMap {

    private final List<XmlMapEntry> entries = new ArrayList<XmlMapEntry>();

    @XmlElement
    public List<XmlMapEntry> getEntries() {
        return this.entries;
    }
}
