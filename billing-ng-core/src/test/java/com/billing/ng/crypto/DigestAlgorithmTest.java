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

package com.billing.ng.crypto;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.text.pattern.PatternMatcher.matchesPattern;
import static org.testng.Assert.*;

/**
 * DigestAlgorithmTest
 *
 * @author Brian Cowdery
 * @since 14/01/11
 */
@Test(groups = {"crypto", "quick"})
public class DigestAlgorithmTest {

    private static String PLAIN_TEXT = "Plain Text String! #123456789 @test.com";

    @DataProvider(name = "digest_algorithms")
    public Object[][] getAllDigestAlgorithms() {
        return new Object[][] {
                {
                        DigestAlgorithm.MD5,
                        "TAhzqKKQenwKn5dxq87wzg==\r\n"
                },
                {
                        DigestAlgorithm.RipeMD128,
                        "k4jH/zkGAAIC5HZefIfJPg==\r\n"
                },
                {
                        DigestAlgorithm.RipeMD160,
                        "/uFW+nT/RroOa43+oPAY7oqZWTw=\r\n"
                },
                {
                        DigestAlgorithm.RipeMD256,
                        "GXC9+TqsM+5VHShwmFMrgsCZr8RSv2gmr0jgCOV0Zas=\r\n"
                },
                {
                        DigestAlgorithm.RipeMD320,
                        "8WiqXaPaYDViLtes78Zvs3S/6nA7Rl0uAQsMQ1pIXa3VLx9jvq7THA==\r\n"
                },
                {
                        DigestAlgorithm.SHA1,
                        "Je4dk9ZIFqKADaTRhkDr7p9BGzM=\r\n"
                },
                {
                        DigestAlgorithm.SHA256,
                        "z8pHCOwefOzyPwmjqaNq/uXHaESJ0BHeZbn/5p/YDBI=\r\n"
                },
                {
                        DigestAlgorithm.SHA384,
                        "jnIYY918IfVDaIt2o2RWYmrsxIH7ZzPsDblkcUQodLsCr8W6Kl74bWyeOHxQmxcV\r\n"
                },
                {
                        DigestAlgorithm.SHA512,
                        "feYfGu2VmJcONP7212V7lSjKL2nJKh3HLjwUhHd9C+ZHvCn9F+KVtd65r9sznyi/aqobO5+89rK4\r\nApvfuPQHvA==\r\n"
                },
                {
                        DigestAlgorithm.Tiger,
                        "ESikkdwGEP+XsDR/8ldeqHQ5a4h2waBy\r\n"
                },
                {
                        DigestAlgorithm.GOST3411,
                        "LmcNWH1HjTypB/5CcFYBWM3jRnLhIs7cPce1tpm1wto=\r\n"
                },
                {
                        DigestAlgorithm.Whirlpool,
                        "zTzXr6nLXV2Q2cHAc+Kg2poZ52AArWtV+fuBE7OmJxWOtmBHxYHfYbiKXBspzT2UlZmgYQdziq3r\r\nDo2NVxqqHg==\r\n"
                }
        };
    }

    @Test(dataProvider = "digest_algorithms")
    public void testDigest(DigestAlgorithm algorithm, String expected) throws Exception {
        String hash = algorithm.digest(PLAIN_TEXT);
        assertThat(hash, is(not(PLAIN_TEXT)));
        assertThat(hash, is(expected));
    }
}
