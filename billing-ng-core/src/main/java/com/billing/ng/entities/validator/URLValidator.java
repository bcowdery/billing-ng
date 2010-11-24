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

package com.billing.ng.entities.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.Serializable;
import java.net.MalformedURLException;

/**
 * Validates that a string is a valid URL.
 *
 * @author Brian Cowdery
 * @since 21-Nov-2010
 */
public class URLValidator implements ConstraintValidator<URL, String>, Serializable {

    private static final long serialVersionUID = 1L;

    public void initialize(URL url) {
    }

    public boolean isValid(String value, ConstraintValidatorContext constraintContext) {
        if (value == null || "".equals(value))
            return true;

        try {
            java.net.URL url = new java.net.URL(value);
        } catch (MalformedURLException e) {
            return false;
        }

        return true;
    }
}
