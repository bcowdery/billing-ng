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
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;

/**
 * Account held by a customer.
 *
 * Purchase orders and invoices are held by accounts, allowing a customer to
 * theoretically hold several separate accounts in the system in a user defined
 * hierarchy.
 *
 * Charges are accrued under the customer's account (root account if a hierarchical structure is used),
 * and can either be billed up to the customer, or to an account-specific or sub-account specific
 * contact.
 *
 * @author Brian Cowdery
 * @since 26-Aug-2010
 */
@Entity
public class Account extends BaseEntity implements Visitable<Account> {

    /**
     * Simple visitor that updates the hierarchy level of all visited accounts. Used
     * during the addition/removal of nodes from the account hierarchy to keep hierarchy
     * levels in sync.
     */
    private static class HierarchyLevelUpdateVisitor implements Visitor<Account, Object> {
        public Object visit(Account account) {
            if (!account.isRootAccount()) {
                account.setHierarchyLevel(account.getParentAccount().getHierarchyLevel() + 1);
            } else {
                account.setHierarchyLevel(ROOT_HIERARCHY_LEVEL);
            }

            for (Account subAccount : account.getSubAccounts())
                subAccount.accept(this);

            return null;
        }
    }

    public static final Integer ROOT_HIERARCHY_LEVEL = 0;

    public enum BillingType {
        /** Accrue charges under the customer holding this account. */
        CUSTOMER,
        /** Accrue charges under this account, using the account contact information instead of the customers. */
        ACCOUNT,
        /** Accrue charges for each sub-account separately, using the sub-accounts contact information.*/
        SUB_ACCOUNT
    }

    @Id @GeneratedValue
    private Long id;

    @Column
    private String number;

    @Column
    private String name;

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
    private List<Account> subAccounts = new ArrayList<Account>();

    @Column
    private Integer hierarchyLevel = ROOT_HIERARCHY_LEVEL;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    /**
     * Sets the parent account to the given Account. Do not use this method to manipulate the
     * structure of a hierarchy. Instead use the {@link #addParent(Account)} and
     * {@link #remove()} methods.
     *
     * This method is provided for Hibernate and other tools that expect valid Javabeans getters and setters. 
     *
     * @param parentAccount parent account to set
     */
    public void setParentAccount(Account parentAccount) {
        this.parentAccount = parentAccount;
    }

    /**
     * Adds the given account as a parent of this sub-account. If this account already
     * has a parent, then the new parent will be inserted below the existing parent
     * adding a new hierarchy level by shifting this sub-account down one level.
     *
     * Adding a new parent to the root of the tree:
     * <code>
     *        a      new root e      e
     *        |                      |
     *   b ---+--- c                 a
     *                               |
     *                          b ---+--- c
     * </code>
     *
     * Adding a new parent to a sub-account:
     * <code>
     *        a      new parent e      a
     *        |                        |
     *   b ---+--- c              e ---+--- c
     *                            |
     *                            b 
     * </code>
     *
     * @param account account to add as a parent
     */
    public void addParent(Account account) {
        if (account != null) {
            // insert a new parent account for "this" sub-account, shifting the sub-account
            // down 1 level in the hierarchy and placing the new parent under the old one
            if (!isRootAccount()) {
                // move the old parent above the new parent
                account.setParentAccount(this.parentAccount);
                account.setHierarchyLevel(this.parentAccount.getHierarchyLevel() + 1);
                this.parentAccount.getSubAccounts().add(account);
                // move the sub-account below the new parent
                this.parentAccount.getSubAccounts().remove(this);
            }

            // set the parent of "this" sub-account
            this.parentAccount = account;
            account.getSubAccounts().add(this);

            // shift sub-account hierarchy levels down by 1 to reflect addition of a new parent
            if (account.hasSubAccounts())
                this.accept(new HierarchyLevelUpdateVisitor());
        }
    }

    /**
     * Removes this account from the hierarchy. If this account is a parent node (has children), then
     * all child accounts will be shifted up one level to become children of the parent.
     *
     * Attempting to remove a root account will result in an <code>UnsupportedOperationException</code>.
     *
     * Removing a parent account:
     * <code>
     *        a      remove parent b      a
     *        |                           |
     *        b                      c ---+--- d
     *        |
     *   c ---+--- d
     * </code>
     *
     * @return account after removal from the hierarchy
     */
    public Account remove() {
        if (isRootAccount())
            throw new UnsupportedOperationException("Cannot remove a root account from the hierarchy.");

        // disconnect account from hierarchy
        Account parent = getParentAccount();
        getParentAccount().getSubAccounts().remove(this);
        setParentAccount(null);
        
        // move child accounts up to parent
        for (Account child : getSubAccounts()) {
            child.setParentAccount(null);
            child.addParent(parent);
        }
        
        getSubAccounts().clear();

        return this;
    }
    
    public List<Account> getSubAccounts() {
        return subAccounts;
    }

    public void setSubAccounts(List<Account> subAccounts) {
        this.subAccounts = subAccounts;
    }

    public void addSubAccount(Account account) {        
        account.setHierarchyLevel(hierarchyLevel + 1);
        account.addParent(this);
    }

    /**
     * Returns the hierarchy level of this account (the number
     * of levels below the parent account). Parent accounts
     * have a hierarchy level of 0.
     *
     * @return hierarchy level
     */
    public Integer getHierarchyLevel() {
        return hierarchyLevel;
    }

    public void setHierarchyLevel(Integer hierarchyLevel) {
        this.hierarchyLevel = hierarchyLevel;
    }

    /**
     * Returns true if this account is the top level (root)
     * parent of an account hierarchy. This method will also
     * return true if this account is the ONLY account in a hierarchy.
     *
     * @return true if account is a top level parent account
     */
    @Transient
    public boolean isRootAccount() {
        return parentAccount == null;
    }

    /**
     * Returns true if this account is a sub-account of another.
     *
     * @return true if account is a sub-account
     */
    @Transient
    public boolean isSubAccount() {
        return parentAccount != null;
    }

    /**
     * Returns true if this account has sub-accounts.
     *
     * @return true if account has sub-accounts
     */
    @Transient
    public boolean hasSubAccounts() {
        return !subAccounts.isEmpty();

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

    @Override public String toString() {                
        List<Long> subAccountIds = new ArrayList<Long>(subAccounts.size());
        for (Account account : subAccounts)
            subAccountIds.add(account.getId());
        
        return "Account{"
               + "id=" + id
               + ", hierarchyLevel=" + hierarchyLevel
               + ", subAccounts=" + subAccountIds
               + ", parentAccount=" + (parentAccount != null ? parentAccount.getId() : null)
               + '}';
    }
}
