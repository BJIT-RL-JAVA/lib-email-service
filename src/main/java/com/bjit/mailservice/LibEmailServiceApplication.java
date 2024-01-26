package com.bjit.mailservice;

import com.bjit.mailservice.exception.EmailException;
import com.bjit.mailservice.models.MailContent;
import com.bjit.mailservice.services.MailService;
import com.bjit.mailservice.services.impl.SmtpMailService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class LibEmailServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(LibEmailServiceApplication.class, args);
        MailService gmailSender = new SmtpMailService();

        String from = "khalidhasan374@gmail.com";
        String to = "connectkhalid404@gmail.com";
        String subject = "Sample email 1";
        String text = "test2";

        List<File> attachmentFiles = Arrays.asList(
                new File("C:\\Users\\BJIT\\Downloads\\20230801_044533795_iOS.jpg"),
                new File("C:\\Users\\BJIT\\Downloads\\flower.jpg"),
                new File("C:\\Users\\BJIT\\Downloads\\download.jpeg"),
                new File("C:\\Users\\BJIT\\Downloads\\20221223_153514060_iOS-1689994677260.png")
        );

        try {
            MailContent mailContent = createMailContent(from, to, subject, text, attachmentFiles);
            gmailSender.sendMail(mailContent);
            System.out.println("Email has been sent successfully.");
        } catch (EmailException e) {
            System.out.println("Error sending email: " + e.getMessage());
        }
    }

    private static MailContent createMailContent(String from, String to, String subject, String body, List<File> attachments) {
        MailContent mailContent = new MailContent();
        mailContent.setFrom(from);
        mailContent.setTo(new ArrayList<>(Arrays.asList(to)));
        mailContent.setSubject(subject);
        mailContent.setBody(body);
        mailContent.setAttachments(new ArrayList<>(attachments));
        return mailContent;
    }
}
