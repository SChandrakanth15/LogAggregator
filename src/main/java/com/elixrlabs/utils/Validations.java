package com.elixrlabs.utils;

import java.io.File;

public class Validations {
    /**
     * Validates if the provided path is a valid directory.
     *
     * @param path the directory path to validate
     * @return true if the path is a valid directory, false otherwise
     */
    public static boolean isvalidDirectory(String path) {
        File directory = new File(path);
        return directory.exists() && directory.isDirectory();
    }

    /**
     * Validates if the provided string is not null and not empty.
     *
     * @param str the string to validate
     * @return true if the string is not null and not empty, false otherwise
     */
    public static boolean isNotNullOrEmpty(String str) {
        return str != null && !str.trim().isEmpty();
    }

    /**
     * Validates if the provided file path is a valid file.
     *
     * @param path the file path to validate
     * @return true if the path is a valid file, false otherwise
     */
    public static boolean isValidFile(String path) {
        File file = new File(path);
        return file.exists() && file.isFile();
    }
}
