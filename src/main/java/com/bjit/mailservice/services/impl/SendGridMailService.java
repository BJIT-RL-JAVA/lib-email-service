package com.bjit.mailservice.services.impl;

import com.bjit.mailservice.services.MailService;
import com.bjit.mailservice.services.MailValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bjit.mailservice.models.MailContent;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Attachments;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import jakarta.mail.MessagingException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;

/**
 * @author Mallika Dey
 */
@Service
public class SendGridMailService implements MailService, MailValidation {
    private static final Logger logger = LoggerFactory.getLogger(SendGridMailService.class);

    @Autowired
    private SendGrid sendGrid;

    /**
     * Sends an email with the specified mail content.
     *
     * @param mailContent The content of the email to be sent.
     * @return A message indicating the result of the email sending operation.
     * It could be a success message or an error message.
     */
    @Override
    public String sendMail(MailContent mailContent) throws MessagingException {
        Mail mail = new Mail();
        mail.setFrom(new Email(mailContent.getFrom()));

        Personalization personalization = new Personalization();
        for (String toEmail : mailContent.getTo()) {
            personalization.addTo(new Email(toEmail));
        }

        mail.addPersonalization(personalization);
        mail.setSubject(mailContent.getSubject());
        mail.addContent(new Content("text/plain", mailContent.getBody()));

        try {
            for (File attachment : mailContent.getAttachments()) {
                byte[] fileContent = Files.readAllBytes(attachment.toPath());
                Attachments sendGridAttachment = new Attachments();
                sendGridAttachment.setContent(new String(Base64.getEncoder().encode(fileContent)));
                sendGridAttachment.setFilename(attachment.getName());
                mail.addAttachments(sendGridAttachment);
            }
        } catch (IOException ex) {
            logger.error("IO exception occured ", ex);
            throw new MessagingException("Failed to send mail ", ex);
        }

        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sendGrid.api(request);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());
        } catch (IOException ex) {
            logger.error("IO exception occur in request ", ex);
            throw new MessagingException("Failed to send mail ", ex);
        }
        return "mail sent successfully";
    }

    @Override
    public void checkFileCompatibility(File file) {
        MailValidation.super.checkFileCompatibility(file);
    }
}
