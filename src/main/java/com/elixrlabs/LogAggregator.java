package com.elixrlabs;

import java.io.File;
import java.util.Scanner;

public class LogAggregator {
    public static void main(String[] args) {
        System.out.println("Welcome to LogAggregator Tool");
        Scanner scanner = new Scanner(System.in);

        // Ask user to input the folder path
        System.out.print("Enter the path to the folder containing log files: ");
        String folderPath = scanner.nextLine().trim();
        scanner.close();
        // Check if the provided path exists and is a directory
        File folder = new File(folderPath);
        if (!folder.exists() || !folder.isDirectory()) {
            System.err.println("please enter a valid folder");
            return;
        }
        // Show processing message
        System.out.println("Processing...");
        File[] files = folder.listFiles();
        if (files != null) {
            System.out.println("Files in the folder:");
            for (File file : files) {
                System.out.println(file.getName());
            }
        } else {
            System.out.println("Folder is empty.");
        }
    }
}
