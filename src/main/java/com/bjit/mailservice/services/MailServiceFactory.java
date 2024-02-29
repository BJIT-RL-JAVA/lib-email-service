package com.bjit.mailservice.services;

/**
 * Factory interface for creating instances of MailService.
 * Implementations of this interface should provide a way to create a specific MailService.
 *
 * @author Mallika Dey
 */
public interface MailServiceFactory {
    MailService createMailService();
}
