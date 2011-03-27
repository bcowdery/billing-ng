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

package com.billing.ng.entities.validator.exception;

import javax.validation.ConstraintViolation;
import java.util.Set;

/**
 * ValidationException
 *
 * @author Brian Cowdery
 * @since 29/01/11
 */
public class ValidationException extends RuntimeException {

    private final Set<? extends ConstraintViolation<?>> constraintViolations;

    public ValidationException() {
        this.constraintViolations = null;
    }

    public ValidationException(Set<? extends ConstraintViolation<?>> constraintViolations) {
        super(buildMessage(constraintViolations));
        this.constraintViolations = constraintViolations;
    }

    public Set<? extends ConstraintViolation<?>> getConstraintViolations() {
        return constraintViolations;
    }

    /**
     * Builds a human readable message from the given set of constraint violations.
     *
     * @param constraintViolations constraint violations
     * @return message string
     */
    public static String buildMessage(Set<? extends ConstraintViolation<?>> constraintViolations) {
        if (constraintViolations.isEmpty())
            return null;

        StringBuilder builder = new StringBuilder();

        for (ConstraintViolation error : constraintViolations) {
            if (builder.length() == 0) {
                builder.append("Constraint violations for ");
                builder.append(error.getRootBean().getClass().getSimpleName());
                builder.append("\n");
            }

            builder.append("\t");
            builder.append(error.getPropertyPath());
            builder.append(" ");
            builder.append(error.getMessage());

            if (error.getInvalidValue() != null) {
                builder.append(", was: '");
                builder.append(error.getInvalidValue());
                builder.append("'");
            }

            builder.append("\n");
        }

        return builder.toString();
    }
}
