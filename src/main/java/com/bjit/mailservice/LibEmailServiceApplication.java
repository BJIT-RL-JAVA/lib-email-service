package com.bjit.mailservice;

import com.bjit.mailservice.services.MailSender;
import jakarta.mail.MessagingException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class LibEmailServiceApplication {

    public static void main(String[] args) throws MessagingException {
        ApplicationContext applicationContext = SpringApplication.run(LibEmailServiceApplication.class, args);

        MailSender mailSender = applicationContext.getBean(MailSender.class);
//        mailSender.sendMail();
        mailSender.sendMailWithHTMLTemplate();
    }
}
