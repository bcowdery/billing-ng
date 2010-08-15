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

import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MapXMLAdaptor
 *
 * @author Brian Cowdery
 * @since 28-Apr-2010
 */
@XmlTransient
public class XmlMapAdapter extends XmlAdapter<XmlMap, Map<String, String>> {

    @Override
    public XmlMap marshal(Map<String, String> v) throws Exception {
        XmlMap map = new XmlMap();

        List<XmlMapEntry> entries = map.getEntries();
        for (Map.Entry<String, String> entry : v.entrySet())
            entries.add(new XmlMapEntry(entry.getKey(), entry.getValue()));

        return map;
    }

    @Override
    public Map<String, String> unmarshal(XmlMap v) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        for (XmlMapEntry entry : v.getEntries())
            map.put(entry.getKey(), entry.getValue());
        
        return map;
    }
}
