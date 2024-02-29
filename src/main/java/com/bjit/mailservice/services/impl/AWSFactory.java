package com.bjit.mailservice.services.impl;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.bjit.mailservice.services.MailService;
import com.bjit.mailservice.services.MailServiceFactory;
import io.awspring.cloud.ses.SimpleEmailServiceMailSender;
import org.springframework.mail.MailSender;

/**
 * Factory for creating AWS mail service instances.
 * Requires AWS credentials and region for configuration.
 * Implements the MailServiceFactory interface.
 *
 * @author Mallika Dey
 */
public class AWSFactory implements MailServiceFactory {
    private String accessKey;
    private String secretKey;
    private String region;

    public AWSFactory(String accessKey, String secretKey, String region) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.region = region;
    }

    /**
     * Creates and returns an instance of AwsMailService.
     *
     * @return An AwsMailService instance.
     */
    @Override
    public MailService createMailService() {
        AmazonSimpleEmailService amazonSimpleEmailService = amazonSimpleEmailService();
        return new AwsMailService(amazonSimpleEmailService);
    }

    private AmazonSimpleEmailService amazonSimpleEmailService() {
        BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);

        return AmazonSimpleEmailServiceClientBuilder.standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();
    }

    private MailSender mailSender() {
        return new SimpleEmailServiceMailSender(amazonSimpleEmailService());
    }
}
