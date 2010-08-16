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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.Currency;

/**
 * Rate represents the conversion rate between the primary system currency
 * and this rates currency where the primary system currency is always rated at 1.
 *
 * @author Brian Cowdery
 * @since 15-Aug-2010
 */
@Entity
public class Rate extends BaseEntity {

    private Long id;
    private BigDecimal rate;
    private String currencyCode;

    public Rate() {
    }

    public Rate(BigDecimal rate, String currencyCode) {
        this.rate = rate;
        this.currencyCode = currencyCode;
    }

    @Id @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "rate", nullable = false, precision = 10, length = 22)
    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    @Column(name = "currency_code", nullable = false, length = 3)
    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    @Transient
    public Currency getCurrency() {
        return Currency.getInstance(currencyCode);
    }
}
