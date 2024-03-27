/**
 * Created by Mohammad Khalid Hasan|| BJIT-R&D
 * Since: 3/27/2024
 * Version: 1.0
 */

package com.bjit.mailservice.constants;

public class MessageConstant {
    //MailConfig Messges
    public static final String invalid_email_service = "Invalid mail service type: ";

    // MailContent validation messages
    public static final String from_email_address_invalid = "Invalid 'from' email address. ";
    public static final String to_email_address_empty = "Please provide at least one recipient email address.";

    //AWS-SES service class
    public static final String aws_sendmail_log_message = "Attempting to send an template email through Amazon SES "
            + "using the AWS SDK for Java...";

    //SandGrid Service class
    public static final String sandgrid_api_call = "Calling sendgrid api {} {}";
    public static final String io_exception = "IO exception occur in request ";

    //SMTP Mail Service
    public static final String sendMail_success = "Mail has been sent successfully.";
    public static final String sendMail_error = "Error sending simple email ";
    public static final String successfully_sending_html_template_email = "HTML template mail has been sent successfully.";
    public static final String error_sending_html_template_email = "Error sending HTML template email";

    public static final String process_attachment_error = "Error sending email attachments";

    //LoadMailTemplate Messages
    public static final String unsuccessful_template_loading = "Error loading HTML template file!";

    //MailContentValidation Messages
    public static final String not_null_MailContent = "MailContent must not be null";

    //MailValidation Messages
    public static final String unsupported_file_type = "Unsupported file type:";
    public static final String fileNotExists = "File does not exist:";
    public static final String maximum_attachment_file_size = "File size exceeds the maximum limit of 5MB.";
    public static final String html_file_type_mismatched = "Invalid File! Please provide an HTML file only.";

    //ValidAttachmentSize MessageConstant
    public static final String valid_Attachment_Size_message = "Total size of attachments must not exceed 25 MB.";


}
