package com.elixrlabs;

import java.io.File;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ProcessingTask implements Runnable {
    private File folder;

    public ProcessingTask(File folder) {
        this.folder = folder;
    }

    @Override
    public void run() {
        // For demonstration, let's read contents of all files in the folder
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    System.out.println("File: " + file.getName());
                    readLogFile(file);
                }
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

    private void readLogFile(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Process each line here (you can add your logic)
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
