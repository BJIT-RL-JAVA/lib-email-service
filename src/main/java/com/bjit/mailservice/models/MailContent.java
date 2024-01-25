package com.bjit.mailservice.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

/**
 * Represents the content of an email message.
 *
 * @author Mallika Dey
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MailContent {
    private String from;
    private ArrayList<String> to;
    private ArrayList<String> cc;
    private ArrayList<String> bcc;
    private String subject;
    private String body;
    private ArrayList<String> attachments;
}
