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

import org.hibernate.annotations.Where;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Customer
 *
 * http://velocity.apache.org/engine/releases/velocity-1.6.4/user-guide.html
 *
 * @author Brian Cowdery
 * @since 16-Aug-2010
 */
@Entity
@XmlRootElement
public class Customer extends User implements Numbered {

    @Column
    private String number;

    @ManyToOne
    @Where(clause = "type = CUSTOMER")
    private NumberPattern numberPattern;
    
    @Column
    private String companyName;
    
    @OneToOne
    private Contact contact;

    @OneToMany(mappedBy = "customer")
    private Set<Account> accounts = new HashSet<Account>();

    @ElementCollection
    @CollectionTable(name = "customer_attribute")
    @MapKeyColumn(name = "attribute_key")
    @Column(name = "attribute_value")
    private Map<String, String> attributes = new HashMap<String, String>();

    public Customer() {
    }

    @XmlAttribute
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @XmlTransient
    public NumberPattern getNumberPattern() {
        return numberPattern;
    }

    public void setNumberPattern(NumberPattern numberPattern) {
        this.numberPattern = numberPattern;
    }

    @PrePersist
    public void generateNumber() {
        if (getNumber() == null && getNumberPattern() != null)
            setNumber(getNumberPattern().generate("customer", this));
    }

    @XmlAttribute
    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    @XmlElement
    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    @XmlElement
    @XmlElementWrapper(name = "accounts")
    public Set<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(Set<Account> accounts) {
        this.accounts = accounts;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    @XmlJavaTypeAdapter(XmlMapAdapter.class)
    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;        
    }

    public void addAttribute(String name, String value) {
        this.attributes.put(name, value);
    }
}
