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

import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * NumberPatternTest
 *
 * @author Brian Cowdery
 * @since 23-Nov-2010
 */
@Test(groups = {"quick"})
public class NumberPatternTest {

    /**
     * Class under test
     */
    public static class TestEntity {
        private Long id;
        private String value;

        private TestEntity() {
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    @Test
    public void testGenerate() {
        NumberPattern pattern = new NumberPattern();
        pattern.setPattern("No. ${entity.id?string(\"00000\")}");

        TestEntity entity = new TestEntity();
        entity.setId(12L);
        entity.setValue("some random value");

        assertThat(pattern.generate("entity", entity), is("No. 00012"));
    }

    @Test
    public void testGenerateNullPattern() {
        NumberPattern pattern = new NumberPattern();
        pattern.setPattern(null);

        assertThat(pattern.generate("entity", new TestEntity()), nullValue());
    }

    @Test
    public void testGenerateNullEntity() {
        NumberPattern pattern = new NumberPattern();
        pattern.setPattern("${entity.id}");

        assertThat(pattern.generate("entity", null), nullValue());
    }
}
