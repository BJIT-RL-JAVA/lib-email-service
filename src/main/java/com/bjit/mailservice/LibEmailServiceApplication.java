package com.bjit.mailservice;

import com.bjit.mailservice.exception.EmailException;
import com.bjit.mailservice.models.MailContent;
import com.bjit.mailservice.services.impl.SmtpMailService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class LibEmailServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(LibEmailServiceApplication.class, args);

        SmtpMailService gmailSender = new SmtpMailService();

        String from = "khalidhasan374@gmail.com";
        List<String> to = Arrays.asList("khalidhasan374@gmail.com", "connectkhalid404@gmail.com");
        List<String> cc = Arrays.asList("khalidhasan374@gmail.com", "connectkhalid404@gmail.com");
        List<String> bcc = Arrays.asList("khalidhasankibria@gmail.com", "connectkhalid404@gmail.com");
        String subject = "Sample email with having cc and bcc";
        String text = "sending massage with attachment to multiple recipient along with cc and bcc";

        List<File> attachmentFiles = Arrays.asList(
                new File("C:\\Users\\BJIT\\Downloads\\20230801_044533795_iOS.jpg"),
                new File("C:\\Users\\BJIT\\Downloads\\flower.jpg"),
                new File("C:\\Users\\BJIT\\Downloads\\download.jpeg"),
                new File("C:\\Users\\BJIT\\Downloads\\20221223_153514060_iOS-1689994677260.png")
        );

        try {
            MailContent mailContent = createMailContent(from, to, cc,bcc,subject, text, attachmentFiles);
            gmailSender.sendMail(mailContent);
            System.out.println("Email has been sent successfully.");
        } catch (EmailException e) {
            System.out.println("Error sending email: " + e.getMessage());
        }
    }

    private static MailContent createMailContent
            (String from, List<String> to, List<String>cc,
             List<String>bcc, String subject, String body, List<File> attachments) {
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
