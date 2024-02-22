package com.bjit.mailservice.services;

import com.bjit.mailservice.models.MailContent;
import jakarta.validation.*;

import java.util.Set;

/**
 * @author Mallika Dey
 */
public class MailContentValidation {
    public static void validateMailContent(MailContent mailContent) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<MailContent>> violations = validator.validate(mailContent);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}
