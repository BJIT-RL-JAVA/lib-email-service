package com.bjit.mailservice.services;

import com.bjit.mailservice.models.MailContent;
import jakarta.mail.MessagingException;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

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
        mailContent.setTo(new ArrayList<>(Arrays.asList("mallika.dey@bjitgroup.com")));
        mailContent.setCc(new ArrayList<>(Arrays.asList("khalidhasan374@gmail.com", "connectkhalid404@gmail.com")));
        mailContent.setBcc(new ArrayList(Arrays.asList("khalidhasankibria@gmail.com", "connectkhalid404@gmail.com")));
        mailContent.setSubject("Testing mail");
        mailContent.setBody("Message body");
        mailContent.setAttachments(
                new ArrayList<>(Arrays.asList(
                        new File("C:\\Users\\Bjit\\Desktop\\sss.txt"))));
        MailContentValidation.validateMailContent(mailContent);

        mailService.sendMail(mailContent);
    }

    public void sendMailWithHTMLTemplate() throws MessagingException {
        MailContent mailContent = new MailContent();
        mailContent.setFrom("khalid.hasan@bjitgroup.com");
        mailContent.setTo(new ArrayList<>(Arrays.asList("mallika.dey@bjitgroup.com")));
//        mailContent.setCc(new ArrayList<>(Arrays.asList("khalidhasan374@gmail.com", "connectkhalid404@gmail.com")));
//        mailContent.setBcc(new ArrayList(Arrays.asList("khalidhasankibria@gmail.com", "connectkhalid404@gmail.com")));
        mailContent.setSubject("Testing mail");
        mailContent.setBody("Message body");
        mailContent.setAttachments(
                new ArrayList<>(Arrays.asList(
                        new File("C:\\Users\\BJIT\\Downloads\\home.jpeg"))));
        MailContentValidation.validateMailContent(mailContent);

        mailService.sendHtmlTemplateMail(mailContent, "welcome.html");
    }
}
