package com.elixrlabs;

import com.elixrlabs.utils.LogAggregatorConstants;
import com.elixrlabs.utils.Validations;

import java.io.BufferedReader;
import java.io.FileWriter;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The ProcessingTask class is responsible for processing log files located in a specified directory.
 * It reads the log files, merges and sorts their content based on timestamps, and writes the result to an output file.
 */
public class ProcessingTask implements Callable<String> {
    private final String folderPath;
    private final String outputPath;

    /**
     * Constructs a ProcessingTask with the specified folder path and output path.
     *
     * @param folderPath the path to the directory containing log files
     * @param outputPath the path to the output file where merged logs will be written
     */
    public ProcessingTask(String folderPath, String outputPath) {
        if (!Validations.isNotNullOrEmpty(folderPath)) {
            System.err.println(folderPath + LogAggregatorConstants.NULL_OR_EMPTY_ERROR_MESSAGE);
        }
        if (!Validations.isNotNullOrEmpty(outputPath)) {
            System.err.println(outputPath + LogAggregatorConstants.NULL_OR_EMPTY_ERROR_MESSAGE);
        }
        this.folderPath = folderPath;
        this.outputPath = outputPath;
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
            writeLogsToFile(mergedLogs, outputPath);
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
                    try (BufferedReader reader = Files.newBufferedReader(file)) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            Matcher matcher = pattern.matcher(line);
                            if (matcher.find()) {
                                String timestamp = matcher.group();
                                logsByTimestampMap.computeIfAbsent(timestamp, k -> new ArrayList<>()).add(line);
                            }
                        }
                    }
                }
            }
        }

        List<String> mergedLogs = new ArrayList<>();
        // Extract timestamps and sort
        List<String> timestamps = new ArrayList<>(logsByTimestampMap.keySet());
        Collections.sort(timestamps);

        // Append logs sorted by timestamps
        for (String timestamp : timestamps) {
            List<String> logs = logsByTimestampMap.get(timestamp);
            mergedLogs.addAll(logs);
        }

        return mergedLogs;
    }

    /**
     * Writes the merged and sorted log entries to the specified output file.
     *
     * @param logs       logs the list of merged and sorted log entries
     * @param outputPath outputPath the path to the output file
     * @throws IOException if an error occurs while writing the log files
     */
    private void writeLogsToFile(List<String> logs, String outputPath) throws IOException {
        try (FileWriter writer = new FileWriter(outputPath)) {
            for (String log : logs) {
                writer.write(log + System.lineSeparator());
            }
        }
    }

}
