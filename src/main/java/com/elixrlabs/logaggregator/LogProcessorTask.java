package com.elixrlabs.logaggregator;

import com.elixrlabs.logaggregator.constants.AuditTableConstants;
import com.elixrlabs.logaggregator.constants.LogAggregatorConstants;
import com.elixrlabs.logaggregator.jdbc.AuditLogEntry;
import com.elixrlabs.logaggregator.jdbc.AuditEntryOperation;
import com.elixrlabs.logaggregator.reader.LogFileReader;
import com.elixrlabs.logaggregator.validator.LogAggregatorValidator;
import com.elixrlabs.logaggregator.writer.LogFileWriter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.DirectoryStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Collections;
import java.util.concurrent.Callable;
import java.util.regex.Pattern;
import java.util.Date;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The ProcessingTask class is responsible for processing log files located in a specified directory.
 * It reads the log files, merges and sorts their content based on timestamps, and writes the result to an output file.
 */
public class LogProcessorTask implements Callable<String> {
    private final String folderPath;
    private final String outputFilePath;
    private final LogFileReader logFileReader;
    private final LogAggregatorValidator validator;

    /**
     * Constructs a ProcessingTask with the specified folder path and output path.
     *
     * @param folderPath the path to the directory containing log files
     * @param outputPath the path to the output file where merged logs will be written
     */
    public LogProcessorTask(String folderPath, String outputPath) {
        this.folderPath = folderPath;
        this.outputFilePath = outputPath;
        this.logFileReader = new LogFileReader();
        this.validator = new LogAggregatorValidator();
    }

    /**
     * The call method is the entry point for the task when executed by an ExecutorService.
     * It merges and sorts the log files and writes the output to a file.
     *
     * @return the path to the output file
     * @throws Exception if an error occurs during processing
     */
    public String call() throws Exception {
        LogFileWriter logFileWriter = new LogFileWriter();
        validatePaths();
        List<String> mergedLogsList = null;
        String processingResult;
        String processingErrorMessage = "";
        AuditEntryOperation auditEntryOperation = new AuditEntryOperation();
        try {
            mergedLogsList = mergeAndSortLogs();
            if (mergedLogsList == null || mergedLogsList.isEmpty()) {
                throw new IOException(LogAggregatorConstants.NO_LOGS_FOUND_MESSAGE);
            }
            logFileWriter.writeLogsToFile(mergedLogsList, outputFilePath);
            processingResult = LogAggregatorConstants.SUCCESS_RESULT;
        } catch (Exception exception) {
            processingResult = LogAggregatorConstants.FAILURE_RESULT;
            processingErrorMessage = exception.getMessage();
        }
        AuditLogEntry auditLogEntry = new AuditLogEntry();

        if (processingResult.equals(LogAggregatorConstants.FAILURE_RESULT)) {
            auditLogEntry.setResult(processingResult);
            auditLogEntry.setError_message(processingErrorMessage);
            auditLogEntry.setPathfolder(folderPath);
            auditLogEntry.setDate_time_of_operation(getCurrentTimeStamp());
        } else {
            auditLogEntry.setPathfolder(folderPath);
            auditLogEntry.setNo_of_files(countFilesInTheDirectory());
            auditLogEntry.setName_of_file(getFileNamesInTheDirectory());
            auditLogEntry.setDate_time_of_operation(getCurrentTimeStamp());
            auditLogEntry.setResult(processingResult);
            auditLogEntry.setOutput_file_name(outputFilePath);
        }
        try (Connection databaseConnection = DriverManager.getConnection(AuditTableConstants.URL, AuditTableConstants.USER, AuditTableConstants.PASSWORD)) {
            auditEntryOperation.insertDataIntoAuditTable(databaseConnection, auditLogEntry);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        if (LogAggregatorConstants.FAILURE_RESULT.equals(processingResult)) {
            throw new IOException(LogAggregatorConstants.PROCESSING_FAILED_MESSAGE + processingErrorMessage);
        }
        return outputFilePath;
    }

    /**
     * Validates the folder and output paths.
     */
    private void validatePaths() {
        if (!validator.isValidPathString(folderPath)) {
            throw new IllegalArgumentException(folderPath + LogAggregatorConstants.NULL_OR_EMPTY_ERROR_MESSAGE);
        }
        if (!validator.isValidPathString(outputFilePath)) {
            throw new IllegalArgumentException(outputFilePath + LogAggregatorConstants.NULL_OR_EMPTY_ERROR_MESSAGE);
        }
    }

    /**
     * Merges and sorts the log entries from all log files in the specified directory.
     *
     * @return a list of merged and sorted log entries
     * @throws IOException if an error occurs while reading the log files
     */
    private List<String> mergeAndSortLogs() throws IOException {
        Map<String, List<String>> logsByTimestampMap = new HashMap<>();
        Pattern timestampPattern = Pattern.compile(LogAggregatorConstants.TIMESTAMP_REGEX);
        Path logFolderPath = Paths.get(folderPath);
        try (DirectoryStream<Path> logFilesStream = Files.newDirectoryStream(logFolderPath, path -> path.toString().endsWith(LogAggregatorConstants.LOG_EXTENSION))) {
            for (Path logFile : logFilesStream) {
                logFileReader.readLogsFromFile(logFile, timestampPattern, logsByTimestampMap);
            }
        }
        if (logsByTimestampMap.isEmpty()) {
            throw new IOException(LogAggregatorConstants.IO_EXCEPTION);
        }
        List<String> mergedLogsList = new ArrayList<>();
        List<String> timestampsList = new ArrayList<>(logsByTimestampMap.keySet());
        Collections.sort(timestampsList);
        for (String timestamp : timestampsList) {
            List<String> logsForTimestamp = logsByTimestampMap.get(timestamp);
            mergedLogsList.addAll(logsForTimestamp);
        }
        return mergedLogsList;
    }

    private int countFilesInTheDirectory() throws IOException {
        try (Stream<Path> paths = Files.list(Paths.get(folderPath))) {
            return (int) paths.filter(Files::isRegularFile).count();
        }
    }

    private String getFileNamesInTheDirectory() throws IOException {
        try (Stream<Path> paths = Files.list(Paths.get(folderPath))) {
            return paths
                    .filter(Files::isRegularFile)
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .collect(Collectors.joining(", "));
        }
    }

    private String getCurrentTimeStamp() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(LogAggregatorConstants.SIMPLE_DATE_FORMAT);
        return dateFormat.format(new Date());
    }
}
