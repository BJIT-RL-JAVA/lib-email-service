/**
 * SmtpMailService is an implementation of the MailService interface that sends emails using the SMTP protocol.
 * It provides methods for sending emails with text content and attachments.
 *
 * @author Khalid|| BJIT-R&D
 * @since: 1/26/2024
 * Time: 3:38 PM
 * Project Name: lib-email-service
 * @version 1.0
 */

package com.bjit.mailservice.services.impl;

import com.bjit.mailservice.exception.EmailException;
import com.bjit.mailservice.models.MailContent;
import com.bjit.mailservice.services.MailService;
import com.bjit.mailservice.services.MailValidation;
import jakarta.activation.DataHandler;
import jakarta.activation.FileDataSource;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StreamUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.Properties;
public class SmtpMailService implements MailService, MailValidation {

    private static final Logger log = LoggerFactory.getLogger(SmtpMailService.class);

    /**
     * Sends an email with the specified mail content.
     *
     * @param mailContent The content of the email to be sent.
     * @return A message indicating the result of the email sending operation.
     *         It could be a success message or an error message.
     * @throws MessagingException If an error occurs during the email sending process.
     */
    @Override
    public String sendMail(MailContent mailContent) throws MessagingException {
        try {

            Session session = createSmtpSession();
            MimeMessage message = createMimeMessage(session, mailContent);
            Transport.send(message);
            return "Mail has been sent successfully.";
        } catch (MessagingException e) {
            log.error("Error sending email", e);
            throw e;
        }
    }

    @Override
    public String sendHtmlTemplateMail(MailContent mailContent) throws MessagingException {
        try {
            Session session = createSmtpSession();
            MimeMessage message = createTemplateMimeMessage(session, mailContent);
            Transport.send(message);
            return "HTML template mail has been sent successfully.";
        } catch (MessagingException e) {
            log.error("Error sending HTML template email", e);
            throw e;
        }
    }
    private Session createSmtpSession() {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", true);
        properties.put("mail.smtp.starttls.enable", true);
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.host", "smtp.gmail.com");

        return Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication
                        ("khalid.hasan.bjit", "zlpr ncix ceil ntlv");
            }
        });
    }
    /**
     * Creates a MimeMessage for the specified mail content.
     *
     * @param session      The SMTP session.
     * @param mailContent  The content of the email.
     * @return A MimeMessage ready to be sent.
     * @throws MessagingException If an error occurs during message creation.
     */
    private MimeMessage createMimeMessage(Session session, MailContent mailContent)
            throws MessagingException {
        MimeMessage message = new MimeMessage(session);
        setEmailHeader(message, mailContent.getTo(), mailContent.getCc(), mailContent.getBcc(), mailContent.getSubject());
        Multipart multipart = new MimeMultipart();
        addTextPart(multipart, mailContent.getBody());
        if(!mailContent.getAttachments().isEmpty())
        {
            try {
                addAttachmentParts(multipart, mailContent.getAttachments());
            } catch (EmailException e) {
                throw new RuntimeException(e);
            }
        }
        message.setContent(multipart);
        return message;
    }
    private void setEmailHeader(MimeMessage message, List<String> to,
                                List<String> cc, List<String> bcc, String subject) {
        try {
            message.setRecipients(Message.RecipientType.TO,
                    createInternetAddresses(to));
            message.setRecipients(Message.RecipientType.CC,
                    createInternetAddresses(cc));
            message.setRecipients(Message.RecipientType.BCC,
                    createInternetAddresses(bcc));

            message.setSubject(subject);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
    private Address[] createInternetAddresses(List<String> addresses) throws AddressException {
        if (addresses == null) {
            return new Address[0]; // Return an empty array if the addresses list is null
        }

        Address[] internetAddresses = new Address[addresses.size()];
        for (int i = 0; i < addresses.size(); i++) {
            internetAddresses[i] = new InternetAddress(addresses.get(i));
        }
        return internetAddresses;
    }
    /**
     * Adds a text part to the specified multipart content.
     *
     * @param multipart The multipart content.
     * @param body      The body text of the email.
     * @throws MessagingException If an error occurs during part creation.
     */
    private void addTextPart(Multipart multipart, String body)
            throws MessagingException {
        MimeBodyPart textPart = new MimeBodyPart();
        textPart.setText(body);
        multipart.addBodyPart(textPart);
    }

    /**
     * Validates the size and type of the given file.
     *
     * @param attachments The file to be validated.
     * @throws EmailException If the file fails size or type validation.
     */
    private void addAttachmentParts(Multipart multipart, List<File> attachments) throws EmailException {
        try {
            for (File file : attachments) {
                checkFileCompatibility(file);

                MimeBodyPart filePart = new MimeBodyPart();
                filePart.setDataHandler(new DataHandler(new FileDataSource(file)));
                filePart.setFileName(file.getName());
                multipart.addBodyPart(filePart);
            }
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void checkFileCompatibility(File file) {
        MailValidation.super.checkFileCompatibility(file);
    }
    /**
     * Loads an HTML template file and processes it with dynamic content if provided otherwise it processes the default template
     *
     * @param htmlTemplateFile The file to be validated.
     * @throws EmailException If the file fails size or type validation.
     */
    private String loadHtmlTemplate(File htmlTemplateFile, Map<String, Object> objectMap) {
        try {
            byte[] templateBytes;
            if (htmlTemplateFile != null && htmlTemplateFile.exists() && !objectMap.isEmpty()) {
                // Process HTML template with dynamic content
                return processHtmlTemplate(htmlTemplateFile, objectMap);
            } else if (htmlTemplateFile != null && htmlTemplateFile.exists()) {
                // Load HTML content from the custom template file without dynamic content
                templateBytes = Files.readAllBytes(htmlTemplateFile.toPath());
            } else {
                // Load HTML content from the default template file
                String templateName = "welcome.html";
                // Load HTML content from the template file in the resources/templates directory
                ClassPathResource resource = new ClassPathResource("templates/" + templateName);
                templateBytes = StreamUtils.copyToByteArray(resource.getInputStream());
            }
            return new String(templateBytes, StandardCharsets.UTF_8);
        } catch (IOException  e) {
            log.error("Error loading HTML template file", e);
            throw new RuntimeException("Error loading HTML template file", e);
        }
    }
    private String processHtmlTemplate(File htmlTemplateFile, Map<String, Object> objectMap) {
        try {
            TemplateEngine templateEngine = new TemplateEngine();
            String template = Files.readString(htmlTemplateFile.toPath(), StandardCharsets.UTF_8);
            // Create Thymeleaf context and add objectMap as a variable
            Context context = new Context();
            context.setVariables(objectMap);
            // Process the Thymeleaf template with dynamic values
            return templateEngine.process(template, context);
        } catch (IOException e) {
            log.error("Error loading HTML template file", e);
            throw new RuntimeException("Error loading HTML template file", e);
        }
    }
    private MimeMessage createTemplateMimeMessage(Session session, MailContent mailContent) throws MessagingException {
        MimeMessage message = new MimeMessage(session);
        setEmailHeader(message, mailContent.getTo(), mailContent.getCc(), mailContent.getBcc(), mailContent.getSubject());

        // Load HTML content from template and process with dynamic values
        String htmlContent = loadHtmlTemplate(mailContent.getHtmlTemplate(), mailContent.getObjectMap());

        // Create a multipart to hold both HTML content and other parts (if any)
        MimeMultipart multipart = new MimeMultipart();

        // Create a MimeBodyPart for the HTML content
        MimeBodyPart htmlPart = new MimeBodyPart();
        htmlPart.setContent(htmlContent, "text/html");
        addTextPart(multipart, mailContent.getBody());
        multipart.addBodyPart(htmlPart);

        // Add other parts (attachments, text, etc.) to the multipart
        if (!ObjectUtils.isEmpty(mailContent.getAttachments())) {
            try {
                addAttachmentParts(multipart, mailContent.getAttachments());
            } catch (EmailException e) {
                throw new RuntimeException(e);
            }
        }

        // Set the multipart as the content of the MimeMessage
        message.setContent(multipart);

        return message;
    }
}