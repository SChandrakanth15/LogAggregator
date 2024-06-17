package com.elixrlabs.logaggregator.writer;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class LogFileWriter {
    /**
     * Writes the merged and sorted log entries to the specified output file.
     *
     * @param mergedLogs     mergedLogs the list of merged and sorted log entries.
     * @param outputFilePath outputPath the path to the output file.
     * @throws IOException if an error occurs while writing the log files.
     */
    public void writeLogsToFile(List<String> mergedLogs, String outputFilePath) throws IOException {
        try (FileWriter fileWriter = new FileWriter(outputFilePath)) {
            for (String logEntry : mergedLogs) {
                fileWriter.write(logEntry + System.lineSeparator());
            }
        }
    }
}
