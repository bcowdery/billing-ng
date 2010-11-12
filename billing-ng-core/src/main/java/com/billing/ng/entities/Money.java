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
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.Locale;

/**
 * Money class that preserves currency and can be persisted with zero loss
 * in precision regardless of the database vendor. 
 *
 * This class enforces the number of decimal places per currency type, where the stored
 * and displayed number of decimal places is determined by {@link java.util.Currency#getDefaultFractionDigits()}.
 *
 * Example, Japanese Yen has zero fractional digits:
 * <code>
 *      Money jpy = new Money("17.0000 JPY");
 *      jpy.toString(); // "17 JPY"
 * </code>
 *
 * All money math operations automatically convert between different currency types, allowing
 * common math operations to be performed regardless of currency differences and exchange rates.
 *
 * Example, USD + GBP:
 * <code>
 *      Money usd = new Money("10.00 USD");
 *      Money gbp = new Money("7.00 GBP");
 *
 *      usd.add(gbp); // "$20.90 USD"
 *      gbp.add(usd); // "£13.42 GBP"
 * </code> 
 *
 * @see RateTable
 *
 * @author Brian Cowdery
 * @since 11-Aug-2010
 */
@Embeddable
@XmlRootElement(name = "monetary") 
public class Money implements Serializable {

    private static final String NON_DIGIT_REGEX = "^\\D*";
    private static final String ALPHA_CHARACTER_REGEX = "[A-Za-z]";
    private static final String BLANK = "";
    private static final String WHITESPACE = " ";

    private BigDecimal value;
    private Currency currency;

    public Money() {
    }

    /**
     * Copy constructor
     * @param money money to copy
     */
    public Money(Money money) {
        // BigDecimal and Currency are both immutable or singleton - no need for defensive copies.
        this.value = money.getValue();
        this.currency = money.getCurrency();        
    }


    /**
     * Constructs money for the given value and currency.
     * @param value money value
     * @param currency currency
     */
    public Money(BigDecimal value, Currency currency) {
        this.currency = currency;
        this.value = value.setScale(currency.getDefaultFractionDigits(), RoundingMode.HALF_UP);
    }

    /**
     * Constructs money for the given value and locale.
     * @param value money value
     * @param locale locale
     */
    public Money(BigDecimal value, Locale locale) {
        this.currency = Currency.getInstance(locale);
        this.value = value.setScale(currency.getDefaultFractionDigits(), RoundingMode.HALF_UP);
    }

    /**
     * Constructs money from a simple monetary string representation of
     * the value and currency.
     *
     * Examples:
     *      new Money("$5.95 USD");
     *      new Money("10.20 GBP");
     *
     * @param amount monetary amount string and currency code
     */
    public Money(String amount) {
        String[] values = amount.trim().replaceAll(NON_DIGIT_REGEX, BLANK).split(WHITESPACE);
        this.currency = Currency.getInstance(values[1]);
        this.value = new BigDecimal(values[0]).setScale(currency.getDefaultFractionDigits(), RoundingMode.HALF_UP);
    }

    /**
     * Constructs money from a monetary amount string "$29.95" which may optionally
     * contain a monetary symbol.
     *
     * @param amount monetary amount string
     * @param currency currency
     */
    public Money(String amount, String currency) {
        this.currency = Currency.getInstance(currency);
        this.value = new BigDecimal(amount.trim().replaceAll(NON_DIGIT_REGEX, BLANK))
                .setScale(this.currency.getDefaultFractionDigits(), RoundingMode.HALF_UP);                            
    }

    /**
     * Constructs money from a integral long value and scale. Used for constructing
     * money from persisted values.
     *
     * @param value integral value
     * @param scale scale (number of decimal places)
     * @param currency currency
     */
    public Money(long value, int scale, String currency) {
        this.currency = Currency.getInstance(currency);
        this.value = new BigDecimal(BigInteger.valueOf(value), scale)
                .setScale(this.currency.getDefaultFractionDigits(), RoundingMode.HALF_UP);
    }

    @Transient
    @XmlAttribute(name = "amount")
    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    /**
     * Returns the dollar value of this money instance an integral value instead
     * of a fractional value. This is loosely the "number of pennies" of the dollar value.
     *
     * Example: <code>new Money("20.00 USD").getLongValue(); // returns 2000L</code>
     *
     * @return integral dollar value as a long
     */
    @Column(name = "value", nullable = false, length = 22)
    @XmlAttribute(name = "integral_amount")
    public long getLongValue() {
        return value.movePointRight(value.scale()).longValueExact();
    }

    public void setLongValue(long value) {

    }

    /**
     * Scale of this dollar value, effectively the number of decimal places for this dollar
     * value and currency.
     *
     * @return scale
     */
    @Column(name = "scale", nullable = false, length = 2)
    @XmlAttribute(name = "decimal_places")
    public int getScale() {
        return value.scale();
    }

    public void setScale(int scale) {

    }

    @Transient
    @XmlTransient
    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    @Column(name = "currency_code", nullable = false, length = 3)
    @XmlAttribute(name = "currency")
    public String getCurrencyCode() {
        return currency.getCurrencyCode();
    }

    public void setCurrencyCode(String currency) {
        
    }

    /**
     * Adds the given money amount. The amount to add will be converted
     * to this currency before before summing the amount.
     * @param money money to add
     * @return sum total
     */
    public Money add(Money money) {
        return new Money(value.add(convert(money).getValue()), currency);
    }

    /**
     * Subtracts the given money amount. The amount to subtract will be
     * converted to this currency before subtracting the amount.
     * @param money money to subtract
     * @return subtracted total
     */
    public Money subtract(Money money) {
        return new Money(value.subtract(convert(money).getValue()), currency);
    }

    /**
     * Multiplies this amount by the given money amount. The multiplicand amount
     * will be converted to this currency before multiplying the amount.
     * @param money money to multiply by
     * @return product
     */
    public Money multiply(Money money) {
        return multiply(convert(money).getValue());
    }

    /**
     * Multiplies this amount by the given multiplicand.
     * @param multiplicand multiplicand to multiply by
     * @return product
     */
    public Money multiply(BigDecimal multiplicand) {
        return new Money(value.multiply(multiplicand), currency);
    }

    /**
     * Divides this amount by the given money amount. The divisor amount will be
     * converted to this currency before dividing the amount.
     * @param money money to divide by
     * @return dividend
     */
    public Money divide(Money money) {
        return divide(convert(money).getValue());
    }

    /**
     * Divides this amount by the given divisor.
     * @param divisor divisor to divide by
     * @return dividend
     */
    public Money divide(BigDecimal divisor) {
        return new Money(value.divide(divisor), currency);
    }

    /**
     * Returns the absolute value of this money amount.
     * @return money amount
     */
    public Money abs() {
        return new Money(value.abs(), currency);
    }

    /**
     * Negates this money amount, returning the negative value of a positive amount, or
     * the positive value of a negative amount.
     * @return negated value.
     */
    public Money negate() {
        return new Money(value.negate(), currency);
    }

    /**
     * Converts the given money to the same currency as this instance.
     * @param money money to convert
     * @return converted money
     */
    public Money convert(Money money) {
        if (currency.equals(money.getCurrency()))
            return money;

        RateTable table = RateTable.getInstance();

        // convert to system currency, maintain scale of conversion rate for accuracy
        Rate system = table.getRate(money.getCurrencyCode());
        BigDecimal pivot = money.getValue().divide(system.getRate(), system.getRate().scale(), RoundingMode.HALF_UP);

        // convert back to target currency
        Rate target = table.getRate(currency.getCurrencyCode());
        BigDecimal converted = pivot.multiply(target.getRate());

        // drop scale down to currency's default precision
        return new Money(converted.setScale(currency.getDefaultFractionDigits(), RoundingMode.HALF_UP), currency);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder()
                .append(currency.getSymbol().replaceAll(ALPHA_CHARACTER_REGEX, BLANK))
                .append(value)
                .append(WHITESPACE)
                .append(currency.getCurrencyCode());

        return builder.toString();
    }

    public String toString(Locale locale) {
        StringBuilder builder = new StringBuilder()
                .append(currency.getSymbol(locale).replaceAll(ALPHA_CHARACTER_REGEX, BLANK))
                .append(value)
                .append(WHITESPACE)
                .append(currency.getCurrencyCode());

        return builder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (o == null || getClass() != o.getClass())
            return false;

        Money money = (Money) o;
        return currency.equals(money.currency) && value.equals(money.value);
    }

    @Override
    public int hashCode() {
        int result = value.hashCode();
        result = 31 * result + currency.hashCode();
        return result;
    }
}
