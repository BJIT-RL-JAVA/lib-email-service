package com.bjit.mailservice.services.impl;

import com.bjit.mailservice.services.MailService;
import com.bjit.mailservice.validators.MailValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
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
import org.springframework.util.ObjectUtils;
import org.springframework.util.StreamUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Base64;

/**
 * Implementation of the MailService interface for sending emails using SendGrid.
 * This class handles the integration with SendGrid and provides the logic for sending email messages.
 *
 * @author Mallika Dey
 */
public class SendGridMailService implements MailService, MailValidation {
    private static final Logger LOGGER = LoggerFactory.getLogger(SendGridMailService.class);

    private SendGrid sendGrid;

    /**
     * Constructs a SendGridMailService object with the provided SendGrid instance.
     *
     * @param sendGrid the SendGrid instance used for sending emails
     */
    public SendGridMailService(SendGrid sendGrid) {
        this.sendGrid = sendGrid;
    }

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

        return mailSendUsingSendGrid(mailContent, mail);
    }

    /**
     * Sends an HTML template-based email using the SendGrid with the provided MailContent.
     * If a specific HTML template is not provided in the MailContent, the default "welcome.html" template is loaded.
     * The dynamic content specified in the MailContent is replaced in the template before sending the email.
     *
     * @param mailContent The MailContent object containing information about the email to be sent,
     *                    including the HTML template and dynamic content.
     * @return A unique message identifier for tracking the sent email.
     * @throws MessagingException If an error occurs during the email sending process.
     */
    @Override
    public String sendHtmlTemplateMail(MailContent mailContent) throws MessagingException {
        Mail mail = new Mail();
        mail.setFrom(new Email(mailContent.getFrom()));

        Personalization personalization = new Personalization();
        for (String toEmail : mailContent.getTo()) {
            personalization.addTo(new Email(toEmail));
        }

        String htmlContent = (mailContent.getHtmlTemplate() == null) ?
                loadHtmlTemplate("welcome.html") : mailContent.getHtmlTemplate();

        htmlContent = htmlContent.replace("[Dynamic Content]", mailContent.getBody());
        mail.addPersonalization(personalization);
        mail.setSubject(mailContent.getSubject());
        mail.addContent(new Content("text/plain", mailContent.getBody()));
        mail.addContent(new Content("text/html", htmlContent));

        return mailSendUsingSendGrid(mailContent, mail);
    }

    private String mailSendUsingSendGrid(MailContent mailContent, Mail mail) {
        if (!ObjectUtils.isEmpty(mailContent.getAttachments())) {
            for (File attachment : mailContent.getAttachments()) {

                try {
                    checkFileCompatibility(attachment);
                    byte[] fileContent = Files.readAllBytes(attachment.toPath());
                    Attachments sendGridAttachment = new Attachments();
                    sendGridAttachment.setContent(new String(Base64.getEncoder().encode(fileContent)));
                    sendGridAttachment.setFilename(attachment.getName());
                    mail.addAttachments(sendGridAttachment);
                } catch (IOException ex) {
                    LOGGER.error("IO exception occured ", ex);
                    return "mail sending failed";
                }
            }
        }

        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sendGrid.api(request);
            LOGGER.info("Calling sendgrid api {} {}", response.getStatusCode(), response.getBody());
        } catch (IOException ex) {
            LOGGER.error("IO exception occur in request ", ex);
            return "mail sending failed";
        }
        return "mail sent successfully";
    }

    private String loadHtmlTemplate(String templateName) {
        try {
            ClassPathResource resource = new ClassPathResource("templates/" + templateName);
            byte[] templateBytes = StreamUtils.copyToByteArray(resource.getInputStream());
            return new String(templateBytes, StandardCharsets.UTF_8);
        } catch (IOException e) {
            LOGGER.error("Error loading HTML template file: {}", templateName, e);
            throw new RuntimeException("Error loading HTML template file", e);
        }
    }
}
