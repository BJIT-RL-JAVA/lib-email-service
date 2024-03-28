package com.bjit.mailservice.validators;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Mallika Dey
 */
@SpringBootTest
public class MailValidationTest {
    private MailValidation mailValidation;

    @BeforeEach
    void setUp() {
        mailValidation = new MailValidation() {
        };
    }

    @Test
    public void checkFileCompatibilityTest_FileNotExist_ShouldThrowException() {
        File file = new File("C:\\Users\\Bjit\\Downloads\\Git.exe");

        assertThrows(IOException.class, () -> mailValidation.checkFileCompatibility(file));
    }

    @Test
    public void checkFileCompatibilityTest_InvalidFileType_ShouldThrowException() {
        File file = new File("C:\\Users\\Bjit\\Downloads\\Git-2.43.0-64-bit.exe");

        assertThrows(RuntimeException.class, () -> mailValidation.checkFileCompatibility(file));
    }
}
