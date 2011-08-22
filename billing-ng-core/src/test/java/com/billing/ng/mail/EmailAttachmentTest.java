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

package com.billing.ng.mail;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.*;

@Test(groups = { "email", "quick" })
public class EmailAttachmentTest {

    private File file = null;    

    @BeforeClass
    public void setupAttachmentTestFile() {
        File fromRoot = new File("billing-ng-core/src/test/resources/test-attachment.txt");
        File fromCore = new File("src/test/resources/test-attachment.txt");

        file = fromRoot.exists() ? fromRoot : fromCore;
    }

    @Test
    public void testAttachmentFromFile() throws Exception {        
        EmailAttachment attachment = new EmailAttachment(file);

        assertTrue(0 != attachment.getBytes().length);
        assertThat("test-attachment.txt", is(attachment.getFilename()));
        assertThat("text/plain", is(attachment.getContentType()));
    }

    @Test
    public void testAttachmentFromFileInputStream() throws Exception {
        FileInputStream fis = new FileInputStream(file);

        EmailAttachment attachment = new EmailAttachment("test-attachment.txt", "text/plain", fis);
        
        assertTrue(0 != attachment.getBytes().length);
        assertThat("test-attachment.txt", is(attachment.getFilename()));
        assertThat("text/plain", is(attachment.getContentType()));
    }

    @Test
    public void testAttachmentFromBytes() throws Exception {
        FileInputStream fis = new FileInputStream(file);

        byte[] bytes = new byte[fis.available()];
        fis.read(bytes);

        EmailAttachment attachment = new EmailAttachment("test-attachment.txt", "text/plain", bytes);

        assertTrue(0 != attachment.getBytes().length);
        assertEquals(bytes, attachment.getBytes());
        assertThat("test-attachment.txt", is(attachment.getFilename()));
        assertThat("text/plain", is(attachment.getContentType()));
    }

    @Test(expectedExceptions = RuntimeException.class)
    public void testGetOutputStream() throws Exception {
        // there is no output stream for an EmailAttachment, it should always throw an exception.
        new EmailAttachment(file).getOutputStream();
    }
}
