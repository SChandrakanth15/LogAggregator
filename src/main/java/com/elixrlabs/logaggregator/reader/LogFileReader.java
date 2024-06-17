package com.elixrlabs.logaggregator.reader;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The LogFileReader class is responsible for reading log entries from a file and storing them in a map
 * based on their timestamps.
 */
public class LogFileReader {
    /**
     * Reads log entries from the specified logFilePath and stores them in the provided map based on their timestamps.
     *
     * @param logFilePath the path to the log logFilePath.
     * @param timestampPattern the timestampPattern to match timestamps in the log entries.
     * @param logsByTimestampMap the map to store log entries categorized by timestamps
     * @throws IOException if an error occurs while reading the log logFilePath
     */
    public  void readLogsFromFile(Path logFilePath, Pattern timestampPattern, Map<String, List<String>> logsByTimestampMap) throws IOException {
        try (BufferedReader logFileReader = Files.newBufferedReader(logFilePath)) {
            String logEntryLine;
            while ((logEntryLine = logFileReader.readLine()) != null) {
                Matcher timestampMatcher = timestampPattern.matcher(logEntryLine);
                if (timestampMatcher.find()) {
                    String timestamp = timestampMatcher.group();
                    logsByTimestampMap.computeIfAbsent(timestamp, k -> new ArrayList<>()).add(logEntryLine);
                }
            }
        }
    }
}