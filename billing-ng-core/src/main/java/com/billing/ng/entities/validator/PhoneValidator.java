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

/**
 * Validates phone numbers to ensure the correct character count and formatting.
 *
 * Phone numbers must at least 7 digits, but can be 10 or more for international calling
 * numbers. Number tokens can be delimited using a hyphen, period or whitespace, and may
 * be used in any combination or omitted entirely.
 *
 *  Valid:
 *          310-1010
 *      000-310-1010
 *      000.310.1010
 *      000 310 1010
 *      000 310-1010
 *   +1 000 3010 1010
 * +123 000 3010 1010  etc.
 *
 * This validator will also accept null values and blank strings as valid. If you wish to enforce not
 * null values you should use the @NotNull constraint validator annotation.
 *
 * @author Brian Cowdery
 * @since 17-Nov-2010
 */
public class PhoneValidator implements ConstraintValidator<Phone, String>, Serializable {

    private static final long serialVersionUID = 1L;

    private static final String REGEX = "(^(([\\+]\\d{1,3})?[\\s\\.-]?[\\(]?\\d{3}[\\)]?)?[\\s\\.-]?\\d{3}[\\s\\.-]?\\d{4}$)";

    public void initialize(Phone phone) {
    }
            
    public boolean isValid(String value, ConstraintValidatorContext constraintContext) {
        if (value == null || "".equals(value))
            return true;

        return value.matches(REGEX);
    }
}
