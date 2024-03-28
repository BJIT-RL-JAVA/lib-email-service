package com.bjit.mailservice.models;

import com.bjit.mailservice.validators.ValidAttachmentSize;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.groups.Default;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
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
    @Email
    private String from;

    @NotEmpty(message = "Receiver is required")
    private ArrayList<@NotNull String> to;
    private ArrayList<@NotNull String> cc;
    private ArrayList<@NotNull String> bcc;
    private String subject;
    private String body;
    private String htmlTemplate;

    @ValidAttachmentSize(groups = {Default.class})
    private ArrayList<File> attachments;
}
