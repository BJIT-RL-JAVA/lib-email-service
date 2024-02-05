package com.bjit.mailservice.models;

import lombok.Data;

import java.io.File;
import java.util.List;

/**
 * Represents the content of an email message.
 *
 * @author Mallika Dey
 */
@Data
public class MailContent {
    private String from;
    private List<String> to;
    private List<String> cc;
    private List<String> bcc;
    private String subject;
    private String body;
    private List<File> attachments;
}
