package com.bjit.mailservice.services.impl;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.bjit.mailservice.models.MailContent;
import com.bjit.mailservice.services.MailService;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Properties;

import com.amazonaws.services.simpleemail.model.RawMessage;
import com.amazonaws.services.simpleemail.model.SendRawEmailRequest;
import com.bjit.mailservice.services.MailValidation;
import jakarta.activation.DataHandler;
import jakarta.activation.FileDataSource;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.Session;
import jakarta.mail.internet.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

/**
 * Implementation of the MailService interface for sending emails using AWS SES (Simple Email Service).
 * This class handles the integration with AWS SES and provides the logic for sending email messages.
 *
 * @author Mallika Dey
 */
public class AwsMailService implements MailService, MailValidation {
    private final AmazonSimpleEmailService client;

    public AwsMailService(AmazonSimpleEmailService client) {
        this.client = client;
    }

    @Override
    public String sendMail(MailContent mailContent) throws MessagingException {
        Session session = Session.getDefaultInstance(new Properties());

        MimeMessage message = generateMimeMessage(mailContent, session);

        MimeMultipart msg_body = new MimeMultipart("alternative");

        MimeBodyPart wrap = new MimeBodyPart();

        MimeBodyPart textPart = new MimeBodyPart();
        textPart.setContent(mailContent.getBody(), "text/plain; charset=UTF-8");

        MimeBodyPart htmlPart = new MimeBodyPart();
        htmlPart.setContent(mailContent.getBody(), "text/html; charset=UTF-8");

        msg_body.addBodyPart(textPart);
        msg_body.addBodyPart(htmlPart);

        wrap.setContent(msg_body);

//        MimeMultipart msg = new MimeMultipart("mixed");
//
//        message.setContent(msg);
//        msg.addBodyPart(wrap);

        Multipart multipart = new MimeMultipart();
//        multipart.addBodyPart(textPart);
        multipart.addBodyPart(wrap);

        for (File file : mailContent.getAttachments()) {
            MimeBodyPart filePart = new MimeBodyPart();
            checkFileCompatibility(file);
            filePart.setDataHandler(new DataHandler(new FileDataSource(file)));
            filePart.setFileName(file.getName());
            multipart.addBodyPart(filePart);
        }

        message.setContent(multipart);
        try {
            System.out.println("Attempting to send an email through Amazon SES "
                    + "using the AWS SDK for Java...");

            PrintStream out = System.out;
            message.writeTo(out);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            message.writeTo(outputStream);
            RawMessage rawMessage =
                    new RawMessage(ByteBuffer.wrap(outputStream.toByteArray()));

            SendRawEmailRequest rawEmailRequest =
                    new SendRawEmailRequest(rawMessage);

            client.sendRawEmail(rawEmailRequest);
            System.out.println("Email sent!");
        } catch (Exception ex) {
            System.out.println("Email Failed");
            System.err.println("Error message: " + ex.getMessage());
            ex.printStackTrace();
        }
        return "mail sent successfully";
    }

    private MimeMessage generateMimeMessage(MailContent mailContent,
                                            Session session) throws MessagingException {
        MimeMessage message = new MimeMessage(session);

        message.setSubject(mailContent.getSubject(), "UTF-8");


        ArrayList<InternetAddress> addressCc = new ArrayList<InternetAddress>();
        ArrayList<InternetAddress> addressBCc = new ArrayList<InternetAddress>();

        if (!ObjectUtils.isEmpty(mailContent.getTo())) {
            setRecipients(message, Message.RecipientType.TO, mailContent.getTo());
        }
        if (!ObjectUtils.isEmpty(mailContent.getCc())) {
            setRecipients(message, Message.RecipientType.CC, mailContent.getCc());
        }
        if (!ObjectUtils.isEmpty(mailContent.getBcc())) {
            setRecipients(message, Message.RecipientType.BCC, mailContent.getBcc());
        }

        message.setFrom(new InternetAddress(mailContent.getFrom()));

        return message;
    }

    private void setRecipients(MimeMessage message, Message.RecipientType type,
                               ArrayList<String> addresses) throws MessagingException {

        ArrayList<InternetAddress> addressTo = new ArrayList<InternetAddress>();
        for (String address : addresses) {
            addressTo.add(new InternetAddress(address));
        }
        message.setRecipients(type, addressTo.toArray(new InternetAddress[0]));
    }
}
