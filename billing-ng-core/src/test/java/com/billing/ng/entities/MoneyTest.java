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

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.Locale;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * MoneyTest
 *
 * @author Brian Cowdery
 * @since 15-Aug-2010
 */
@Test(groups = { "money", "quick" })
public class MoneyTest {

    @BeforeClass
    public void setupRateTable() {
        // August 15, 2010
        // from http://www.xe.com/currency/usd-us-dollar
        RateTable table = RateTable.getInstance();
        table.setSystemRate(new Rate(new BigDecimal("1"), "USD"));
        table.addRate(new Rate(new BigDecimal("1.04279"), "CAD"));
        table.addRate(new Rate(new BigDecimal("0.64218"), "GBP"));
        table.addRate(new Rate(new BigDecimal("85.8221"), "JPY"));
    }

    @Test
    public void testCreateFromLocale() throws Exception {
        Money cad = new Money(new BigDecimal("20.03"), Locale.CANADA);

        assertThat(cad.getValue(), is(new BigDecimal("20.03")));
        assertThat(cad.getCurrency().getCurrencyCode(), is("CAD"));

        Money pound = new Money(new BigDecimal("22.00"), Locale.UK);

        assertThat(pound.getValue(), is(new BigDecimal("22.00")));
        assertThat(pound.getCurrency().getCurrencyCode(), is("GBP"));
    }

    @Test
    public void testCreateFromString() throws Exception {
        Money money = new Money("$10.95 USD");

        assertThat(money.getValue(), is(new BigDecimal("10.95")));
        assertThat(money.getCurrency().getCurrencyCode(), is("USD"));
    }

    @Test
    public void testCreateFromStringAndCurrency() throws Exception {
        Money pounds = new Money("11.33", "GBP");

        assertThat(pounds.getValue(), is(new BigDecimal("11.33")));
        assertThat(pounds.getCurrency().getCurrencyCode(), is("GBP"));

        Money cad = new Money("13.79", "CAD");

        assertThat(cad.getValue(), is(new BigDecimal("13.79")));
        assertThat(cad.getCurrency().getCurrencyCode(), is("CAD"));
    }

    @Test
    public void testCreateFromLongValueAndScale() throws Exception {
        Money money = new Money(2939L, 2, "CAD");

        assertThat(money.getValue(), is(new BigDecimal("29.39")));
    }

    @Test
    public void testGetLongValue() throws Exception {
        Money smallScale = new Money("$29.93 CAD");
        assertThat(smallScale.getLongValue(), is(2993L));

        Money largeScale = new Money("$29.9555556 CAD");
        assertThat(largeScale.getLongValue(), is(2996L)); // dropped down to the default CAD fractional digits
    }

    @Test
    public void testGetScale() throws Exception {
        Money smallScale = new Money("$29.96 CAD");
        assertThat(smallScale.getScale(), is(2));

        Money largeScale = new Money("$29.9777778 CAD");
        assertThat(largeScale.getScale(), is(2)); // dropped down to default CAD fractional digits

        Money noScale = new Money("31 JPY");
        assertThat(noScale.getScale(), is(0)); // no significant digits for JPY
    }

    /**
     * Test conversion between the system rate and a foreign rate
     * @throws Exception unexpected exception
     */
    @Test
    public void testConvertCADtoUSD() throws Exception {
        Money usd = new Money("1.00", "USD");
        Money cad = new Money("1.00", "CAD");

        Money convertedCad = usd.convert(cad); // convert CAD to USD
        assertThat(convertedCad.getValue(), is(new BigDecimal("0.96")));
        assertThat(convertedCad.getCurrencyCode(), is("USD"));
        assertThat(convertedCad.getCurrency().getDefaultFractionDigits(), is(convertedCad.getValue().scale()));

        Money convertedUsd = cad.convert(usd); // convert USD to CAD
        assertThat(convertedUsd.getValue(), is(new BigDecimal("1.04")));
        assertThat(convertedUsd.getCurrencyCode(), is("CAD"));
        assertThat(convertedUsd.getCurrency().getDefaultFractionDigits(), is(convertedUsd.getValue().scale()));

    }

    /**
     * Test conversion between two foreign rates, neither of which is the
     * system rate.
     * @throws Exception unexpected exception
     */
    @Test
    public void testConvertGBPtoJPY() throws Exception {
        Money gbp = new Money("1.00", "GBP");
        Money jpy = new Money("100.00", "JPY");

        Money convertedGbp = jpy.convert(gbp); // convert GBP to JPY
        assertThat(convertedGbp.getValue(), is(new BigDecimal("134")));
        assertThat(convertedGbp.getCurrencyCode(), is("JPY"));
        assertThat(convertedGbp.getCurrency().getDefaultFractionDigits(), is(convertedGbp.getValue().scale()));

        Money convertedJpy = gbp.convert(jpy); // convert JPY to GBP
        assertThat(convertedJpy.getValue(), is(new BigDecimal("0.75")));
        assertThat(convertedJpy.getCurrencyCode(), is("GBP"));
        assertThat(convertedJpy.getCurrency().getDefaultFractionDigits(), is(convertedJpy.getValue().scale()));
    }

    // todo: money math tests.

    @Test
    public void testToString() throws Exception {
        Money usd = new Money("$56.23 USD");
        assertThat(usd.toString(), is("$56.23 USD"));

        Money gbp = new Money("73.24 GBP");
        assertThat(gbp.toString(), is("73.24 GBP"));

        Money jpy = new Money("81 JPY");
        assertThat(jpy.toString(), is("81 JPY"));
    }

    @Test
    public void testToStringWithLocale() throws Exception {
        Money jpy = new Money("193 JPY");
        assertThat(jpy.toString(Locale.JAPAN), is("￥193 JPY"));

        Money gbp = new Money("17.00 GBP");
        assertThat(gbp.toString(Locale.UK), is("£17.00 GBP"));

        // GBP printed in a Japanese locale
        Money compound = new Money("98.23 GBP");
        assertThat(compound.toString(Locale.JAPAN), is("98.23 GBP"));
    }
}
