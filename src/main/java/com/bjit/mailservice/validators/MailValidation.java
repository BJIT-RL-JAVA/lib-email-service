package com.bjit.mailservice.validators;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Interface for validating files based on compatibility criteria.
 * Implementations should provide methods for various file validation checks.
 *
 * @author Mallika Dey
 */
public interface MailValidation {

    /**
     * Checks the compatibility of a file based on its existence and file type.
     *
     * @param file The file to be checked for compatibility.
     * @throws IllegalArgumentException if the file is null or if it has an unsupported file type.
     */
    default void checkFileCompatibility(File file) throws IOException {
        if (!file.exists()) {
            throw new IOException("File does not exist: " + file.getAbsolutePath());
        }
        String fileName = file.getName();
        String fileExtension = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();

        List<String> blockedFileTypes = Arrays.asList(
                "ade", "adp", "apk", "appx", "appxbundle", "bat", "cab", "chm", "cmd", "com", "cpl",
                "diagcab", "diagcfg", "diagpack", "dll", "dmg", "ex", "ex_", "exe", "hta", "img", "ins",
                "iso", "isp", "jar", "jnlp", "js", "jse", "lib", "lnk", "mde", "mjs", "msc", "msi",
                "msix", "msixbundle", "msp", "mst", "nsh", "pif", "ps1", "scr", "sct", "shb", "sys",
                "vb", "vbe", "vbs", "vhd", "vxd", "wsc", "wsf", "wsh", "xll");
        if (blockedFileTypes.contains(fileExtension)) {
            throw new RuntimeException("Unsupported file type: " + fileExtension + " - " + fileName);
        }
    }
    default boolean validateHtmlTemplate(File htmlTemplate){
        if (htmlTemplate != null && htmlTemplate.isFile() && htmlTemplate.getName().endsWith(".html")) {
            // Check file size
            long fileSizeInBytes = htmlTemplate.length();
            long fileSizeInMB = fileSizeInBytes / (1024 * 1024); // Convert bytes to MB
            if (fileSizeInMB <= 5) {
                return true;
            } else {
                throw new IllegalArgumentException("File size exceeds the maximum limit of 5MB.");
            }
        } else {
            throw new IllegalArgumentException("Invalid File! Please provide an HTML file only.");
        }
    }
}
