# MailService

MailService is a Java-based library that provides functionality for sending emails. It offers a simple and easy-to-use API to integrate email sending capabilities into your Java applications.

## Features

- Support for 3 mail services (AWS SES, SendGrid, SMTP)
- Send emails with attachments
- Support for specifying recipients (To, Cc, Bcc)
- Customizable email subject and body
- Customizable and default HTML template
- Automatic validation of email content, including attachments
- Integration with Jakarta Bean Validation for input validation

## QuickStart

To use MailService in your project, you can include it as a jar.

### Installation

Add the `mailservice-0.0.1-SNAPSHOT-jar-with-dependencies.jar` in your project.

[//]: # (Add the following dependency to your `pom.xml`:)

[//]: # ()
[//]: # (```xml)

[//]: # (<dependency>)

[//]: # (    <groupId>com.example</groupId>)

[//]: # (    <artifactId>mailservice</artifactId>)

[//]: # (    <version>1.0.0</version>)

[//]: # (</dependency>)

[//]: # (```)

### Configuration
Before using MailService, you need to configure it with the appropriate settings for your chosen email service provider. Below are the configuration options for AWS SES, SendGrid:

#### AWS configuration

```yml
cloud:
  aws:
    region:
      static: <aws-region>
      auto: false
    stack:
      auto: false
    credentials:
      accessKey: <your-access-key-id>
      secretKey: <your-secret-access-key>
```

#### SendGrid configuration

```yml
sendgrid:
  api:
    key: <your-sendgrid-api-key>
```

#### MailService Type

```yml
mail:
  service:
    type: <mail-service-type>
```

### Usage
To send an email using MailService, follow these steps:

- Add _com.bjit_ to the component scan. Here is an example,
```java
@ComponentScan(basePackages = {"org.example", "com.bjit"})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
```

- Create a MailContent object with the email details, including the recipient(s), sender, subject, and body.

```java
MailContent mailContent = new MailContent();
mailContent.setTo(new ArrayList<>(Arrays.asList("receiver_mail")));
mailContent.setFrom("sender_mail");
mailContent.setSubject("mail_subject");
mailContent.setBody("mail_body");
```
- Use the MailSender to send the email by calling the **sendMail** or **sendMailWithHTMLTemplate** method with the MailContent object.

```java
mailSender.sendMail(mailContent);
```

