package com.elixrlabs.logaggregator.constants;

import java.security.PublicKey;

public class LogAggregatorConstants {
    public static final String ENTER_PATH_FOLDER_OF_LOGFILES = " Enter the path to the folder containing log files: ";
    public static final String ERROR_DURING_PROCESSING_MESSAGE = " Error occurred during processing: ";
    public static final String INVALID_FOLDER_PATH_MESSAGE = "\n Please enter a valid folder path.";
    public static final String LINE_SEPARATOR = " -------------------------------------------";
    public static final String NEW_LINE_CHAR = "\n";
    public static final String NO_LOGS_FOUND_MESSAGE = " No log statements found in the log files.";
    public static final String NULL_OR_EMPTY_ERROR_MESSAGE = " cannot be null or empty.";
    public static final String OUTPUT_FILE_PATH = "C:\\elixrlabs\\chandrakanth\\merged_logs.txt";
    public static final String PROCESSING_FAILED_MESSAGE = " Processing failed. Unable to create merged log file.";
    public static final String PROCESSING_MESSAGE = " Processing...";
    public static final String TIMESTAMP_REGEX = "\\b(\\d{4}[-/]\\d{2}[-/]\\d{2}|\\d{2}[-/]\\d{2}[-/]\\d{4})\\b";
    public static final String WELCOME_MESSAGE = " Welcome to LogAggregator Tool";
    public static final String SUCCESS_RESULT = "success";
    public static final String FAILURE_RESULT = "failure";
    public static final String SIMPLE_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String LOG_EXTENSION=".log";
    public static final String IO_EXCEPTION ="No log files found in the directory.";
}
