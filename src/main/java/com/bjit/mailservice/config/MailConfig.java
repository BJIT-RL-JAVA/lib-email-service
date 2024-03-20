package com.bjit.mailservice.config;

import com.bjit.mailservice.exception.EmailException;
import com.bjit.mailservice.models.MailServiceType;
import com.bjit.mailservice.services.MailSender;
import com.bjit.mailservice.services.MailServiceFactory;
import com.bjit.mailservice.services.impl.AWSFactory;
import com.bjit.mailservice.services.impl.SendGridFactory;
import com.bjit.mailservice.services.impl.SmtpFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Mallika Dey
 */
@Configuration
public class MailConfig {
    @Value("${cloud.aws.credentials.accessKey:}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secretKey:}")
    private String secretKey;

    @Value("${cloud.aws.region.static:}")
    private String region;

    @Value("${sendgrid.api.key:}")
    private String sendGridApiKey;

    /**
     * Bean definition for creating a MailServiceFactory based on the configured mail service type.
     *
     * @param mailServiceType The type of mail service to use (e.g., "sendgrid" or "aws")
     * @return MailServiceFactory instance for the specified mail service type
     * @throws EmailException if an invalid mail service type is provided
     */
    @Bean
    public MailServiceFactory mailServiceFactory(@Value("${mail.service.type}") String mailServiceType) {
        if (MailServiceType.SENDGRID.getValue().equals(mailServiceType.toUpperCase()))
            return new SendGridFactory(sendGridApiKey);
        else if (MailServiceType.AWS.getValue().equals(mailServiceType.toUpperCase()))
            return new AWSFactory(accessKey, secretKey, region);
        else if (MailServiceType.SMTP.getValue().equals(mailServiceType.toUpperCase()))
            return new SmtpFactory();

        throw new EmailException("Invalid mail service type: " + mailServiceType);
    }

    /**
     * Bean definition for creating a MailSender instance using the configured MailServiceFactory.
     *
     * @param mailServiceFactory MailServiceFactory instance for creating MailService
     * @return MailSender instance
     */
    @Bean
    public MailSender mailSender(MailServiceFactory mailServiceFactory) {
        return new MailSender(mailServiceFactory);
    }
}
