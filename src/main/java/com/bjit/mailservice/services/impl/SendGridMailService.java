package com.bjit.mailservice.services.impl;

import com.bjit.mailservice.constants.MessageConstant;
import com.bjit.mailservice.services.LoadMailTemplate;
import com.bjit.mailservice.services.MailService;
import com.bjit.mailservice.validators.MailValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;

/**
 * Implementation of the MailService interface for sending emails using SendGrid.
 * This class handles the integration with SendGrid and provides the logic for sending email messages.
 *
 * @author Mallika Dey
 */
public class SendGridMailService implements MailService, MailValidation, LoadMailTemplate {
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
        if (!validateHtmlTemplate(mailContent.getHtmlTemplate())) {
            throw new IllegalArgumentException(MessageConstant.html_file_type_mismatched);
        }
        String htmlContent = loadHtmlTemplate(mailContent.getHtmlTemplate(), mailContent.getObjectMap());

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
                    LOGGER.error(MessageConstant.io_exception, ex);
                    return MessageConstant.sendMail_error;
                }
            }
        }
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sendGrid.api(request);
            LOGGER.info(MessageConstant.sandgrid_api_call, response.getStatusCode(), response.getBody());
        } catch (IOException ex) {
            LOGGER.error(MessageConstant.io_exception, ex);
            return MessageConstant.sendMail_error;
        }
        return MessageConstant.sendMail_success;
    }
}
