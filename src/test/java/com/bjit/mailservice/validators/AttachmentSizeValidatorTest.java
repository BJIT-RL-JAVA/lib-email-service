package com.bjit.mailservice.validators;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Mallika Dey
 */
@SpringBootTest
public class AttachmentSizeValidatorTest {
    @InjectMocks
    private AttachmentSizeValidator attachmentSizeValidator;

    @Mock
    private ConstraintValidatorContext constraintValidatorContext;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void fileSizeMoreThan25MBShouldReturnFalse() {
        ArrayList<File> files = new ArrayList<>(List.of(
                new File("E:\\Mallika Dey\\downloads\\jdk-17.0.10_windows-x64_bin.exe")));
        assertFalse(attachmentSizeValidator.isValid(files, constraintValidatorContext));
    }

    @Test
    public void ListOfFileIsLessThan25MBShouldReturnTrue() {
        ArrayList<File> files = new ArrayList<>(List.of(
                new File("E:\\Mallika Dey\\materials\\others\\sss.txt")));
        assertTrue(attachmentSizeValidator.isValid(files, constraintValidatorContext));
    }

    @Test
    public void emptyFileListShouldReturnTrue() {
        ArrayList<File> files = new ArrayList<>();
        assertThat(attachmentSizeValidator.isValid(files, constraintValidatorContext)).isTrue();
    }

}
