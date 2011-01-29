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

import org.joda.time.DateMidnight;
import org.joda.time.Days;
import org.joda.time.Months;
import org.joda.time.Period;
import org.joda.time.Weeks;
import org.joda.time.Years;

import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;

/**
 * The current billing cycle is calculated from a billing period and the initial billing
 * start date. Current billing cycle calculations are done in linear time, but are still
 * fairly expensive.
 *
 * A calculated current billing cycle is immutable, and is not persisted.
 *
 * @author Brian Cowdery
 * @since 29-Jan-2011
 */
@XmlTransient
public class CurrentBillingCycle implements Serializable {

    private final Integer cycleNumber;
    private final DateMidnight start;
    private final DateMidnight end;

    public CurrentBillingCycle() {
        this.cycleNumber = null;
        this.start = null;
        this.end = null;
    }

    /**
     * Calculate the current billing cycle for today's date.
     *
     * @param period billing period
     * @param billingStart billing cycle start date
     */
    public CurrentBillingCycle(BillingPeriod period, DateMidnight billingStart) {
        this(period, billingStart, new DateMidnight());
    }

    /**
     * Calculate the current billing cycle for the given date.
     *
     * @param period billing period
     * @param billingStart billing cycle start date
     * @param today date to calculate the current billing cycle for
     */
    public CurrentBillingCycle(BillingPeriod period, DateMidnight billingStart, DateMidnight today) {
        this.cycleNumber = calculateCycleNumber(period, billingStart, today);
        this.start = calculateCycleStart(period, billingStart, cycleNumber);
        this.end = calculateCycleEnd(period, start);
    }

    public Integer getCycleNumber() {
        return cycleNumber;
    }

    public DateMidnight getStart() {
        return start;
    }

    public DateMidnight getEnd() {
        return end;
    }

    /**
     * Calculates the number of complete cycles between the given billing start date and
     * the given "today's" date.
     *
     * @param period billing period
     * @param billingStart billing cycle start
     * @param today end date
     * @return number of complete cycles
     */
    public Integer calculateCycleNumber(BillingPeriod period, DateMidnight billingStart, DateMidnight today) {
        Integer interval = period.getInterval();

        switch (period.getType()) {
            case DAY:
                return Days.daysBetween(billingStart, today).getDays() / interval;

            case WEEK:
                return Weeks.weeksBetween(billingStart, today).getWeeks() / interval;

            case MONTH:
                return Months.monthsBetween(billingStart, today).getMonths() / interval;

            case YEAR:
                return Years.yearsBetween(billingStart, today).getYears() / interval;
        }
        return null;
    }

    /**
     * Calculate the start date of the current cycle.
     *
     * @param period billing period
     * @param billingStart billing cycle start
     * @param cycleNumber current cycle number
     * @return calculated cycle start date
     */
    public DateMidnight calculateCycleStart(BillingPeriod period, DateMidnight billingStart, Integer cycleNumber) {
        Period increment = period.getPeriodOfTime(cycleNumber); // elapsed period of time for cycle number
        return billingStart.plus(increment);
    }

    /**
     * Calculate the end date of the current cycle.
     *
     * @param period billing period
     * @param cycleStart calculated start date for the current cycle
     * @return calculated cycle end date
     */
    public DateMidnight calculateCycleEnd(BillingPeriod period, DateMidnight cycleStart) {
        Period increment = period.getPeriodOfTime(); // single period
        return cycleStart.plus(increment);
    }

    @Override
    public String toString() {
        return "CurrentBillingCycle{"
               + "cycleNumber=" + cycleNumber
               + ", start=" + start
               + ", end=" + end
               + '}';
    }
}
