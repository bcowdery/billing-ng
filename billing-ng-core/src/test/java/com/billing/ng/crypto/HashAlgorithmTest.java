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

/**
 * DigestAlgorithmTest
 *
 * @author Brian Cowdery
 * @since 14/01/11
 */
@Test(groups = {"crypto", "quick"})
public class HashAlgorithmTest {

    private static String PLAIN_TEXT = "Plain Text String! #123456789 @test.com";
    private static String SALT = "74neA70ou04r5aLV1ejGT4wKHtkD1fTmrMtWIW7IMrYiQvSinAszh85PsZs8Ih";

    @DataProvider(name = "hash_results")
    public Object[][] getHashResults() {
        return new Object[][] {
                {
                        HashAlgorithm.MD5,
                        "TAhzqKKQenwKn5dxq87wzg==\r\n"
                },
                {
                        HashAlgorithm.RipeMD128,
                        "k4jH/zkGAAIC5HZefIfJPg==\r\n"
                },
                {
                        HashAlgorithm.RipeMD160,
                        "/uFW+nT/RroOa43+oPAY7oqZWTw=\r\n"
                },
                {
                        HashAlgorithm.RipeMD256,
                        "GXC9+TqsM+5VHShwmFMrgsCZr8RSv2gmr0jgCOV0Zas=\r\n"
                },
                {
                        HashAlgorithm.RipeMD320,
                        "8WiqXaPaYDViLtes78Zvs3S/6nA7Rl0uAQsMQ1pIXa3VLx9jvq7THA==\r\n"
                },
                {
                        HashAlgorithm.SHA1,
                        "Je4dk9ZIFqKADaTRhkDr7p9BGzM=\r\n"
                },
                {
                        HashAlgorithm.SHA256,
                        "z8pHCOwefOzyPwmjqaNq/uXHaESJ0BHeZbn/5p/YDBI=\r\n"
                },
                {
                        HashAlgorithm.SHA384,
                        "jnIYY918IfVDaIt2o2RWYmrsxIH7ZzPsDblkcUQodLsCr8W6Kl74bWyeOHxQmxcV\r\n"
                },
                {
                        HashAlgorithm.SHA512,
                        "feYfGu2VmJcONP7212V7lSjKL2nJKh3HLjwUhHd9C+ZHvCn9F+KVtd65r9sznyi/aqobO5+89rK4\r\nApvfuPQHvA==\r\n"
                },
                {
                        HashAlgorithm.Tiger,
                        "ESikkdwGEP+XsDR/8ldeqHQ5a4h2waBy\r\n"
                },
                {
                        HashAlgorithm.GOST3411,
                        "LmcNWH1HjTypB/5CcFYBWM3jRnLhIs7cPce1tpm1wto=\r\n"
                },
                {
                        HashAlgorithm.Whirlpool,
                        "zTzXr6nLXV2Q2cHAc+Kg2poZ52AArWtV+fuBE7OmJxWOtmBHxYHfYbiKXBspzT2UlZmgYQdziq3r\r\nDo2NVxqqHg==\r\n"
                }
        };
    }

    @Test(dataProvider = "hash_results")
    public void testHashes(HashAlgorithm algorithm, String expected) throws Exception {
        String hash = algorithm.digest(PLAIN_TEXT);
        assertThat(hash, is(not(PLAIN_TEXT)));
        assertThat(hash, is(expected));
    }

    @DataProvider(name = "salted_hash_results")
    public Object[][] getSaltedHashResults() {
        return new Object[][] {
                {
                        HashAlgorithm.MD5,
                        "ojM1dVCqZJRbs64bTh2aoQ==\r\n"
                },
                {
                        HashAlgorithm.RipeMD128,
                        "MijEqc9rcp3JP4qGfZGWEg==\r\n"
                },
                {
                        HashAlgorithm.RipeMD160,
                        "TysBIe5ks0msXhZ+j+hKPqPtXB4=\r\n"
                },
                {
                        HashAlgorithm.RipeMD256,
                        "Oy/KOgfplgiBNQY3UJYk21t5qUzYf6YhuEFLEY5K3Xw=\r\n"
                },
                {
                        HashAlgorithm.RipeMD320,
                        "tePvh70ESVanGz7+Qpzowhr+e7Ecf4sDcwgeBLLoEG9CzHQpJC52tQ==\r\n"
                },
                {
                        HashAlgorithm.SHA1,
                        "jFqlW1quhWu/tuxb4M6z0L4jtpo=\r\n"
                },
                {
                        HashAlgorithm.SHA256,
                        "G9ePlKuEaUYmf4wzv09aoslDUwuOZW54uhyy3B9kzWc=\r\n"
                },
                {
                        HashAlgorithm.SHA384,
                        "mN8Q6KXuuphDsO8A2bzgRBD8RGIW022teTNqs1ibwt8Y+mw9vU3ySlxvwKzCE3uH\r\n"
                },
                {
                        HashAlgorithm.SHA512,
                        "W7AzVt9R+5k79Uc4XzRyh8GWA/MQVeeO1At8gw7aEISit93JkNUYOliupFT3SZ+zStHHYQPkVcWw\r\nNdBtNFNIzQ==\r\n"
                },
                {
                        HashAlgorithm.Tiger,
                        "EFH1FTNIoyI1Que1Ad+HWxRakjxqbT+j\r\n"
                },
                {
                        HashAlgorithm.GOST3411,
                        "cN5TDnl283v0n6xM3jk8eq3uUDJcit3ApQKPdOf+unM=\r\n"
                },
                {
                        HashAlgorithm.Whirlpool,
                        "b1IU2GUiB6YJzXhVhIxX98ILcAvbW3DWaqL1RQOdZntDJW6a35QC7EFkki+eI+kG08Uwb8217RG7\r\nYWNuLQ8qog==\r\n"
                },
                {
                        HashAlgorithm.PBKDF2_128,
                        "QBAcyheyFkwnVp7hEbFChw==\r\n"
                },
                {
                        HashAlgorithm.PBKDF2_256,
                        "QBAcyheyFkwnVp7hEbFCh7uODwjjPbyT3B3qbHdZ6YQ=\r\n"
                }
        };
    }

    @Test(dataProvider = "salted_hash_results")
    public void testSaltedHashes(HashAlgorithm algorithm, String expected) throws Exception {
        String hash = algorithm.digest(PLAIN_TEXT, SALT);
        assertThat(hash, is(not(PLAIN_TEXT)));
        assertThat(hash, is(expected));
    }
}
