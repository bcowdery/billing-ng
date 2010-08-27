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

import com.billing.ng.entities.structure.Visitable;
import com.billing.ng.entities.structure.Visitor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

/**
 * Account held by a customer.
 *
 * Purchase orders and invoices are held by accounts, allowing a customer to
 * theoretically hold several separate accounts in the system in a user defined
 * hierarchy.
 *
 * Charges are accrued under the customer's account (root account if a hierarchical structure is used),
 * and can either be billed up to the customer, or to an account-specific contact.
 *
 * @author Brian Cowdery
 * @since 26-Aug-2010
 */
@Entity
public class Account extends BaseEntity implements Visitable<Account> {

    public enum BillingType {
        /** Accrue charges under the customer holding this account. */
        CUSTOMER,
        /** Accrue charges under this account, using the account contact information instead of the customers. */
        ACCOUNT 
    }
    
    @Id @GeneratedValue    
    private Long id;

    @Column
    private String number;

    @ManyToOne   
    private Customer customer;

    @ManyToOne
    private Contact contact;

    @Enumerated(EnumType.STRING)
    @Column
    private BillingType billingType;

    @ManyToOne
    private Account parentAccount;

    @OneToMany(mappedBy = "parentAccount")
    private Set<Account> subAccounts = new HashSet<Account>();

    @Column
    private Integer hierarchyLevel = 0;

    public Account() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    /**
     * Returns the billable contact for this Account. If the billing type for this
     * account is {@link BillingType#ACCOUNT} then this account will generate invoices
     * using this contact.
     *
     * @return account contact
     */
    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    /**
     * Returns the billing type for this account.
     * @return billing type
     */
    public BillingType getBillingType() {
        return billingType;
    }

    public void setBillingType(BillingType billingType) {
        this.billingType = billingType;
    }

    public Account getParentAccount() {
        return parentAccount;
    }

    public void setParentAccount(Account parentAccount) {
        // shift all sub account hierarchy levels +1 when adding a new parent
        if (this.parentAccount == null && parentAccount != null && subAccounts != null)
            accept(new Visitor<Account, Object>() {
                public Object visit(Account account) {
                    account.setHierarchyLevel(account.getHierarchyLevel() + 1);
                    
                    for (Account subAccount : account.subAccounts)
                        subAccount.accept(this);
                    return null;
                }
            });

        this.parentAccount = parentAccount;
    }

    public Set<Account> getSubAccounts() {
        return subAccounts;
    }

    public void setSubAccounts(Set<Account> subAccounts) {
        this.subAccounts = subAccounts;
    }

    public void addSubAccount(Account account) {
        // set sub account hierarchy level to +1 of this current level
        account.setParentAccount(this);
        account.setHierarchyLevel(this.hierarchyLevel + 1);
        this.subAccounts.add(account);
    }

    /**
     * Returns the hierarchy level of this account (the number
     * of levels below the parent account). Parent accounts
     * have a hierarchy level of 0.
     *
     * @return hierarchy level.
     */
    public Integer getHierarchyLevel() {
        return hierarchyLevel;
    }

    public void setHierarchyLevel(Integer hierarchyLevel) {
        this.hierarchyLevel = hierarchyLevel;
    }

    /**
     * Visit this account and return the visitor's result. Visitors can be used
     * to transverse the entire Account hierarchy, aggregating values or
     * applying a change to the entire structure.
     *
     * @param visitor visitor to accept
     * @param <R> visitor return type
     * @return value of type <R> returned from visitor
     */
    public <R> R accept(Visitor<Account, R> visitor) {
        return visitor.visit(this);
    }
}
