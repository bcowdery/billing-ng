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

import org.joda.time.DateMidnight;
import org.joda.time.Period;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * BillingCycle
 *
 * @author Brian Cowdery
 * @since 26-Oct-2010
 */
@Entity
public class BillingCycle extends BaseEntity {

    public static final int LAST_DAY_OF_MONTH = 31;

    @GeneratedValue @Id
    private Long id;

    @Column @Temporal(TemporalType.DATE)
    private Date start = new Date();
    @Column @Temporal(TemporalType.DATE)
    private Date end;
    @ManyToOne
    private BillingPeriod billingPeriod;
    @Column
    private Integer cycleStartDay;

    public BillingCycle() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public BillingPeriod getBillingPeriod() {
        return billingPeriod;
    }

    public void setBillingPeriod(BillingPeriod billingPeriod) {
        this.billingPeriod = billingPeriod;
    }

    /**
     * Day of month that this customers cycle starts.
     *
     * @return cycle start day of month
     */
    public Integer getCycleStartDay() {
        return cycleStartDay;
    }

    /**
     * Sets the day of month that this customer cycle starts. Use
     * <code>Billingcycle.LAST_DAY_OF_MONTH</code> for the last day
     * of the month.
     *
     * @param cycleStartDay cycle start day of month
     */
    public void setCycleStartDay(Integer cycleStartDay) {
        this.cycleStartDay = cycleStartDay;
    }

    /**
     * Returns true if today's date is between the start and end date for
     * this billing cycle.
     *
     * @return true if cycle is active for today's date, false if not.
     */
    public boolean isActive() {
        if (getStart() != null && new DateMidnight(getStart()).isAfterNow())
            return false;
        
        if (getEnd() != null && new DateMidnight(getEnd()).isBeforeNow())
            return false;

        return true;
    }

    /**
     * Returns true if the given date is between the start and end date
     * for this billing cycle.
     *
     * @param date active date
     * @return true if cycle is active for the given date, false if not.
     */
    public boolean isActive(DateMidnight date) {
        if (getStart() != null && new DateMidnight(getStart()).isAfter(date))
            return false;

        if (getEnd() != null && new DateMidnight(getEnd()).isBefore(date))
            return false;

        return true;
    }

    /**
     * The initial starting instant of the entire billing cycle. The start instant is
     * the first instance of the cycle start day after (or equal to) the cycle start date.
     *
     * @return starting instant of this billing cycle.
     */
    public DateMidnight getStartInstant() {
        DateMidnight start = new DateMidnight(getStart());
        Period period = getBillingPeriod().getPeriod();

        // first possible start date for a period
        DateMidnight calculated = getCycleStartDay() != LAST_DAY_OF_MONTH
                                  ? start.dayOfMonth().setCopy(getCycleStartDay())
                                  : start.dayOfMonth().withMaximumValue();

        // increment by billing period interval until a valid start date is found
        while (calculated.isBefore(start))
            calculated = calculated.plus(period);
        return calculated;
    }

    /**
     * The end instant of the entire billing cycle. The end instant is the last day
     * of the billing cycle, effectively {@link #getEnd()}.
     *
     * If billing cycle end is null, this method will return the start instant
     * with the year billingPeriod incremented to it's maximum value.
     *
     * @return ending instant of this billing cycle.
     */
    public DateMidnight getEndInstant() {
        return getEnd() == null
               ? getStartInstant().year().withMaximumValue()
               : new DateMidnight(getEnd());
    }    

    /**
     * Calculate the current cycle start date for this billingPeriod based on the
     * set cycle start day.
     *
     * @return calculated cycle start date for this billingPeriod.
     */
    public DateMidnight calculateCycleStart() {
        return null;
    }

    /**
     * Calculate the current cycle end date for this billingPeriod based on the
     * set cycle start day.
     *
     * @return calculated cycle end date for this billingPeriod.
     */
    public DateMidnight calculateCycleEnd() {
        return null;
    }
}
