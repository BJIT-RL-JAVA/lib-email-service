package com.bjit.mailservice.services.impl;

import com.bjit.mailservice.models.SmtpCredential;
import com.bjit.mailservice.services.MailService;
import com.bjit.mailservice.services.MailServiceFactory;

/**
 * Factory for creating SMTP mail service instance
 *
 * @author Mallika Dey
 */
public class SmtpFactory implements MailServiceFactory {
    private SmtpCredential smtpCredential;

    public SmtpFactory(SmtpCredential smtpCredential) {
        this.smtpCredential = smtpCredential;
    }

    /**
     * Creates and returns an instance of SmtpMailService.
     *
     * @return the {@code MailService}
     */
    @Override
    public MailService createMailService() {
        return new SmtpMailService(smtpCredential);
    }
}
