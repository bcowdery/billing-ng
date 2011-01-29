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

import com.billing.ng.crypto.key.KeyPair;
import org.bouncycastle.asn1.cryptopro.CryptoProObjectIdentifiers;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import javax.swing.text.PlainDocument;
import java.math.BigInteger;
import java.security.Key;
import java.security.Security;
import java.security.spec.AlgorithmParameterSpec;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * CipherAlgorithmTest
 *
 * @author Brian Cowdery
 * @since 13/01/11
 */
@Test(groups = { "crypto", "quick"})
public class CipherAlgorithmTest {

    private static String PLAIN_TEXT = "Plain Text String! #123456789 @test.com";

    @DataProvider(name = "cipher_algorithms")
    public Object[][] getAllCipherAlgorithms() {
        Object[][] data = new Object[CipherAlgorithm.values().length][1];

        for (int i = 0; i < CipherAlgorithm.values().length; i++)
            data[i] = new Object[] { CipherAlgorithm.values()[i] };

        return data;
    }

    @Test(dataProvider = "cipher_algorithms")
    public void testCipherAlgorithm(CipherAlgorithm algorithm) throws Exception {
        KeyPair keyPair = algorithm.generateKey();

        String encrypted = algorithm.encrypt(keyPair.getEncryptionKey(), PLAIN_TEXT);
        assertThat(encrypted, is(not(PLAIN_TEXT)));

        String decrypted = algorithm.decrypt(keyPair.getDecryptionKey(), encrypted);
        assertThat(decrypted, is(PLAIN_TEXT));
    }
}
