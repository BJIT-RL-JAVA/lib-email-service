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
To obtain the `mailservice-0.0.1-SNAPSHOT-jar-with-dependencies.jar` file for integration into your project, please follow these formal steps:

1. Navigate to the **com.bjit.mailservice** under packages section.
2. Download the **mailservice-0.0.1-SNAPSHOT-jar-with-dependencies.jar** file 
3. Once the download is complete, add the downloaded JAR file to your project's dependencies.

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
Before using MailService, you need to configure it with the appropriate settings for your chosen email service provider. Below are the configuration options for AWS SES, SendGrid & SMTP:

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

#### SMTP configuration

```yml
smtp:
  mail:
    username: <your-smtpserver-username>
    properties:
      mail:
        smtp:
          starttls:
            enable: true
          auth: true
    host: smtp.gmail.com
    port: 587
    password: <your-smtpserver-password>
```

#### MailService Type

```yml
mail:
  service:
    type: <mail-service-type>
```

### Usage
1. To send an simple mail using MailService, follow these steps:
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
2. You may also use custom HTML template. You can provide your custom HTML template like this -
    ```java
    mailContent.setHtmlTemplate(new File(html-file-location));
    ```
    If you have dynamic field in your HTML template, you may pass these fields through hashmap.
    ```java
    HashMap<String, Object> objectHashMap = new HashMap<>();
    objectHashMap.put("fieldName", value);
    mailContent.setObjectMap(objectHashMap);
    ```


