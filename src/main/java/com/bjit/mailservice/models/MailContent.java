package com.bjit.mailservice.models;

import lombok.Data;

import java.io.File;
import java.util.ArrayList;

/**
 * Represents the content of an email message.
 *
 * @author Mallika Dey
 */
@Data
public class MailContent {
    private String from;
    private ArrayList<String> to;
    private ArrayList<String> cc;
    private ArrayList<String> bcc;
    private String subject;
    private String body;
    private ArrayList<File> attachments;
}
