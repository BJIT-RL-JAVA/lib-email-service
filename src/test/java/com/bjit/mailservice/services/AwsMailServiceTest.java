package com.bjit.mailservice.services;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.SendRawEmailRequest;
import com.amazonaws.services.simpleemail.model.SendRawEmailResult;
import com.bjit.mailservice.models.MailContent;
import com.bjit.mailservice.services.impl.AwsMailService;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
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
public class AwsMailServiceTest {
    @Mock
    private AmazonSimpleEmailService amazonSimpleEmailService;
    @InjectMocks
    private AwsMailService awsMailService;
    private MailContent mailContent;

    @BeforeEach
    void setUp() {
        mailContent = createMailContent();
    }

    @Test
    public void sendMailUsingAWSSESShouldExecuteSuccessfully() throws MessagingException {
        mailContent.setAttachments(new ArrayList<>(
                List.of(new File("C:\\Users\\Bjit\\Desktop\\others\\sss.txt"))));
        when(amazonSimpleEmailService.sendRawEmail(any(SendRawEmailRequest.class)))
                .thenReturn(new SendRawEmailResult());

        String msg = awsMailService.sendMail(mailContent);
        assertThat(msg).isEqualTo("mail sent successfully");
    }

    @Test
    public void sendMailUsingAWSSES_FileNotFound_ShouldFail() throws MessagingException {
        mailContent.setAttachments(new ArrayList<>(List.of(new File("abc.txt"))));

        when(amazonSimpleEmailService.sendRawEmail(any(SendRawEmailRequest.class)))
                .thenReturn(new SendRawEmailResult());

        assertThat(awsMailService.sendMail(mailContent)).isEqualTo("mail sending failed");
    }

    @Test
    public void sendHTMLTemplateMailUsingAWSSES_ShouldExecuteSuccessfully() throws MessagingException {
        when(amazonSimpleEmailService.sendRawEmail(any(SendRawEmailRequest.class)))
                .thenReturn(new SendRawEmailResult());

        assertThat(awsMailService.sendHtmlTemplateMail(mailContent)).isEqualTo("mail sent successfully");
    }

    private MailContent createMailContent() {
        MailContent mailContent = new MailContent();
        mailContent.setFrom("abcd@gmail.com");
        mailContent.setTo(new ArrayList<>(Arrays.asList("abcd1@gmail.com")));
        mailContent.setCc(new ArrayList<>(Arrays.asList("abc@gmail.com")));
        mailContent.setBcc(new ArrayList<>(Arrays.asList("abc1@gmail.com")));
        mailContent.setSubject("Testing aws ses mail service");
        mailContent.setBody("testing aws ses using mockito");

        return mailContent;
    }


}
