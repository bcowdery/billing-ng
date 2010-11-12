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
import org.hibernate.validator.InvalidValue;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.*;

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

        @Length(min = 0, max = 20)
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

        List<InvalidValue> invalid = Arrays.asList(entity.validate());
        
        assertThat(invalid.size(), is(1));

        assertThat(invalid, hasItem(Matchers.<InvalidValue>hasProperty("propertyName", is("id"))));
        assertThat(invalid, hasItem(Matchers.<InvalidValue>hasProperty("message", is("may not be null"))));
    }

    @Test
    public void testMultipleValidations() {
        TestEntity entity = new TestEntity();
        entity.setId(null);                        // not null validator
        entity.setString("1234567890abcdefghijk"); // 21, exceeds max length validator

        List<InvalidValue> invalid = Arrays.asList(entity.validate());

        assertThat(invalid.size(), is(2));

        assertThat(invalid, hasItem(Matchers.<InvalidValue>hasProperty("propertyName", is("id"))));
        assertThat(invalid, hasItem(Matchers.<InvalidValue>hasProperty("message", is("may not be null"))));

        assertThat(invalid, hasItem(Matchers.<InvalidValue>hasProperty("propertyName", is("string"))));
        assertThat(invalid, hasItem(Matchers.<InvalidValue>hasProperty("message", is("length must be between 0 and 20"))));
    }
}
