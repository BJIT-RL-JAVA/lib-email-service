package com.bjit.mailservice;

import com.bjit.mailservice.models.MailContent;
import com.bjit.mailservice.services.MailSender;
import jakarta.mail.MessagingException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

@SpringBootApplication
public class LibEmailServiceApplication {

    public static void main(String[] args) throws MessagingException {
        //SpringApplication.run(LibEmailServiceApplication.class, args);
        ApplicationContext applicationContext = SpringApplication.run(LibEmailServiceApplication.class, args);

        MailSender mailSender = applicationContext.getBean(MailSender.class);
        MailContent mailContent = new MailContent();
//        mailContent.setfrom("khalid.hasan@bjitgroup.com");
        mailContent.setTo(new ArrayList<>(Arrays.asList("mallika.dey@bjitgroup.com")));
//        mailContent.setCc(new arraylist<>(arfrays.aslist("khalidhasan374@gmail.com", "connectkhalid404@gmail.com")));
        mailContent.setSubject("Testing mail from deployed maven package");
        mailContent.setBody("Modular configuration");
        mailContent.setAttachments(
                new ArrayList<>(Arrays.asList(
                        new File("C:\\Users\\Bjit\\Desktop\\others\\sss.txt"))));

        mailSender.sendMail(mailContent);

    }
}