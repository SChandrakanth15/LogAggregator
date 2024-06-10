package com.elixrlabs;

import java.io.File;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProcessingTask implements Runnable {
    private File folder;

    public ProcessingTask(File folder) {
        this.folder = folder;
    }

    @Override
    public void run() {
        File[] files = folder.listFiles();
        if (files != null && files.length > 0) {
            // Merge log statements from multiple files, sort based on timestamp, and store in a HashMap
            Map<String, List<String>> logsByTimestamp = mergeAndSortLogs(files);
            // Write merged and sorted logs to a file
            writeLogsToFile(logsByTimestamp);
            System.out.println("Logs files have been merged, sorted, and stored in merged_logs.txt");
        } else {
            System.out.println("Folder is empty.");
        }
    }

    private Map<String, List<String>> mergeAndSortLogs(File[] files) {
        Map<String, List<String>> logsByTimestamp = new HashMap<>();

        Pattern pattern = Pattern.compile("^\\d{4}/\\d{2}/\\d{2}"); // Regex pattern for timestamp

        // Read log statements from each file and merge them
        for (File file : files) {
            if (file.isFile()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        Matcher matcher = pattern.matcher(line);
                        if (matcher.find()) {
                            String timestamp = matcher.group(); // Extract timestamp
                            if (!logsByTimestamp.containsKey(timestamp)) {
                                logsByTimestamp.put(timestamp, new ArrayList<>());
                            }
                            logsByTimestamp.get(timestamp).add(line);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // Sort logs by timestamp
        for (List<String> logs : logsByTimestamp.values()) {
            Collections.sort(logs);
        }

        return logsByTimestamp;
    }

    private void writeLogsToFile(Map<String, List<String>> logsByTimestamp) {
        try (FileWriter writer = new FileWriter("merged_logs.txt")) {
            for (Map.Entry<String, List<String>> entry : logsByTimestamp.entrySet()) {
                for (String log : entry.getValue()) {
                    writer.write(log + System.lineSeparator());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
