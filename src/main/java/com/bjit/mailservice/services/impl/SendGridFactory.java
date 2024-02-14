package com.bjit.mailservice.services.impl;

import com.bjit.mailservice.services.MailService;
import com.bjit.mailservice.services.MailServiceFactory;
import com.sendgrid.SendGrid;
import org.springframework.context.annotation.Bean;

/**
 * Factory for creating SendGrid mail service instance
 *
 * @author Mallika Dey
 */
public class SendGridFactory implements MailServiceFactory {
    private String sendGridApiKey;

    public SendGridFactory(String sendGridApiKey) {
        this.sendGridApiKey = sendGridApiKey;
    }

    @Override
    public MailService createMailService() {
        return new SendGridMailService(sendGrid());
    }

    public SendGrid sendGrid() {
        return new SendGrid(sendGridApiKey);
    }
}
