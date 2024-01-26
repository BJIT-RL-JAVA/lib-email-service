package com.bjit.mailservice.services;

import com.bjit.mailservice.exception.EmailException;
import com.bjit.mailservice.models.MailContent;

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
    /**
     * Sends an email with the specified mail content.
     *
     * @param mailContent The content of the email to be sent.
     * @return A message indicating the result of the email sending operation.
     *         It could be a success message or an error message.
     */

    String sendMail(MailContent mailContent) throws EmailException;
}

