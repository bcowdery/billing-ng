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

import org.hibernate.validator.Email;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Contact
 *
 * @author Brian Cowdery
 * @since 16-Aug-2010
 */
@Entity
public class Contact {

    @Id @GeneratedValue
    private Long id;

    @Column
    private String homePhoneNumber;
    @Column
    private String mobilePhoneNumber;
    @Column
    private String workPhoneNumber;
    @Column
    private String faxNumber;
    @Column @Email
    private String email;
    @Column
    private String xmpp;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "streetAddress1", column = @Column(name = "mail_street_address1")),
            @AttributeOverride(name = "streetAddress2", column = @Column(name = "mail_street_address2")),
            @AttributeOverride(name = "streetAddress3", column = @Column(name = "mail_street_address3")),
            @AttributeOverride(name = "city", column = @Column(name = "mail_city")),
            @AttributeOverride(name = "state", column = @Column(name = "mail_state")),
            @AttributeOverride(name = "country", column = @Column(name = "mail_country")),
            @AttributeOverride(name = "postalCode", column = @Column(name = "mail_postal_code"))
    })
    private Address mailingAddress;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "streetAddress1", column = @Column(name = "bill_street_address1")),
            @AttributeOverride(name = "streetAddress2", column = @Column(name = "bill_street_address2")),
            @AttributeOverride(name = "streetAddress3", column = @Column(name = "bill_street_address3")),
            @AttributeOverride(name = "city", column = @Column(name = "bill_city")),
            @AttributeOverride(name = "state", column = @Column(name = "bill_state")),
            @AttributeOverride(name = "country", column = @Column(name = "bill_country")),
            @AttributeOverride(name = "postalCode", column = @Column(name = "bill_postal_code"))
    })
    private Address billingAddress;
    
    @Column
    private boolean useMailingAddress = true;

    public Contact() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHomePhoneNumber() {
        return homePhoneNumber;
    }

    public void setHomePhoneNumber(String homePhoneNumber) {
        this.homePhoneNumber = homePhoneNumber;
    }

    public String getMobilePhoneNumber() {
        return mobilePhoneNumber;
    }

    public void setMobilePhoneNumber(String mobilePhoneNumber) {
        this.mobilePhoneNumber = mobilePhoneNumber;
    }

    public String getWorkPhoneNumber() {
        return workPhoneNumber;
    }

    public void setWorkPhoneNumber(String workPhoneNumber) {
        this.workPhoneNumber = workPhoneNumber;
    }

    public String getFaxNumber() {
        return faxNumber;
    }

    public void setFaxNumber(String faxNumber) {
        this.faxNumber = faxNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getXmpp() {
        return xmpp;
    }

    public void setXmpp(String xmpp) {
        this.xmpp = xmpp;
    }

    public Address getMailingAddress() {
        return mailingAddress;
    }

    public void setMailingAddress(Address mailingAddress) {
        this.mailingAddress = mailingAddress;
    }

    public Address getBillingAddress() {
        return useMailingAddress ? mailingAddress : billingAddress;
    }

    public void setBillingAddress(Address billingAddress) {
        this.billingAddress = billingAddress;
    }

    public boolean isUseMailingAddress() {
        return useMailingAddress;
    }

    public void setUseMailingAddress(boolean useMailingAddress) {
        this.useMailingAddress = useMailingAddress;
    }
}
