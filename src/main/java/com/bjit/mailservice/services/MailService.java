package com.bjit.mailservice.services;

import com.bjit.mailservice.models.MailContent;
import jakarta.mail.MessagingException;

/**
 * Interface for sending email messages.
 * Implementations of this interface handle the actual email sending logic.
 * <p>
 * Implementing classes should provide a concrete implementation of the sendMail method.
 * </p>
 *
 * @author Mallika Dey
 */
public interface MailService {

    String sendMail(MailContent mailContent) throws MessagingException;

    String sendHtmlTemplateMail(MailContent mailContent) throws MessagingException;
}

