package com.bjit.mailservice.services.impl;

import com.bjit.mailservice.services.MailService;
import com.bjit.mailservice.services.MailServiceFactory;

/**
 * @author Mallika Dey
 */
public class SendGridFactory implements MailServiceFactory {
    @Override
    public MailService createMailService() {
        return new SendGridMailService();
    }
}
