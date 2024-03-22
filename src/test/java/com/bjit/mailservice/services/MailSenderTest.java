package com.bjit.mailservice.services;

import com.bjit.mailservice.models.MailContent;
import jakarta.mail.MessagingException;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * @author Mallika Dey
 */
@SpringBootTest
public class MailSenderTest {
    private MailSender mailSender;
    private MailServiceFactory mockMailServiceFactory;
    private MailService mockMailService;

    @BeforeEach
    void setUp() {
        mockMailServiceFactory = mock(MailServiceFactory.class);
        mockMailService = mock(MailService.class);

        when(mockMailServiceFactory.createMailService()).thenReturn(mockMailService);

        mailSender = new MailSender(mockMailServiceFactory);
    }

    @Test
    void sendMail() throws MessagingException {
        MailContent mailContent = createMailContent();

        mailSender.sendMail(mailContent);

        ArgumentCaptor<MailContent> argumentCaptor = ArgumentCaptor.forClass(MailContent.class);
        verify(mockMailService).sendMail(argumentCaptor.capture());

        MailContent capturedMailContent = argumentCaptor.getValue();
    }

    @Test
    void sendMailWithHTMLTemplate() throws MessagingException {
        MailContent mailContent = createMailContent();

        mailSender.sendMailWithHTMLTemplate(mailContent);

        ArgumentCaptor<MailContent> argumentCaptor = ArgumentCaptor.forClass(MailContent.class);
        verify(mockMailService).sendHtmlTemplateMail(argumentCaptor.capture());

        MailContent capturedMailContent = argumentCaptor.getValue();
    }

    @Test
    void sendMailWithInvalidContent() {
        MailContent mailContent = new MailContent();
        assertThrows(ConstraintViolationException.class, () -> mailSender.sendMail(mailContent));
    }

    private MailContent createMailContent() {
        MailContent mailContent = new MailContent();
        mailContent.setFrom("khalid.hasan@bjitgroup.com");
        mailContent.setTo(new ArrayList<>(Arrays.asList("mallika.dey@bjitgroup.com")));
        mailContent.setSubject("Testing aws ses mail service");
        mailContent.setBody("testing aws ses using mockito");
        mailContent.setAttachments(
                new ArrayList<>(Arrays.asList(
                        new File("E:\\Mallika Dey\\materials\\others\\sss.txt"))));

        return mailContent;
    }

}
