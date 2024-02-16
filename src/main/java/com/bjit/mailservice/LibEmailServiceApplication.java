package com.bjit.mailservice;

import com.bjit.mailservice.models.MailContent;
import com.bjit.mailservice.services.impl.SmtpMailService;
import jakarta.mail.MessagingException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

@SpringBootApplication
public class LibEmailServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(LibEmailServiceApplication.class, args);

        SmtpMailService gmailSender = new SmtpMailService();

        String from = "khalidhasan374@gmail.com";
        ArrayList<String> to = new ArrayList<>(
                Arrays.asList("khalidhasan374@gmail.com", "connectkhalid404@gmail.com"));
        ArrayList<String> cc = new ArrayList<>(
                Arrays.asList("khalidhasan374@gmail.com", "connectkhalid404@gmail.com"));
        ArrayList<String> bcc = new ArrayList<>(
                Arrays.asList("khalidhasankibria@gmail.com", "connectkhalid404@gmail.com"));
        String subject = "Sample email with having cc and bcc";
        String text = "sending massage with attachment to multiple recipient along with cc and bcc";

        ArrayList<File> attachmentFiles = new ArrayList<>(Arrays.asList(
                new File("C:\\Users\\BJIT\\Downloads\\flower.jpg"),
                new File("C:\\Users\\BJIT\\Downloads\\download.jpeg"),
                new File("C:\\Users\\BJIT\\Downloads\\20221223_153514060_iOS-1689994677260.png")
        ));

        try {
            MailContent mailContent = createMailContent(from, to, cc, bcc, subject, text, attachmentFiles);
            // Change templateName to the name of your HTML template file
            String templateName = "welcome.html";
            gmailSender.sendHtmlTemplateMail(mailContent, templateName);
            System.out.println("Email has been sent successfully.");
        } catch (MessagingException e) {
            System.out.println("Error sending email: " + e.getMessage());
        }
    }

    private static MailContent createMailContent
            (String from, ArrayList<String> to, ArrayList<String> cc,
             ArrayList<String> bcc, String subject, String body, ArrayList<File> attachments) {
        MailContent mailContent = new MailContent();
        mailContent.setFrom(from);
        mailContent.setTo(to);
        mailContent.setCc(cc);
        mailContent.setBcc(bcc);
        mailContent.setSubject(subject);
        mailContent.setBody(body);
        mailContent.setAttachments(attachments);
        return mailContent;
    }
}
