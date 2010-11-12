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

import org.testng.annotations.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.*;

/**
 * XmlMapTest
 *
 * @author Brian Cowdery
 * @since 2010-11-11
 */
@Test(groups = {"xml", "quick"})
public class XmlMapTest {

    /** Class under test. Entity with java Map as an XML element. */
    @XmlRootElement
    private static class TestEntity extends BaseEntity {

        private Map<String, String> attributes = new HashMap<String, String>();

        @XmlElement
        @XmlJavaTypeAdapter(XmlMapAdapter.class)
        public Map<String, String> getAttributes() {
            return attributes;
        }

        public void setAttributes(Map<String, String> attributes) {
            this.attributes = attributes;
        }

        public void addAttribute(String key, String value) {
            attributes.put(key, value);
        }
    }

    private static JAXBContext getTestEntityContext() throws Exception {
        return JAXBContext.newInstance(TestEntity.class);
    }

    private static final String XML_EMPTY =
            "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"
            + "<entities:testEntity xmlns:entities=\"http://billing-ng.com/entities\">\n"
            + "    <entities:attributes/>\n"
            + "</entities:testEntity>\n";
    
    private static final String XML =
            "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"
            + "<entities:testEntity xmlns:entities=\"http://billing-ng.com/entities\">\n"
            + "    <entities:attributes>\n"
            + "        <entities:entries key=\"b\" value=\"value 2\"/>\n"
            + "        <entities:entries key=\"a\" value=\"value 1\"/>\n"
            + "    </entities:attributes>\n"
            + "</entities:testEntity>\n";

    @Test
    public void testEmptyMapToXML() throws Exception {
        TestEntity entity = new TestEntity();
        assertThat(entity.toXml(getTestEntityContext()), is(XML_EMPTY));
    }

    @Test
    public void testMapToXML() throws Exception {
        TestEntity entity = new TestEntity();
        entity.addAttribute("a", "value 1");
        entity.addAttribute("b", "value 2");

        assertThat(entity.toXml(getTestEntityContext()), is(XML));
    }    
}
