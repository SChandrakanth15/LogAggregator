package com.elixrlabs.logaggregator.writer;

import com.elixrlabs.logaggregator.constants.LogAggregatorConstants;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
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
        try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(outputFilePath))) {
            for (String logEntry : mergedLogs) {
                fileWriter.write(logEntry + System.lineSeparator());
            }
        } catch (IOException ioException) {
            System.out.println(LogAggregatorConstants.IO_EXCEPTION + ioException.getMessage());
        }
    }
}
