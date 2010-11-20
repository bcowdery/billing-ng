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

import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import javax.validation.ConstraintViolation;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

/**
 * BaseEntityTest
 *
 * @author Brian Cowdery
 * @since 25-Aug-2010
 */
@Test(groups = { "entity", "quick", "validation" })
public class BaseEntityTest {

    /**
     * Class under test
     */
    private class TestEntity extends BaseEntity {

        @NotNull
        private Long id;

        @Size(min = 0, max = 20)
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
    public void testSingleValidation() {
        TestEntity entity = new TestEntity();
        entity.setId(null); // not null validator

        Set<ConstraintViolation<TestEntity>> constraintViolations = entity.validateConstraints();

        assertThat(constraintViolations.size(), is(1));

        assertThat(constraintViolations, hasItem(Matchers.<ConstraintViolation<TestEntity>>hasProperty("invalidValue", nullValue())));
        assertThat(constraintViolations, hasItem(Matchers.<ConstraintViolation<TestEntity>>hasProperty("message", is("may not be null"))));
    }

    @Test
    public void testMultipleValidations() {
        TestEntity entity = new TestEntity();
        entity.setId(null);                        // not null validator
        entity.setString("1234567890abcdefghijk"); // 21, exceeds max length validator

        Set<ConstraintViolation<TestEntity>> constraintViolations = entity.validateConstraints();

        assertThat(constraintViolations.size(), is(2));

        assertThat(constraintViolations, hasItem(Matchers.<ConstraintViolation<TestEntity>>hasProperty("invalidValue", nullValue())));
        assertThat(constraintViolations, hasItem(Matchers.<ConstraintViolation<TestEntity>>hasProperty("message", is("may not be null"))));

        assertThat(constraintViolations, hasItem(Matchers.<ConstraintViolation<TestEntity>>hasProperty("invalidValue", is("1234567890abcdefghijk"))));
        assertThat(constraintViolations, hasItem(Matchers.<ConstraintViolation<TestEntity>>hasProperty("message", is("size must be between 0 and 20"))));
    }
}
