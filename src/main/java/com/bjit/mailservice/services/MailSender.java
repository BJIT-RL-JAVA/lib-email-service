package com.bjit.mailservice.services;

import com.bjit.mailservice.models.MailContent;
import jakarta.mail.MessagingException;
import jakarta.validation.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

/**
 * @author Mallika Dey
 */
public class MailSender {
    private final MailService mailService;

    public MailSender(MailServiceFactory mailServiceFactory) {
        this.mailService = mailServiceFactory.createMailService();
    }

    public void sendMail() throws MessagingException {
        MailContent mailContent = new MailContent();
        mailContent.setFrom("khalid.hasan@bjitgroup.com");
        mailContent.setTo(new ArrayList<>(Arrays.asList("mallika.dey@bjitgroup.com", "2018-2-60-110@std.ewubd.edu")));
        mailContent.setCc(new ArrayList<>(Arrays.asList("khalidhasan374@gmail.com", "connectkhalid404@gmail.com")));
        mailContent.setBcc(new ArrayList<>(Arrays.asList("khalidhasankibria@gmail.com", "connectkhalid404@gmail.com")));
        mailContent.setSubject("Testing mail");
        mailContent.setBody("Message body");
        mailContent.setAttachments(
                new ArrayList<>(Arrays.asList(
                        new File("C:\\Users\\BJIT\\Downloads\\home.jpeg"))));
        validateMailContent(mailContent);

        mailService.sendMail(mailContent);
    }

    public void sendMailWithHTMLTemplate() throws MessagingException {
        MailContent mailContent = new MailContent();
        mailContent.setFrom("khalid.hasan@bjitgroup.com");
        mailContent.setTo(new ArrayList<>(Arrays.asList("mallika.dey@bjitgroup.com")));
        mailContent.setCc(new ArrayList<>(Arrays.asList("khalidhasan374@gmail.com", "connectkhalid404@gmail.com")));
        mailContent.setBcc(new ArrayList(Arrays.asList("khalidhasankibria@gmail.com", "connectkhalid404@gmail.com")));
        mailContent.setSubject("Testing mail");
        // HTML-formatted email body
        String htmlBody = "<html><body>" +
                "<p>Welcome to BJIT! We are thrilled to have you join our team and embark on this journey together. At BJIT, we strive for excellence, innovation, and collaboration, and we're confident that your unique skills and talents will contribute significantly to our shared success.</p>" +
                "<p>As you settle into your role, don't hesitate to reach out to your colleagues or managers for guidance and support. We believe in fostering a supportive and inclusive work environment where everyone has the opportunity to thrive and grow.</p>" +
                "<p>Once again, welcome aboard! We look forward to working alongside you and achieving great things together.</p>" +
                "<p>Best regards,<br>BJIT HR Agency</p>" +
                "</body></html>";

        mailContent.setBody(htmlBody);
        mailContent.setAttachments(
                new ArrayList<>(Arrays.asList(
                        new File("C:\\Users\\BJIT\\Downloads\\home.jpeg"))));
        validateMailContent(mailContent);

        mailService.sendHtmlTemplateMail(mailContent, "welcome.html");
    }

    private static void validateMailContent(MailContent mailContent) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<MailContent>> violations = validator.validate(mailContent);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}
