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

import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.annotations.MapKey;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
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
public class Customer extends User {

    @Column
    private String number;

    @Column
    private String companyName;
    
    @OneToMany(mappedBy = "customer")
    private Set<Account> accounts = new HashSet<Account>();

    @CollectionOfElements
    @MapKey(columns = @Column(name = "attribute_name"))
    @Column(name = "attribute_value")
    private Map<String, String> attributes = new HashMap<String, String>();

    public Customer() {
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    
    public Set<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(Set<Account> accounts) {
        this.accounts = accounts;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;        
    }

    public void addAttribute(String name, String value) {
        this.attributes.put(name, value);
    }
}
