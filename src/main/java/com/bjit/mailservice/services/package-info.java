/**
 * Provides services for sending emails.
 * This package contains implementations for email sending functionality,
 * including integration with AWS SES and related utilities.
 *
 * <p>
 * Classes in this package include:
 * <ul>
 *     <li>{@link com.bjit.mailservice.services.MailSender}: Service class for sending emails using a provided MailService implementation.</li>
 *     <li>{@link com.bjit.mailservice.services.MailService}: Interface defining the contract for email sending services.</li>
 *     <li>{@link com.bjit.mailservice.services.MailServiceFactory}: Factory interface for creating specific MailService implementations.</li>
 *     <li>{@link com.bjit.mailservice.services.impl.AwsMailService}: Implementation of MailService for sending emails using AWS SES.</li>
 * </ul>
 * </p>
 *
 * <p>
 * Classes in this package may interact with the following packages:
 * <ul>
 *     <li>{@link com.bjit.mailservice.models}: Contains classes representing email-related data models.</li>
 *     <!-- Add other related packages if needed -->
 * </ul>
 * </p>
 *
 * @since 1.0
 */
package com.bjit.mailservice.services;