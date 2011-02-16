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

package com.billing.ng.plugin.annotation;

import java.math.BigInteger;
import java.net.URL;

/**
 * TestPlugin
 *
 * @author Brian Cowdery
 * @since 15/02/11
 */
@Plugin(name = "test plugin")
public class AnnotatedTestPlugin {

    private String description;
    private Integer number;
    private BigInteger decimal;

    public AnnotatedTestPlugin() {
    }

    @Parameter(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Parameter(name = "name")
    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    @Parameter(name = "decimal")
    public BigInteger getDecimal() {
        return decimal;
    }

    public void setDecimal(BigInteger decimal) {
        this.decimal = decimal;
    }
}
