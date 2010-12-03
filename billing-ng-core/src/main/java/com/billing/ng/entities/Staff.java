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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;

/**
 * Staff
 *
 * @author Brian Cowdery
 * @since 16-Aug-2010
 */
@Entity
public class Staff extends User implements Numbered {
    
    @Column
    private String number;

    @ManyToOne
    @Where(clause = "type = STAFF")
    private NumberPattern numberPattern;

    public Staff() {
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public NumberPattern getNumberPattern() {
        return numberPattern;
    }

    public void setNumberPattern(NumberPattern numberPattern) {
        this.numberPattern = numberPattern;
    }

    @PrePersist
    public void generateNumber() {
        if (getNumber() == null && getNumberPattern() != null)
            setNumber(getNumberPattern().generate("staff", this));
    }
}
