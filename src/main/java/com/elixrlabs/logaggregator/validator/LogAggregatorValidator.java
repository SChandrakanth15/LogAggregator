package com.elixrlabs.logaggregator.validator;

import java.io.File;

public class LogAggregatorValidator {
    /**
     * Validates if the provided path is a valid directory.
     *
     * @param path the directory path to validate
     * @return true if the path is a valid directory, false otherwise
     */
    public  boolean isvalidDirectory(String path) {
        File fileDirectory = new File(path);
        return fileDirectory.exists() && fileDirectory.isDirectory();
    }

    /**
     * Validates if the provided string is not null and not empty.
     *
     * @param str the string to validate
     * @return true if the string is not null and not empty, false otherwise
     */
    public  boolean isNotNullOrEmpty(String str) {
        return str != null && !str.trim().isEmpty();
    }

    /**
     * Validates if the provided file path is a valid file.
     *
     * @param path the file path to validate
     * @return true if the path is a valid file, false otherwise
     */
    public  boolean isValidFile(String path) {
        File file = new File(path);
        return file.exists() && file.isFile();
    }
}
