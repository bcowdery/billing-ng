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

import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;
import java.util.ArrayList;
import java.util.List;

/**
 * CustomerType
 *
 * @author Brian Cowdery
 * @since 19-10-2011
 */
@Entity
@Table
public class CustomerType extends BaseEntity {

    public enum IsolationLevel {
        /** Customers of this type are isolated from everybody and cannot view anyone else's data but their own. */
        ALL,
        /** Customers of this type can only see other customers of the same type. */
        SAME_TYPE,
        /** Customers of this type can only see other customers in the visible type list. */
        SELECTED_TYPES
    }

    @Id @GeneratedValue
    private Long id;

    @Column
    private String name;

    @Column @Enumerated(EnumType.STRING)
    private IsolationLevel isolation = IsolationLevel.ALL;

    @ManyToMany
    @JoinTable(name = "customer_type_visibility",
        joinColumns = @JoinColumn(name = "customer_type", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "visible_customer_type", referencedColumnName = "id")
    )
    private List<CustomerType> visibleTypes = new ArrayList<CustomerType>();

    public CustomerType() {
    }

    @XmlTransient
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @XmlValue
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlTransient
    public IsolationLevel getIsolation() {
        return isolation;
    }

    public void setIsolation(IsolationLevel isolation) {
        this.isolation = isolation;
    }

    @XmlTransient
    public List<CustomerType> getVisibleTypes() {
        return visibleTypes;
    }

    public void setVisibleTypes(List<CustomerType> visibleTypes) {
        this.visibleTypes = visibleTypes;
    }

    @Override
    public String toString() {
        return "CustomerType{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", isolation=" + isolation +
               ", visibleTypes=" + visibleTypes +
               '}';
    }
}
