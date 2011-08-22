package com.billing.ng.mail;

import javax.activation.DataSource;
import javax.activation.FileTypeMap;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * EmailAttachment
 *
 * @author Brian Cowdery
 * @since 21-Aug-2011
 */
public class EmailAttachment implements DataSource {

    private String filename;
    private String contentType;
    private byte[] bytes;

    public EmailAttachment() {        
    }

    public EmailAttachment(File file) {
        this.filename = file.getName();
        this.contentType = FileTypeMap.getDefaultFileTypeMap().getContentType(file);
        setBytes(file);
    }

    public EmailAttachment(String filename, String contentType, FileInputStream stream) {
        this.filename = filename;
        this.contentType = contentType;
        setBytes(stream);
    }

    public EmailAttachment(String filename, String contentType, byte[] bytes) {
        this.filename = filename;
        this.contentType = contentType;
        this.bytes = bytes;
    }

    public String getName() {
        return filename;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public void setBytes(File file) {
        try {
            FileInputStream stream = new FileInputStream(file);
            setBytes(stream);
        } catch (FileNotFoundException fnfe) {
            throw new IllegalArgumentException("File " + file.getName() + " was not found.", fnfe);
        }
    }    

    public void setBytes(FileInputStream stream) {
        try {
            bytes = new byte[stream.available()];
            stream.read(bytes);
            stream.close();
        } catch (IOException ioe) {
            throw new IllegalArgumentException("Could not read from file.", ioe);
        }
    }

    public long getSize() {
        return bytes.length;
    }

    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(bytes, 0, bytes.length);
    }
    
    public OutputStream getOutputStream() throws IOException {
        throw new RuntimeException("No OutputStream available for " + this.getClass());
    }

    @Override
    public String toString() {
        return "filename: " + filename + " content-type: " + contentType + " length: " + getSize() + " bytes";
    }
}

