/**
 * SmtpMailService is an implementation of the MailService interface that sends emails using the SMTP protocol.
 *  It provides methods for sending emails with text content and attachments.
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
import jakarta.activation.DataHandler;
import jakarta.activation.FileDataSource;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class SmtpMailService implements MailService {

    private static final Logger log = LoggerFactory.getLogger(SmtpMailService.class);
    private static final int MAX_FILE_SIZE_MB = 5;
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
        try {
            addAttachmentParts(multipart, mailContent.getAttachments());
        } catch (EmailException e) {
            throw new RuntimeException(e);
        }
        message.setContent(multipart);

        return message;
    }
    private MimeMessage setEmailHeader(MimeMessage message, List<String> to,
                                       List<String> cc,List<String> bcc, String subject){
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
        return message;
    }

    private Address[] createInternetAddresses(List<String> addresses)
            throws AddressException {
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
                validateFile(file);

                MimeBodyPart filePart = new MimeBodyPart();
                filePart.setDataHandler(new DataHandler(new FileDataSource(file)));
                filePart.setFileName(file.getName());
                multipart.addBodyPart(filePart);
            }
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Validates the size and type of the given file.
     *
     * @param file The file to be validated.
     * @throws EmailException If the file fails size or type validation.
     */
    private void validateFile(File file) throws EmailException {
        if (file.length() > MAX_FILE_SIZE_MB * 1024 * 1024) {
            throw new EmailException("File size exceeds the limit of " + MAX_FILE_SIZE_MB + "MB: "
                    + file.getName() + " Length of " + file.length() / (1024 * 1024) + "MB");
        }

        if (!file.exists()) {
            throw new EmailException("File does not exist: " + file.getAbsolutePath());
        }

        String fileName = file.getName();
        String fileExtension = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();

        List<String> blockedFileTypes = Arrays.asList(
                "ade", "adp", "apk", "appx", "appxbundle", "bat", "cab", "chm", "cmd", "com", "cpl",
                // ... (add other blocked file types if needed)
                "xll");
        if (blockedFileTypes.contains(fileExtension)) {
            throw new EmailException("Unsupported file type: " + fileExtension + " - " + fileName);
        }
    }
}