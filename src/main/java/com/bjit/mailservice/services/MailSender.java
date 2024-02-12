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
        mailContent.setTo(new ArrayList<>(Arrays.asList("mallika.dey@bjitgroup.com", "mallikaju307@gmail.com")));
        mailContent.setFrom("khalid.hasan@bjitgroup.com");
        mailContent.setSubject("Testing mail");
        mailContent.setBody("Message body");
        mailContent.setAttachments(
                new ArrayList<>(Arrays.asList(
                        new File("C:\\Users\\Bjit\\Documents\\SMTP.docx"),
                        new File("C:\\Users\\Bjit\\Desktop\\sss.txt"))));
        validateMailContent(mailContent);

        mailService.sendMail(mailContent);
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