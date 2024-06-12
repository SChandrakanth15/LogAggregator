package com.chandrakanthdemo;

import java.io.*;
import java.io.File;

class ProcessingTask implements Runnable {
    private File folder;

    public ProcessingTask(File folder) {
        this.folder = folder;
    }

    @Override
    public void run() {
        File[] files = folder.listFiles();
        if (files != null && files.length > 0) {
            try (FileWriter writer = new FileWriter("merged_logs.txt")) {
                for (File file : files) {
                    if (file.isFile()) {
                        System.out.println("Processing file: " + file.getName());
                        mergeLogFile(file, writer);
                    }
                }
                System.out.println("Logs files have been merged into merged_logs.txt");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Folder is empty.");
        }

        // Simulate some processing time
        try {
            Thread.sleep(2000); // Simulating 2 seconds of processing time
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private String mergeLogFile(File file, FileWriter writer) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                writer.write(line + System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
