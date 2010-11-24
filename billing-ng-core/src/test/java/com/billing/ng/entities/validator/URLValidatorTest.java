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
import org.testng.annotations.Test;

import javax.validation.ConstraintViolation;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.hasItem;
import static org.testng.Assert.*;

/**
 * URLValidatorTest
 *
 * @author Brian Cowdery
 * @since 21-Nov-2010
 */
@Test(groups = {"quick", "validation"})
public class URLValidatorTest {

    /**
     * Class under test
     */
    private class TestEntity extends BaseEntity {
        @URL
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
    public void testBareURL() {
        TestEntity entity = new TestEntity("www.billing-ng.com");
        Set<ConstraintViolation<TestEntity>> constraintViolations = entity.validateConstraints();

        assertThat(constraintViolations.size(), is(1));

        assertThat(constraintViolations, hasItem(Matchers.<ConstraintViolation<TestEntity>>hasProperty("invalidValue", is("www.billing-ng.com"))));
        assertThat(constraintViolations, hasItem(Matchers.<ConstraintViolation<TestEntity>>hasProperty("message", is("{com.billing.ng.constraints.url}"))));        
    }

    @Test
    public void testURLWithoutWWW() {
        TestEntity entity = new TestEntity("http://billing-ng.com");
        Set<ConstraintViolation<TestEntity>> constraintViolations = entity.validateConstraints();

        assertTrue(constraintViolations.isEmpty());
    }

    @Test
    public void testHTTP() {
        TestEntity entity = new TestEntity("http://www.billing-ng.com");
        Set<ConstraintViolation<TestEntity>> constraintViolations = entity.validateConstraints();

        assertTrue(constraintViolations.isEmpty());
    }

    @Test
    public void testHTTPS() {
        TestEntity entity = new TestEntity("https://www.billing-ng.com");
        Set<ConstraintViolation<TestEntity>> constraintViolations = entity.validateConstraints();

        assertTrue(constraintViolations.isEmpty());
    }

    @Test
    public void testURLWithParameters() {
        TestEntity entity = new TestEntity("http://www.billing-ng.com/query?param=123&param2=abc");
        Set<ConstraintViolation<TestEntity>> constraintViolations = entity.validateConstraints();

        assertTrue(constraintViolations.isEmpty());        
    }

    @Test
    public void testRestfulURL() {
        TestEntity entity = new TestEntity("http://www.billing-ng.com/order/edit/1");
        Set<ConstraintViolation<TestEntity>> constraintViolations = entity.validateConstraints();

        assertTrue(constraintViolations.isEmpty());
    }

    @Test
    public void testNonURLString() {
        TestEntity entity = new TestEntity("isnotaurl");
        Set<ConstraintViolation<TestEntity>> constraintViolations = entity.validateConstraints();

        assertThat(constraintViolations.size(), is(1));

        assertThat(constraintViolations, hasItem(Matchers.<ConstraintViolation<TestEntity>>hasProperty("invalidValue", is("isnotaurl"))));
        assertThat(constraintViolations, hasItem(Matchers.<ConstraintViolation<TestEntity>>hasProperty("message", is("{com.billing.ng.constraints.url}"))));
    }

    @Test
    public void testInvalidProtocol() {
        TestEntity entity = new TestEntity("bad://www.billing-ng.com");
        Set<ConstraintViolation<TestEntity>> constraintViolations = entity.validateConstraints();

        assertThat(constraintViolations.size(), is(1));

        assertThat(constraintViolations, hasItem(Matchers.<ConstraintViolation<TestEntity>>hasProperty("invalidValue", is("bad://www.billing-ng.com"))));
        assertThat(constraintViolations, hasItem(Matchers.<ConstraintViolation<TestEntity>>hasProperty("message", is("{com.billing.ng.constraints.url}"))));

        entity = new TestEntity("://www.billing-ng.com");
        constraintViolations = entity.validateConstraints();

        assertThat(constraintViolations.size(), is(1));

        assertThat(constraintViolations, hasItem(Matchers.<ConstraintViolation<TestEntity>>hasProperty("invalidValue", is("://www.billing-ng.com"))));
        assertThat(constraintViolations, hasItem(Matchers.<ConstraintViolation<TestEntity>>hasProperty("message", is("{com.billing.ng.constraints.url}"))));
    }
}
