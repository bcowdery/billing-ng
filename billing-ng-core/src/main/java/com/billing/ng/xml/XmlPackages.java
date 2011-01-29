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

package com.billing.ng.xml;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Provides a list of packages containing JAXB bound objects and a usable <code>JAXBContext</code>
 * for marshaling and un-marshaling objects.
 *
 * @author Brian Cowdery
 * @since 28/01/11
 */
public class XmlPackages {

    /** List of packages containing JAXB bound objects */
    public static final List<String> PACKAGES = Arrays.asList(
            "com.billing.ng.entities"
    );

    /** <code>JAXBContext</code> for the package list */
    public static final JAXBContext JAXB_CONTEXT = getJaxbContext();

    /**
     * Build and return a new instance of <code>JAXBContext</code> for the list of packages.
     *
     * @return JAXB context for package list
     */
    private static JAXBContext getJaxbContext() {
        // convert package list to a colon separated string
        StringBuilder builder = new StringBuilder();
        for (Iterator<String> it = PACKAGES.iterator(); it.hasNext();) {
            builder.append(it.next());
            if (it.hasNext()) builder.append(":");
        }

        // build jaxb context
        try {
            return JAXBContext.newInstance(builder.toString());
        } catch (JAXBException e) {
            throw new RuntimeException("Cannot build JAXB context for packages [" + builder.toString() + "]", e);
        }
    }
}
