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
