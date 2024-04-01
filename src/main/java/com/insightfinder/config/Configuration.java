package com.insightfinder.config;

import java.util.List;

public class Configuration {
    private static final List<String> errorMessageFiles = List.of(
            "ErrorMessages1.txt",
            "ErrorMessages2.txt",
            "ErrorMessages3.txt"
    );

    public static List<String> getErrorMessageFiles() {
        return errorMessageFiles;
    }
}
