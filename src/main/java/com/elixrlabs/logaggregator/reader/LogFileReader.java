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
     * Reads log entries from the specified file and stores them in the provided map based on their timestamps.
     *
     * @param file the path to the log file.
     * @param pattern the pattern to match timestamps in the log entries.
     * @param logsByTimestampMap the map to store log entries categorized by timestamps
     * @throws IOException if an error occurs while reading the log file
     */
    public static void readLogsFromFile(Path file, Pattern pattern, Map<String, List<String>> logsByTimestampMap) throws IOException {
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
