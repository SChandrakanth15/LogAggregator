package com.elixrlabs.logaggregator.validator;

import java.io.File;

public class LogAggregatorValidator {
    /**
     * Validates if the provided directoryPath is a valid directory.
     *
     * @param directoryPath the directory directoryPath to validate
     * @return true if the directoryPath is a valid directory, false otherwise
     */
    public boolean isvalidDirectory(String directoryPath) {
        File fileDirectory = new File(directoryPath);
        return fileDirectory.exists() && fileDirectory.isDirectory();
    }

    /**
     * Validates if the provided string is not null and not empty.
     *
     * @param pathString the string to validate
     * @return true if the string is not null and not empty, false otherwise
     */
    public boolean isValidPathString(String pathString) {
        return pathString != null && !pathString.trim().isEmpty();
    }

    /**
     * Validates if the provided file filePath is a valid file.
     *
     * @param filePath the file filePath to validate
     * @return true if the filePath is a valid file, false otherwise
     */
    public boolean isValidFile(String filePath) {
        File file = new File(filePath);
        return file.exists() && file.isFile();
    }
}
