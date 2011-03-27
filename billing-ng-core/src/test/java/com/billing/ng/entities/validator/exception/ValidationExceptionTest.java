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

import com.billing.ng.plugin.test.TestPluginImpl;
import org.hamcrest.Matchers;
import org.hibernate.validator.NotEmpty;
import org.testng.annotations.Test;

import com.billing.ng.entities.BaseEntity;

import javax.validation.ConstraintViolation;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * ValidationExceptionTest
 *
 * @author Brian Cowdery
 * @since 29/01/11
 */
@Test(groups = {"quick", "validation"})
public class ValidationExceptionTest {

    /**
     * Class under test
     */
    private class TestEntity extends BaseEntity {
        @NotNull
        private Long id;

        @Size(min = 0, max = 5)
        private String string;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getString() {
            return string;
        }

        public void setString(String string) {
            this.string = string;
        }
    }

    @Test
    public void testValidationExceptionMessage() {
        TestEntity entity = new TestEntity();
        entity.setId(null);
        entity.setString("more than five chars");

        Set<ConstraintViolation<TestEntity>> constraintViolations = entity.validateConstraints();
        Exception exception = new ValidationException(constraintViolations);

        // sets don't maintain any order of elements, so we could get multiple error messages
        // as there's no guarantee which order the errors will be printed in
        String expectedOne = "Constraint violations for TestEntity\n"
                             + "\tstring size must be between 0 and 5, was: 'more than five chars'\n"
                             + "\tid may not be null\n";

        String expectedTwo = "Constraint violations for TestEntity\n"
                             + "\tid may not be null\n"
                             + "\tstring size must be between 0 and 5, was: 'more than five chars'\n";


        assertThat(exception.getMessage(), isOneOf(expectedOne, expectedTwo));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testValidationExceptionViolations() {
        TestEntity entity = new TestEntity();
        entity.setId(null);
        entity.setString("more than five chars");

        ValidationException exception = new ValidationException(entity.validateConstraints());
        Set<ConstraintViolation<TestEntity>> constraintViolations = (Set<ConstraintViolation<TestEntity>>) exception.getConstraintViolations();

        assertThat(constraintViolations.size(), is(2));
        assertThat(constraintViolations, hasItem(Matchers.<ConstraintViolation<TestEntity>>hasProperty("message", is("may not be null"))));
        assertThat(constraintViolations, hasItem(Matchers.<ConstraintViolation<TestEntity>>hasProperty("message", is("size must be between 0 and 5"))));
    }
}
