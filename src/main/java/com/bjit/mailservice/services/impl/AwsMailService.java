package com.bjit.mailservice.services.impl;

import com.bjit.mailservice.models.MailContent;
import com.bjit.mailservice.services.MailService;
import jakarta.mail.MessagingException;

public class AwsMailService implements MailService {
    @Override
    public String sendMail(MailContent mailContent) {
        return null;
    }

    @Override
    public String sendHtmlTemplateMail(MailContent mailContent, String templateName) throws MessagingException {
        return null;
    }
}
