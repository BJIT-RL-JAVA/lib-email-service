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
        mailContent.setBcc(null);
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
        mailContent.setTo(new ArrayList<>(Arrays.asList("khalidhasan374@gmail.com")));
        mailContent.setCc(new ArrayList<>(Arrays.asList( "connectkhalid404@gmail.com")));
        mailContent.setBcc(new ArrayList<>(Arrays.asList("mallika.dey@bjitgroup.com")));
        mailContent.setSubject("Testing mail");
        // HTML-formatted email body
        String htmlBody = """
                <p>Welcome to BJIT! We are thrilled to have you join our team and embark on this journey together. At BJIT, we strive for excellence, innovation, and collaboration, and we're confident that your unique skills and talents will contribute significantly to our shared success.</p>
                        <p>As you settle into your role, don't hesitate to reach out to your colleagues or managers for guidance and support. We believe in fostering a supportive and inclusive work environment where everyone has the opportunity to thrive and grow.</p>
                        <p>Once again, welcome aboard! We look forward to working alongside you and achieving great things together.</p>
                        <p>Best regards,<br><span class="signature">BJIT HR Agency</span></p>""";
        String htmlTemplate = """
                <!DOCTYPE html>
                               <html lang="en">
                               <head>
                                   <meta charset="UTF-8">
                                   <meta name="viewport" content="width=device-width, initial-scale=1.0">
                                   <title>Greeting Note</title>
                                   <style>
                                       body {
                                           font-family: Arial, sans-serif;
                                           background-color: #f4f4f4;
                                           margin: 0;
                                           padding: 0;
                                           line-height: 1.6;
                                       }
                                       .container {
                                           max-width: 600px;
                                           margin: 20px auto;
                                           padding: 20px;
                                           background-color: #fff;
                                           border-radius: 5px;
                                           box-shadow: 0 2px 5px rgba(0,0,0,0.1);
                                       }
                                       h1, p {
                                           margin-bottom: 20px;
                                       }
                                       p {
                                           font-weight: bold; /* Set the text to bold */
                                           font-style: italic; /* Set the style to italic */
                                           color: purple; /* Set the color to purple */
                                       }
                                       .signature {
                                           font-style: italic;
                                       }
                                   </style>
                               </head>
                               <body>
                                   <div class="container">
                                       <h1>Welcome to Our Community!</h1>
                                       <p>Hello,</p>
                                       <p>We are delighted to welcome you to our community! We believe in creating a warm and inclusive environment where everyone feels valued and appreciated.</p>
                                       <p>As you embark on this journey with us, we encourage you to explore, learn, and grow. Don't hesitate to reach out if you have any questions or need assistance.</p>
                                       <p>We look forward to getting to know you better and sharing many memorable experiences together.</p>
                                       <p>Best regards,<br><span class="signature">Shaiful Islam</span></p>
                                   </div>
                                   <div class="container">[Dynamic Content]</p>
                                   </div>
                               </body>
                               </html>
                               """;
//        mailContent.setHtmlTemplate(htmlTemplate);
        mailContent.setBody(htmlBody);
        mailContent.setAttachments(
                new ArrayList<>(Arrays.asList(
                        new File("C:\\Users\\BJIT\\Downloads\\home.jpeg"))));
        validateMailContent(mailContent);

        mailService.sendHtmlTemplateMail(mailContent);
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
