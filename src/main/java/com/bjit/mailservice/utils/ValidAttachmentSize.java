package com.bjit.mailservice.utils;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.ReportAsSingleViolation;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.groups.Default;

import java.io.File;
import java.lang.annotation.*;

/**
 * Custom validation annotation for checking the total size of attachments.
 * The total size must not exceed 25 MB.
 * This annotation can be applied to a field representing a list of attachments.
 *
 * @author Mallika Dey
 */
@Documented
@Constraint(validatedBy = { AttachmentSizeValidator.class })
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@NotNull(groups = { Default.class, ValidAttachment.class })
@Size(max = 25, groups = { Default.class, ValidAttachment.class })
@ReportAsSingleViolation
public @interface ValidAttachmentSize {
    String message() default "Total size of attachments must not exceed 25 MB.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
