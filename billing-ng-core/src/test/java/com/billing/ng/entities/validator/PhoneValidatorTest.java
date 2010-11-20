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

import com.billing.ng.entities.BaseEntity;
import org.hamcrest.Matchers;
import org.hibernate.validator.InvalidValue;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.hasItem;
import static org.testng.Assert.*;

/**
 * PhoneValidatorTest
 *
 * @author brian
 * @since 2010-11-19
 */
@Test(groups = {"quick", "validation"})
public class PhoneValidatorTest {

    /**
     * Class under test
     */
    private class TestEntity extends BaseEntity {
        @Phone
        private String phone;

        public TestEntity(String phone) {
            this.phone = phone;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }
    }

    @Test
    public void test7Digit() {
        TestEntity entity = new TestEntity("100 1000");
        Set<ConstraintViolation<TestEntity>> constraintViolations = entity.validateConstraints();

        assertTrue(constraintViolations.isEmpty());

        entity = new TestEntity("100.1000");
        constraintViolations = entity.validateConstraints();

        assertTrue(constraintViolations.isEmpty());

        entity = new TestEntity("100-1000");
        constraintViolations = entity.validateConstraints();

        assertTrue(constraintViolations.isEmpty());
    }

    @Test
    public void test10Digit() {
        TestEntity entity = new TestEntity("100 100 1000");
        Set<ConstraintViolation<TestEntity>> constraintViolations = entity.validateConstraints();

        assertTrue(constraintViolations.isEmpty());

        entity = new TestEntity("100.100.1000");
        constraintViolations = entity.validateConstraints();

        assertTrue(constraintViolations.isEmpty());

        entity = new TestEntity("100-100-1000");
        constraintViolations = entity.validateConstraints();

        assertTrue(constraintViolations.isEmpty());
    }

    @Test
    public void testOptionalBrackets() {
        TestEntity entity = new TestEntity("(100) 100 1000");
        Set<ConstraintViolation<TestEntity>> constraintViolations = entity.validateConstraints();

        assertTrue(constraintViolations.isEmpty());

        entity = new TestEntity("(100) 100-1000");
        constraintViolations = entity.validateConstraints();

        assertTrue(constraintViolations.isEmpty());

        entity = new TestEntity("+123 (100) 100.1000");
        constraintViolations = entity.validateConstraints();

        assertTrue(constraintViolations.isEmpty());
    }

    @Test
    public void testInternational() {
        TestEntity entity = new TestEntity("+1 100 100 1000");
        Set<ConstraintViolation<TestEntity>> constraintViolations = entity.validateConstraints();

        assertTrue(constraintViolations.isEmpty());

        entity = new TestEntity("+12.100.100.1000");
        constraintViolations = entity.validateConstraints();

        assertTrue(constraintViolations.isEmpty());

        entity = new TestEntity("+123-100-100-1000");
        constraintViolations = entity.validateConstraints();

        assertTrue(constraintViolations.isEmpty());
    }

    @Test
    public void testInvalidInternational() {
        TestEntity entity = new TestEntity("1 100 100 1000");
        Set<ConstraintViolation<TestEntity>> constraintViolations = entity.validateConstraints();

        assertThat(constraintViolations.size(), is(1));
        assertThat(constraintViolations, hasItem(Matchers.<ConstraintViolation<TestEntity>>hasProperty("invalidValue", is("1 100 100 1000"))));
        assertThat(constraintViolations, hasItem(Matchers.<ConstraintViolation<TestEntity>>hasProperty("message", is("{com.billing.ng.constraints.phone}"))));

        entity = new TestEntity("12.100.100.1000");
        constraintViolations = entity.validateConstraints();

        assertThat(constraintViolations.size(), is(1));

        entity = new TestEntity("123-100-100-1000");
        constraintViolations = entity.validateConstraints();

        assertThat(constraintViolations.size(), is(1));
    }

    @Test
    public void testAlphanumeric() {
        TestEntity entity = new TestEntity("100 100b");

        Set<ConstraintViolation<TestEntity>> constraintViolations = entity.validateConstraints();

        assertThat(constraintViolations.size(), is(1));
        assertThat(constraintViolations, hasItem(Matchers.<ConstraintViolation<TestEntity>>hasProperty("invalidValue", is("100 100b"))));
        assertThat(constraintViolations, hasItem(Matchers.<ConstraintViolation<TestEntity>>hasProperty("message", is("{com.billing.ng.constraints.phone}"))));
    }

    @Test
    public void testInvalidDelimiter() {
        TestEntity entity = new TestEntity("100_1000");
        
        Set<ConstraintViolation<TestEntity>> constraintViolations = entity.validateConstraints();

        assertThat(constraintViolations.size(), is(1));
        assertThat(constraintViolations, hasItem(Matchers.<ConstraintViolation<TestEntity>>hasProperty("invalidValue", is("100_1000"))));
        assertThat(constraintViolations, hasItem(Matchers.<ConstraintViolation<TestEntity>>hasProperty("message", is("{com.billing.ng.constraints.phone}"))));

        entity = new TestEntity("100@1000");
        constraintViolations = entity.validateConstraints();
        assertThat(constraintViolations.size(), is(1));

        entity = new TestEntity("100$1000");
        constraintViolations = entity.validateConstraints();
        assertThat(constraintViolations.size(), is(1));
    }

    @Test
    public void testInvalidLength() {
        TestEntity entity = new TestEntity("+123 100 100 10000");

        Set<ConstraintViolation<TestEntity>> constraintViolations = entity.validateConstraints();

        assertThat(constraintViolations.size(), is(1));
        assertThat(constraintViolations, hasItem(Matchers.<ConstraintViolation<TestEntity>>hasProperty("invalidValue", is("+123 100 100 10000"))));
        assertThat(constraintViolations, hasItem(Matchers.<ConstraintViolation<TestEntity>>hasProperty("message", is("{com.billing.ng.constraints.phone}"))));       
    }
}
