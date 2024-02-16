package com.bjit.mailservice.email;

import com.bjit.mailservice.exception.EmailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author Mallika Dey
 */
public class EmailTemplate {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmailTemplate.class);

    @Cacheable("emailTemplateCache")
    public String getEmailTemplate() {
        try {
            ClassPathResource resource = new ClassPathResource("email_template.html");
            return StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
        } catch (IOException ex) {
            LOGGER.error(String.format("%s%s", "NOT_FOUND", ex));
            throw new EmailException("NOT_FOUND");
        }
    }
}
