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

import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.*;

@Test(groups = { "email", "quick" })
public class EmailTest {

    @Test
    public void testDefaultContentType() {
        Email email = new Email();

        assertThat(email.getContentType(), is(Email.TEXT_HTML_LATIN1));
        assertThat(email.getContentType(), is("text/html; charset=ISO-8859-1"));
    }

    @Test
    public void testSetToAddress() {
        // should clear any existing addresses when "set"
        Email email = new Email();
        email.addTo("junk@test.com");
        email.setTo("test@billing-ng.com");

        assertThat(email.getTo().size(), is(1));
        assertThat(email.getTo().get(0), is("test@billing-ng.com"));
    }

    @Test
    public void testSetCcAddress() {
        // should clear any existing addresses when "set"
        Email email = new Email();
        email.addCc("junk@test.com");
        email.setCc("test@billing-ng.com");

        assertThat(email.getCc().size(), is(1));
        assertThat(email.getCc().get(0), is("test@billing-ng.com"));
    }

    @Test
    public void testSetBccAddress() {
        // should clear any existing addresses when "set"
        Email email = new Email();
        email.addBcc("junk@test.com");
        email.setBcc("test@billing-ng.com");

        assertThat(email.getBcc().size(), is(1));
        assertThat(email.getBcc().get(0), is("test@billing-ng.com"));
    }

    @Test
    public void testHasAttachments() {
        Email email = new Email();
        assertFalse(email.hasAttachments());

        email.addAttachment(new EmailAttachment());
        assertTrue(email.hasAttachments());
    }

    @Test
    public void testGetRecipients() {
        Email email = new Email();
        email.addTo("bcowdery@billing-ng.com");

        String recipients = email.getRecipients();
        assertThat(recipients, is("to:bcowdery@billing-ng.com;"));

        email.addCc("test@billing-ng.com");
        recipients = email.getRecipients();
        assertThat(recipients, is("to:bcowdery@billing-ng.com;cc:test@billing-ng.com;"));

        email.addBcc("numptybusbang@billing-ng.com");
        recipients = email.getRecipients();
        assertThat(recipients, is("to:bcowdery@billing-ng.com;cc:test@billing-ng.com;bcc:numptybusbang@billing-ng.com;"));
    }

    @Test
    public void testSetRecipients() {
        Email email = new Email();
        
        email.setRecipients("to:bcowdery@billing-ng.com");
        assertThat(email.getTo().get(0), is("bcowdery@billing-ng.com"));

        email.setRecipients("to:bcowdery@billing-ng.com;cc:test@billing-ng.com;");
        assertThat(email.getTo().get(0), is("bcowdery@billing-ng.com"));
        assertThat(email.getCc().get(0), is("test@billing-ng.com"));

        email.setRecipients("to:bcowdery@billing-ng.com;cc:test@billing-ng.com;bcc:numptybusbang@billing-ng.com;");
        assertThat(email.getTo().get(0), is("bcowdery@billing-ng.com"));
        assertThat(email.getCc().get(0), is("test@billing-ng.com"));
        assertThat(email.getBcc().get(0), is("numptybusbang@billing-ng.com"));
    }
}
