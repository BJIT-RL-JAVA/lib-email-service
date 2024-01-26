package com.bjit.mailservice.services;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public interface MailValidation {
    default void checkFileCompatibility(File file) {
        if (!file.exists()) {
            throw new RuntimeException("File does not exist: " + file.getAbsolutePath());
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
}
