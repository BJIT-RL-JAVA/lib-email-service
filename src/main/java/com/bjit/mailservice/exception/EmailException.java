/**
 * Created by BJIT|| BJIT-R&D
 * Date: 1/25/2024
 * Time: 4:06 PM
 * Project Name: lib-email-service
 */

package com.bjit.mailservice.exception;

public class EmailException extends Exception{
    public EmailException(String message) {
        super(message);
    }

    public EmailException(String message, Throwable cause) {
        super(message, cause);
    }
}
