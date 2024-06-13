package com.elixrlabs.logaggregator;

import com.elixrlabs.logaggregator.constants.LogAggregatorConstants;
import com.elixrlabs.logaggregator.reader.LogFileReader;
import com.elixrlabs.logaggregator.validator.LogAggregatorValidator;
import com.elixrlabs.logaggregator.writer.LogFileWriter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.DirectoryStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Collections;
import java.util.concurrent.Callable;
import java.util.regex.Pattern;

/**
 * The ProcessingTask class is responsible for processing log files located in a specified directory.
 * It reads the log files, merges and sorts their content based on timestamps, and writes the result to an output file.
 */
public class ProcessingTask implements Callable<String> {
    private final String folderPath;
    private final String outputPath;
    private final LogFileReader logFileReader;

    /**
     * Constructs a ProcessingTask with the specified folder path and output path.
     *
     * @param folderPath the path to the directory containing log files
     * @param outputPath the path to the output file where merged logs will be written
     */
    public ProcessingTask(String folderPath, String outputPath) {
        LogAggregatorValidator validator = new LogAggregatorValidator();
        if (!validator.isNotNullOrEmpty(folderPath)) {
            System.err.println(folderPath + LogAggregatorConstants.NULL_OR_EMPTY_ERROR_MESSAGE);
        }
        if (!validator.isNotNullOrEmpty(outputPath)) {
            System.err.println(outputPath + LogAggregatorConstants.NULL_OR_EMPTY_ERROR_MESSAGE);
        }
        this.folderPath = folderPath;
        this.outputPath = outputPath;
        this.logFileReader = new LogFileReader();
    }

    /**
     * The call method is the entry point for the task when executed by an ExecutorService.
     * It merges and sorts the log files and writes the output to a file.
     *
     * @return the path to the output file
     * @throws Exception if an error occurs during processing
     */
    @Override
    public String call() throws Exception {
        List<String> mergedLogs = mergeAndSortLogs();
        if (mergedLogs != null) {
            LogFileWriter.writeLogsToFile(mergedLogs, outputPath);
            return outputPath;
        } else {
            throw new IOException(LogAggregatorConstants.NO_LOGS_FOUND_MESSAGE);
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
        Pattern pattern = Pattern.compile(LogAggregatorConstants.TIMESTAMP_REGEX); // Regex pattern for timestamp
        try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(Paths.get(folderPath))) {
            for (Path file : dirStream) {
                if (Files.isRegularFile(file)) {
                    LogFileReader.readLogsFromFile(file, pattern, logsByTimestampMap);
                }
            }
        }
        List<String> mergedLogs = new ArrayList<>();
        // Extract timestamps and sort
        List<String> timestamps = new ArrayList<>(logsByTimestampMap.keySet());
        Collections.sort(timestamps);
        // Append logs sorted by timestamps
        for (String timestamp : timestamps) {
            List<String> timestampLogs = logsByTimestampMap.get(timestamp);
            mergedLogs.addAll(timestampLogs);
        }
        return mergedLogs;
    }
}