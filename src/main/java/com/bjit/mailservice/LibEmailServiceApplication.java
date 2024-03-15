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
import java.util.HashMap;

@SpringBootApplication
public class LibEmailServiceApplication {

    public static void main(String[] args) throws MessagingException {
        //SpringApplication.run(LibEmailServiceApplication.class, args);
        ApplicationContext applicationContext = SpringApplication.run(LibEmailServiceApplication.class, args);

        MailSender mailSender = applicationContext.getBean(MailSender.class);
        MailContent mailContent = new MailContent();
        mailContent.setTo(new ArrayList<>(Arrays.asList("mallika.dey@bjitgroup.com")));
        mailContent.setCc(new ArrayList<>(Arrays.asList("khalidhasan374@gmail.com", "connectkhalid404@gmail.com")));
        mailContent.setSubject("Testing mail from deployed maven package");
        mailContent.setBody("Modular configuration");
        mailContent.setAttachments(
                new ArrayList<>(Arrays.asList(
                        new File("C:\\Users\\Bjit\\Desktop\\Md Reaz Uddin(60199).pdf"))));
        // Assuming File object representing the HTML template file, named htmlTemplateFile
        File htmlTemplateFile = new File("C:\\Users\\Bjit\\Desktop\\MailTemplate.html");

        //For Dynamic Content
        HashMap<String, Object> objectMap = new HashMap<>();
        objectMap.put("TransactionType", "MFS");
        objectMap.put("TransferredAmount", "1,254.00");
        objectMap.put("ServiceCharge", "9.21");
        objectMap.put("Vat", "1.21");
        objectMap.put("GrandTotal", "3,012.00");
        objectMap.put("Remarks", "Bcash Transfer");
        mailContent.setObjectMap(objectMap);

        // Set the HTML template file in the MailContent object
        mailContent.setHtmlTemplate(htmlTemplateFile);
        mailSender.sendMailWithHTMLTemplate(mailContent);
    }
}