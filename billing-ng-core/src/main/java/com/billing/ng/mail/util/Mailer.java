package com.billing.ng.mail.util;

import com.billing.ng.mail.Email;
import com.billing.ng.mail.EmailAttachment;
import com.billing.ng.util.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.activation.DataHandler;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * Mailer
 *
 * @author Brian Cowdery
 * @since 21-Aug-2011
 */
public class Mailer {

    private transient final Logger log = LoggerFactory.getLogger(Mailer.class);

    private Properties properties = null;
    private String hostname = null;

    /**
     * Constructs a new Mailer instance, configuring the mail session using
     * <code>smtp.mail</code> system properties, or properties from the
     * billing-ng.properties configuration file.
     */
    public Mailer() {
        Configuration config = Configuration.getInstance();

        properties = System.getProperties();
        properties.setProperty("mail.transport.protocol", "smtp");

        // auth
        if (config.hasProperty("mail.smtp.auth")) {
            properties.setProperty("mail.smtp.auth", "mail.smtp.auth");
        }

        // hostname
        if (config.hasProperty("mail.smtp.host")) {
            properties.setProperty("mail.smtp.host", config.getProperty("mail.smtp.host"));

        } else {
            try {
                InetAddress localhost = InetAddress.getLocalHost();
                this.hostname = localhost.getCanonicalHostName();
            } catch (UnknownHostException uhe) {
                throw new RuntimeException("An error occurred while retrieving local hostname.", uhe);
            }
        }

        // optional starttls / ssl authentication
        if (config.getPropertyAsBoolean("mail.smtp.starttls.enable")) {
            properties.setProperty("mail.smtp.starttls.enable", "true");
        }

        if (config.getPropertyAsBoolean("mail.smtp.ssl.enable")) {
            properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        }
    }

    /**
     * Sends an e-mail message. If the environment is not a production environment
     * (set "deployment_flags.is_production" to false in billing-ng.properties) the
     * e-mail will be sent to the override address instead of its intended recipients.
     *
     * @param email e-mail message
     * @return number of e-mail messages sent
     * @throws MessagingException un-handled messaging exception
     */
    public int send(Email email) throws MessagingException {
        Session session = Session.getDefaultInstance(properties, new PropertyAuthenticator());
        MimeMessage message = buildMimeMessage(email, session);
        return send(message);
    }

    /**
     * Sends multiple e-mail messages. E-mails are sent in the same manner as
     * {@link #send(com.billing.ng.mail.Email)} regarding production and
     * non-production environments.
     *
     * @param emails e-mail messages
     * @return number of e-mail messages sent
     * @throws MessagingException un-handled messaging exception
     */
    public int send(List<Email> emails) throws MessagingException {
        int sent = 0;
        Session session = Session.getDefaultInstance(properties, new PropertyAuthenticator());

        for(Email email : emails) {
            MimeMessage message = buildMimeMessage(email, session);
            sent += send(message);
        }

        return sent;
    }

    private int send(MimeMessage message) throws MessagingException {
        try {
            log.debug("Attempting to send email to: " + Arrays.toString(message.getAllRecipients()));
            Transport.send(message);
            return 1;

        } catch (SendFailedException sfe) {
            log.warn("Sending Email failed.", sfe);
            return 0;
        }
    }

    private MimeMessage buildMimeMessage(Email email, Session session) throws MessagingException {
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(email.getFrom()));

        MimeMultipart content = new MimeMultipart();

        Configuration config = Configuration.getInstance();

        if (config.getPropertyAsBoolean("deployment_flags.is_production")) {
            // production environment, set recipient addresses normally
            for (String to : email.getTo()) {
                message.addRecipient(MimeMessage.RecipientType.TO, new InternetAddress(to));
            }

            for (String cc : email.getCc()) {
                message.addRecipient(MimeMessage.RecipientType.CC, new InternetAddress(cc));
            }

            for (String bcc : email.getBcc()) {
                message.addRecipient(MimeMessage.RecipientType.BCC, new InternetAddress(bcc));
            }

        } else {
            // non-production environment, send e-mail to the override address
            String blockedHostAddress = config.getProperty("deployment_flags.email_override");
            message.addRecipient(MimeMessage.RecipientType.TO, new InternetAddress(blockedHostAddress));
            content.addBodyPart(getBlockedHostMessageBody(email));
        }

        content.addBodyPart(getMessageBody(email));

        if (email.hasAttachments()) {
            for (BodyPart attachment : getMessageAttachments(email)) {
                content.addBodyPart(attachment);
            }
        }

        message.setContent(content);
        return message;
    }

    private BodyPart getMessageBody(Email email) throws MessagingException {
        BodyPart body = new MimeBodyPart();
        body.setDisposition(Part.INLINE);
        body.setContent(email.getContent(), email.getContentType());
        return body;
    }

    private List<BodyPart> getMessageAttachments(Email email) throws MessagingException {
        List<BodyPart> bodyAttachments = new ArrayList<BodyPart>();

        for (EmailAttachment attachment : email.getAttachments()) {
            BodyPart body = new MimeBodyPart();

            body.setDataHandler(new DataHandler(attachment));
            body.setFileName(attachment.getFilename());
            body.setDisposition(Part.ATTACHMENT);

            bodyAttachments.add(body);
        }

        return bodyAttachments;
    }

    private BodyPart getBlockedHostMessageBody(Email email) throws MessagingException {
        BodyPart body = new MimeBodyPart();
        body.setDisposition(Part.INLINE);

        // ew.
        StringBuilder content = new StringBuilder();
        content.append("<p>Email generated from ").append(this.hostname).append("<br/>");
        content.append("This email was sent from a non-production server.</p>");

        content.append("<p>");
        content.append("<br/>to:      ");
        for (String to : email.getTo()) {
            content.append(to).append(" ");
        }

        content.append("<br/>cc:      ");
        for (String cc : email.getCc()) {
            content.append(cc).append(" ");
        }

        content.append("<br/>bcc:     ");
        for (String bcc : email.getBcc()) {
            content.append(bcc).append(" ");
        }

        content.append("<br/>from:    ").append(email.getFrom());
        content.append("<br/>subject: ").append(email.getSubject());
        content.append("</p>");
        content.append("<p>Original message included below: </p>");

        body.setContent(content.toString(), Email.TEXT_HTML_LATIN1);
        return body;
    }
}

