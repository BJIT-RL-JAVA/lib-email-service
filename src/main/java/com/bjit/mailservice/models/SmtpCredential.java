package com.bjit.mailservice.models;

import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SmtpCredential {
    private Boolean enableStartTls;
    private Boolean smtpAuth;
    private String smtpPort;
    private String smtpHost;
    private String userMail;
    private String userPassword;
}
