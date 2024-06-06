package com.elixrlabs;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LogAggregator {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Please provide the folder path");
            return;
        }
        String folderPath = args[0];
        System.out.println("Processing…");
        System.out.println("The folder path is: " + folderPath);

        List<String> mergedLogs = new ArrayList<>();
        // The output file
        String outputFilePath = "merged_logs.txt";
        // Processing the log files and merge log statements.
        processLogFiles(folderPath, mergedLogs);
        // Sorting the merged logs with the help of the timestamp.
        sortLogsByTimestamp(mergedLogs);

        writeMergedLogsToFile(mergedLogs, outputFilePath);
        System.out.println("Merged log statements are written to " + outputFilePath);
    }

    public static void processLogFiles(String folderPath, List<String> mergedLogs) {
        // To get the files in the folder and read them all.
        File folder = new File(folderPath);
        File[] files = folder.listFiles();
        if (files == null) {
            System.err.println("The folder is empty or it is not a directory");
            return;
        }
        for (File file : files) {
            if (file.isFile()) {
                System.out.println("Processing file: " + file.getName());
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        mergedLogs.add(line);
                    }
                } catch (FileNotFoundException e) {
                    System.err.println("File not found: " + file.getName());
                    e.printStackTrace();
                } catch (IOException e) {
                    System.err.println("Error reading file: " + file.getName());
                    e.printStackTrace();
                }
            }
        }
    }

    public static void sortLogsByTimestamp(List<String> mergedLogs) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Collections.sort(mergedLogs, new Comparator<String>() {
            @Override
            public int compare(String log1, String log2) {
                try {
                    String timeStamp1 = extractTimestamp(log1);
                    String timeStamp2 = extractTimestamp(log2);
                    return dateFormat.parse(timeStamp1).compareTo(dateFormat.parse(timeStamp2));
                } catch (ParseException e) {
                    throw new RuntimeException("Error parsing log timestamps", e);
                }
            }
        });
    }

    private static String extractTimestamp(String log) {
        return log.substring(0, 23);
    }

    public static void writeMergedLogsToFile(List<String> mergedLogs, String outputFilePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
            for (String log : mergedLogs) {
                writer.write(log);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing merged log statements to file");
            e.printStackTrace();
        }
    }
}
