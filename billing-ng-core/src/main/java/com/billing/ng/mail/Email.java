package com.billing.ng.mail;

import com.billing.ng.entities.BaseEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Email
 *
 * @author Brian Cowdery
 * @since 21-Aug-2011
 */
public class Email extends BaseEntity {

    public static String TEXT_HTML_LATIN1 = "text/html; charset=ISO-8859-1";
    public static String TEXT_PLAIN_LATIN1 = "text/plain; charset=ISO-8859-1";
    public static String TEXT_HTML_UTF8 = "text/html; charset=UTF-8";
    public static String TEXT_PLAIN_UTF8 = "text/plain; charset=UTF-8";

    private String from;
    private String recipients;
    private List<String> to = new ArrayList<String>();
    private List<String> cc = new ArrayList<String>();
    private List<String> bcc = new ArrayList<String>();

    private String subject = "<no subject>";
    private String content;
    private String contentType;

    private List<EmailAttachment> attachments = new ArrayList<EmailAttachment>();

    public Email() {
        this.contentType = TEXT_HTML_LATIN1;
    }

    public Email(String from, List<String> to, String subject, String content, String contentType) {
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.content = content;
        this.contentType = contentType;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getRecipients() {
        flattenRecipients();
        return recipients;
    }

    public void setRecipients(String recipients) {
    	this.recipients = recipients;
        expandRecipients();
    }

    public void flattenRecipients() {
        StringBuilder builder = new StringBuilder();

        for(String to : this.to) {
            builder.append("to:").append(to).append(";");
        }
        for(String cc : this.cc) {
            builder.append("cc:").append(cc).append(";");
        }
        for(String bcc : this.bcc) {
            builder.append("bcc:").append(bcc).append(";");
        }

        setRecipients(builder.toString());
    }

    public void expandRecipients() {
        this.to.clear();
        this.cc.clear();
        this.bcc.clear();

        if (null != recipients) {
            String[] recipient = recipients.split(";");
            for (int i = 0; i < recipient.length; i++) {
                String[] tokens = recipient[i].split(":");

                if (tokens.length == 2) {
                    // split recipient contains 2 elements [type, address]

                    if (tokens[0].equals("to")) {
                        this.to.add(tokens[1]);
                    } else if(tokens[0].equals("cc")) {
                        this.cc.add(tokens[1]);
                    } else if(tokens[0].equals("bcc")) {
                        this.bcc.add(tokens[1]);
                    }
                }
            }
        }
    }

    public List<String> getTo() {
        return to;
    }

    public void setTo(List<String> to) {
        this.to = to;
    }

    public void setTo(String to) {
        this.to.clear();
        addTo(to);
    }

    public void addTo(String to) {        
        this.to.add(to);
        flattenRecipients();
    }

    public List<String> getCc() {
        return cc;
    }

    public void setCc(List<String> cc) {
        this.cc = cc;
    }

    public void setCc(String cc) {
        this.cc.clear();
        addCc(cc);
    }

    public void addCc(String cc) {
        this.cc.add(cc);
        flattenRecipients();
    }

    public List<String> getBcc() {
        return bcc;
    }

    public void setBcc(List<String> bcc) {
        this.bcc = bcc;
    }

    public void setBcc(String bcc) {
        this.bcc.clear();
        addBcc(bcc);
    }

    public void addBcc(String bcc) {
        this.bcc.add(bcc);
        flattenRecipients();
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public List<EmailAttachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<EmailAttachment> attachments) {
        this.attachments = attachments;
    }

    public void addAttachment(EmailAttachment attachment) {
        this.attachments.add(attachment);
    }

    public boolean hasAttachments() {
        return !(null == attachments || attachments.isEmpty());
    }

    @Override
    public String toString() {
        return "Email{" +
               "from='" + from + '\'' +
               ", recipients='" + recipients + '\'' +
               ", subject='" + subject + '\'' +
               ", content='" + content + '\'' +
               '}';
    }
}

