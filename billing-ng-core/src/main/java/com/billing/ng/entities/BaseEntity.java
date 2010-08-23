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

import com.billing.ng.xml.XmlNamespacePrefixMapper;
import com.sun.xml.bind.marshaller.NamespacePrefixMapper;
import org.hibernate.validator.ClassValidator;
import org.hibernate.validator.InvalidValue;

import javax.persistence.Transient;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * BaseEntity
 *
 * @author Brian Cowdery
 * @since 24-Apr-2010
 */
@XmlTransient
public abstract class BaseEntity implements Serializable {

    /**
     * colon-separates string of packages that contain JAXB bound objects.
     */
    private static transient String PACKAGES;
    static {
        List<String> packages = Arrays.asList(
                "com.billing.ng.entities"
        );

        StringBuilder builder = new StringBuilder();
        for (Iterator<String> it = packages.iterator(); it.hasNext();) {
            String pkg = it.next();
            builder.append(pkg);
            if (it.hasNext()) builder.append(":");
        }
        PACKAGES = builder.toString();
    }

    /**
     * Executes the Hibernate ClassValidator on this class, validating
     * all annotated fields and returning any invalid values encountered.
     *
     * @return invalid values (validation messages), empty if none
     */
    @Transient
    @SuppressWarnings("unchecked")
    public InvalidValue[] validate() {
        ClassValidator validator = new ClassValidator(this.getClass());
        return validator.getInvalidValues(this);
    }

    /**
     * Marshals this entity to an XML string.
     *
     * @return this entity in a serialized XML form.
     * @throws JAXBException thrown if JAXB context cannot be initialized
     * @throws IOException thrown if serialization fails
     */
    @Transient
    public String toXml() throws JAXBException, IOException {
        JAXBContext context = JAXBContext.newInstance(PACKAGES);
        Marshaller m = context.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        m.setProperty("com.sun.xml.bind.namespacePrefixMapper", new XmlNamespacePrefixMapper());

        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        m.marshal(this, bos);

        bos.flush();
        bos.close();

        return bos.toString();
    }

    /**
     * Un-marshals an entity from a given XML string. 
     *
     * @param destType destination entity type
     * @param xml serialized XML form to read
     * @param <T> Entity type of un-marshaled entity
     * @return un-marshaled entity
     * @throws JAXBException thrown if JAXB context cannot be initialized
     * @throws IOException thrown if serialized XML cannot be read
     */
    @SuppressWarnings("unchecked")
    public static <T> T fromXml(Class<T> destType, String xml) throws JAXBException, IOException {
        JAXBContext context = JAXBContext.newInstance(PACKAGES);
        Unmarshaller u = context.createUnmarshaller();

        ByteArrayInputStream bis = new ByteArrayInputStream(xml.getBytes());
        return (T) u.unmarshal(new StreamSource(bis));
    }
}
