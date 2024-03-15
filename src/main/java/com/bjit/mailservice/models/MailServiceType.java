package com.bjit.mailservice.models;

/**
 * Represents the type of mail services
 *
 * @author Mallika Dey
 */
public enum MailServiceType {
    SENDGRID("SENDGRID"),
    AWS("AWS"),
    SMTP("SMTP");
    private final String value;

    MailServiceType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
