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

import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * PurchaseOrder
 *
 * @author Brian Cowdery
 * @since 2-Dec-2010
 */
@Entity
@XmlRootElement
public class PurchaseOrder extends BaseEntity implements Numbered, Totaled {

    @Id @GeneratedValue
    private Long id;

    @Column
    private String number;

    @ManyToOne
    @Where(clause = "type = ORDER")
    private NumberPattern numberPattern;

    @ManyToOne
    private Account account;

    @OneToMany(mappedBy = "purchaseOrder")
    private List<PurchaseOrderLine> lines = new ArrayList<PurchaseOrderLine>();

    @Embedded
    private Money total;

    public PurchaseOrder() {
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

    public NumberPattern getNumberPattern() {
        return numberPattern;
    }

    public void setNumberPattern(NumberPattern numberPattern) {
        this.numberPattern = numberPattern;
    }

    @PrePersist
    public void generateNumber() {
        if (getNumber() == null && getNumberPattern() != null)
            setNumber(getNumberPattern().generate("order", this));
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public List<PurchaseOrderLine> getLines() {
        return lines;
    }

    public void setLines(List<PurchaseOrderLine> lines) {
        this.lines = lines;
    }

    public Money getTotal() {
        if (total == null)
            calculateTotal();

        return total;
    }

    public void setTotal(Money total) {
        this.total = total;
    }

    public void calculateTotal() {
        for (PurchaseOrderLine line : getLines())
            total = total.add(line.getTotal());
    }
}
