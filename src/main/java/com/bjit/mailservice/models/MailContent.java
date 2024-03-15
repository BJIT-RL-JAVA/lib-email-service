package com.bjit.mailservice.models;

import com.bjit.mailservice.services.MailValidation;
import com.bjit.mailservice.utils.ValidAttachment;
import com.bjit.mailservice.utils.ValidAttachmentSize;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.groups.Default;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Represents the content of an email message.
 *
 * @author Mallika Dey
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MailContent implements MailValidation {
    @Email
    private String from;

    @NotEmpty(message = "Receiver is required")
    private ArrayList<@NotNull String> to;
    private ArrayList<@NotNull String> cc;
    private ArrayList<@NotNull String> bcc;
    private String subject;
    private String body;
    private File htmlTemplate;
    private HashMap<String, Object> objectMap;

    @ValidAttachmentSize(groups = {Default.class, ValidAttachment.class})
    private ArrayList<File> attachments;
    // Custom setter for htmlTemplate with validation
    public void setHtmlTemplate(File htmlTemplate) {
        if (validateHtmlTemplate(htmlTemplate)) {
            this.htmlTemplate = htmlTemplate;
        }
        else {
            throw new IllegalArgumentException("Invalid File! Please provide an HTML file only.");
        }
    }
}
