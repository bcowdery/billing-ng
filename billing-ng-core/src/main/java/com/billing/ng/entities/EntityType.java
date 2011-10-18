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

package com.billing.ng.entities;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

/**
 * Enum of core entity types. Used by various meta data and utility classes to identify
 * the type of core entity that they operate on / belong to.
 *
 * @author Brian Cowdery
 * @since 18-Oct-2011
 */
@XmlType
@XmlEnum
public enum EntityType {

    /** Purchase Order, {@link PurchaseOrder} */
    ORDER,

    /** Invoice */
    INVOICE,

    /** Billable customer account, {@link Account} */
    ACCOUNT,

    /** Top level customer or organization, {@link Customer} */
    CUSTOMER,

    /** Internal user (staff), {@link Staff} */
    STAFF
}