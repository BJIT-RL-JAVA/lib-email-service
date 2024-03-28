package com.bjit.mailservice.services;

import com.bjit.mailservice.exception.EmailException;
import com.bjit.mailservice.models.MailContent;
import com.bjit.mailservice.services.impl.SmtpMailService;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Mallika Dey
 */
@SpringBootTest
@TestPropertySource(locations = "/application-test.properties")
public class SMTPMailServiceTest {
    private SmtpMailService smtpMailService;
    private MailContent mailContent;
    @Value("${file.valid.location}")
    private String validFile;
    @Value("${file.invalid.location}")
    private String invalidFile;

    @BeforeEach
    public void setUp() {
        smtpMailService = new SmtpMailService();
        mailContent = mock(MailContent.class);

        mailContentFieldMock();
    }

    @Test
    public void testSendMail() throws MessagingException {
        when(mailContent.getBcc()).thenReturn(new ArrayList<>(List.of("abc2@gmail.com")));
        when(mailContent.getAttachments())
                .thenReturn(new ArrayList<>(
                        List.of(new File(validFile))));

        String result = smtpMailService.sendMail(mailContent);

        assertEquals("Mail has been sent successfully.", result);
    }

    @Test
    public void testSendHtmlTemplateMailUsingCustomizeTemplate_Successful() throws MessagingException {
        when(mailContent.getBcc()).thenReturn(null);
        when(mailContent.getHtmlTemplate()).thenReturn("[Dynamic Content]");

        String result = smtpMailService.sendHtmlTemplateMail(mailContent);

        assertEquals("HTML template mail has been sent successfully.", result);
    }

    @Test
    public void testSendHtmlTemplateMailUsingDefaultTemplate_Successful() throws MessagingException {
        String result = smtpMailService.sendHtmlTemplateMail(mailContent);

        assertEquals("HTML template mail has been sent successfully.", result);
    }

    @Test
    public void testSendMailFail() throws MessagingException {
        when(mailContent.getAttachments())
                .thenReturn(new ArrayList<>(List.of(new File(invalidFile))));

        assertThrows(EmailException.class, () ->
                smtpMailService.sendMail(mailContent));
    }

    private void mailContentFieldMock() {
        when(mailContent.getFrom()).thenReturn("ab@gmail.com");
        when(mailContent.getTo()).thenReturn(new ArrayList<>(List.of("abc@gmail.com")));
        when(mailContent.getCc()).thenReturn(new ArrayList<>(List.of("abc1@gmail.com")));
        when(mailContent.getSubject()).thenReturn("Test Subject");
        when(mailContent.getBody()).thenReturn("Test Body");
    }
}
