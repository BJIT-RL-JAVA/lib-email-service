package com.bjit.mailservice.services;

import com.bjit.mailservice.models.MailContent;
import com.bjit.mailservice.services.impl.SendGridMailService;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

/**
 * @author Mallika Dey
 */
@SpringBootTest
public class SendGridMailServiceTest {
    @Mock
    private SendGrid sendGrid;
    @InjectMocks
    private SendGridMailService sendGridMailService;
    private MailContent mailContent;

    @BeforeEach
    void setUp() {
        mailContent = createMailContent();
    }

    @Test
    public void mailSendUsingSendGridTestShouldExecuteSuccessfully()
            throws MessagingException {
        try {
            when(sendGrid.api(any(Request.class))).thenReturn(new Response());
        } catch (IOException ignored) {
        }

        assertThat(sendGridMailService.sendMail(mailContent)).isEqualTo("mail sent successfully");
    }

    @Test
    public void mailSendUsingSendGridTest_FileNotFound_ShouldFailed()
            throws MessagingException {
        mailContent.setAttachments(new ArrayList<>(List.of(
                new File("abc.txt"))));
        try {
            when(sendGrid.api(any(Request.class))).thenReturn(new Response());
        } catch (IOException ignored) {

        }

        assertThat(sendGridMailService.sendMail(mailContent)).isEqualTo("mail sending failed");
    }

    @Test
    public void sendHTMLTemplateMailUsingAWSSES_ShouldExecuteSuccessfully() throws MessagingException {
        try {
            when(sendGrid.api(any(Request.class))).thenReturn(new Response());
        } catch (IOException ignored) {

        }
        assertThat(sendGridMailService.sendHtmlTemplateMail(mailContent)).isEqualTo("mail sent successfully");
    }


    private MailContent createMailContent() {
        MailContent mailContent = new MailContent();
        mailContent.setFrom("abcd@gmail.com");
        mailContent.setTo(new ArrayList<>(Arrays.asList("abcd1@gmail.com")));
        mailContent.setCc(new ArrayList<>(Arrays.asList("abc@gmail.com")));
        mailContent.setBcc(new ArrayList<>(Arrays.asList("abc1@gmail.com")));
        mailContent.setSubject("Testing aws ses mail service");
        mailContent.setBody("testing aws ses using mockito");
        mailContent.setAttachments(new ArrayList<>(List.of(
                new File("C:\\Users\\Bjit\\Desktop\\others\\sss.txt"))));

        return mailContent;
    }
}
