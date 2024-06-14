package com.elixrlabs.logaggregator.writer;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class LogFileWriter {
    /**
     * Writes the merged and sorted log entries to the specified output file.
     *
     * @param logs       logs the list of merged and sorted log entries.
     * @param outputPath outputPath the path to the output file.
     * @throws IOException IOException if an error occurs while writing the log files.
     */
    public void writeLogsToFile(List<String> logs, String outputPath) throws IOException {
        try (FileWriter filewriter = new FileWriter(outputPath)) {
            for (String log : logs) {
                filewriter.write(log + System.lineSeparator());
            }
        }
    }
}
