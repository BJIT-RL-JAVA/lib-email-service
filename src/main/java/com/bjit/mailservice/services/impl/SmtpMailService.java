/**
 * SmtpMailService is an implementation of the MailService interface that sends emails using the SMTP protocol.
 * It provides methods for sending emails with text content and attachments.
 * <p>
 * Created by Mohammad Khalid Hasan|| BJIT-R&D
 * Since: 3/27/2024
 * Time: 3:38 PM
 * Project Name: lib-email-service
 * Version: 1.0
 */
package com.bjit.mailservice.services.impl;

import com.bjit.mailservice.constants.MessageConstant;
import com.bjit.mailservice.exception.EmailException;
import com.bjit.mailservice.models.MailContent;
import com.bjit.mailservice.services.LoadMailTemplate;
import com.bjit.mailservice.services.MailService;
import com.bjit.mailservice.validators.MailValidation;
import jakarta.activation.DataHandler;
import jakarta.activation.FileDataSource;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

public class SmtpMailService implements MailService, MailValidation, LoadMailTemplate {
    private static final Logger log = LoggerFactory.getLogger(SmtpMailService.class);
    /**
     * Sends an email with the specified mail content.
     *
     * @param mailContent The content of the email to be sent.
     * @return A message indicating the result of the email sending operation.
     * It could be a success message or an error message.
     * @throws MessagingException If an error occurs during the email sending process.
     */
    @Override
    public String sendMail(MailContent mailContent) throws MessagingException {
        try {

            Session session = createSmtpSession();
            MimeMessage message = createMimeMessage(session, mailContent);
            Transport.send(message);
            return MessageConstant.sendMail_success;
        } catch (MessagingException | EmailException e) {
            log.error(MessageConstant.sendMail_error, e);
            throw e;
        }
    }

    /**
     * Sends an HTML templated email with the specified mail content.
     *
     * @param mailContent The content of the email to be sent.
     * @return A message indicating the result of the email sending operation.
     * It could be a success message or an error message.
     * @throws MessagingException If an error occurs during the email sending process.
     */
    @Override
    public String sendHtmlTemplateMail(MailContent mailContent) throws MessagingException {
        try {
            Session session = createSmtpSession();
            MimeMessage message = createTemplateMimeMessage(session, mailContent);
            Transport.send(message);
            return MessageConstant.successfully_sending_html_template_email;
        } catch (MessagingException e) {
            log.error(MessageConstant.error_sending_html_template_email, e);
            throw e;
        }
    }

    /**
     * Creates an SMTP session with authentication and TLS enabled.
     * <p>
     * Configures the server host, port, and  enables authentication,
     * TLS for secure email transmission as well as new Authenticator
     * instance to handle the SMTP authentication process
     *
     * @return A new Session object configured with SMTP settings.
     */
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
     * @param session     The SMTP session.
     * @param mailContent The content of the email.
     * @return A MimeMessage ready to be sent.
     * @throws MessagingException If an error occurs during message creation.
     */
    private MimeMessage createMimeMessage(Session session, MailContent mailContent)
            throws MessagingException {
        MimeMessage message = new MimeMessage(session);
        setEmailHeader(message, mailContent.getTo(), mailContent.getCc(),
                mailContent.getBcc(), mailContent.getSubject());
        Multipart multipart = new MimeMultipart();
        addTextPart(multipart, mailContent.getBody());
        if (!mailContent.getAttachments().isEmpty()) {
            try {
                addAttachmentParts(multipart, mailContent.getAttachments());
            } catch (EmailException e) {
                log.error(MessageConstant.process_attachment_error);
                throw e;
            }
        }
        message.setContent(multipart);
        return message;
    }

    private void setEmailHeader(MimeMessage message, List<String> to,List<String> cc, List<String> bcc, String subject) {
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
    private void addAttachmentParts(Multipart multipart, List<File> attachments) {
        try {
            for (File file : attachments) {
                checkFileCompatibility(file);

                MimeBodyPart filePart = new MimeBodyPart();
                filePart.setDataHandler(new DataHandler(new FileDataSource(file)));
                filePart.setFileName(file.getName());
                multipart.addBodyPart(filePart);
            }
        } catch (MessagingException | IOException e) {
            log.error(MessageConstant.process_attachment_error, e);
            throw new EmailException(e.getMessage());
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