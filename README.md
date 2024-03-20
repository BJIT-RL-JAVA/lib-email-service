# MailService

MailService is a Java-based library that provides functionality for sending emails. It offers a simple and easy-to-use API to integrate email sending capabilities into your Java applications.

## Features

- Support for 3 mail services (AWS SES, SendGrid, SMTP)
- Send emails with attachments
- Support for specifying recipients (To, Cc, Bcc)
- Customizable email subject and body
- Customizable HTML template
- Automatic validation of email content, including attachments
- Integration with Jakarta Bean Validation for input validation

## Installation

To use MailService in your project, you can include it as a dependency in your Maven or Gradle build file.

### Maven

Add the following dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>com.example</groupId>
    <artifactId>mailservice</artifactId>
    <version>1.0.0</version>
</dependency>
```
