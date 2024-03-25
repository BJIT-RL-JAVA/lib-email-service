package com.bjit.mailservice;

import com.bjit.mailservice.models.MailContent;
import com.bjit.mailservice.services.MailSender;
import jakarta.mail.MessagingException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

@SpringBootApplication
public class LibEmailServiceApplication {

    public static void main(String[] args) throws MessagingException {
        SpringApplication.run(LibEmailServiceApplication.class, args);

    }
}