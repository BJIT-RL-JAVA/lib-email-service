package com.bjit.mailservice.models;

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
    private File htmlTemplate;

    @ValidAttachmentSize(groups = {Default.class, ValidAttachment.class})
    private ArrayList<File> attachments;
    // Custom setter for htmlTemplate with validation
    public void setHtmlTemplate(File htmlTemplate) {
        if (htmlTemplate != null && htmlTemplate.isFile() && htmlTemplate.getName().endsWith(".html")) {
            // Check file size
            long fileSizeInBytes = htmlTemplate.length();
            long fileSizeInMB = fileSizeInBytes / (1024 * 1024); // Convert bytes to MB
            if (fileSizeInMB <= 5) {
                this.htmlTemplate = htmlTemplate;
            } else {
                throw new IllegalArgumentException("File size exceeds the maximum limit of 5MB.");
            }
        } else {
            throw new IllegalArgumentException("Invalid File! Please provide an HTML file only.");
        }
    }
}
