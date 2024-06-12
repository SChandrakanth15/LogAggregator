package com.chandrakanthdemo;

//remove empty spaces/lines
import java.io.File;
import java.util.Scanner;

/**
 * jvsjgkkhlhe
 * jhsgjhd
 * hdg
 */
public class LogAggregatorc { //Clss name should not be as project name
    //Give class name as noun

    /**
     * jygwqdg
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("Welcome to LogAggregator Tool");
        System.out.println("-------------------------------------------");
        Scanner scanner = new Scanner(System.in);//

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
        //to show the files in the folder
        File[] files = folder.listFiles();
        if (files != null) {
            System.out.println("Files in the folder:");
            for (File file : files) {
                System.out.println(file.getName());
            }
        } else {
            System.out.println("Folder is empty.");
        }
        Thread processingThread = new Thread(new ProcessingTask(folder));
        processingThread.start();
        // Main thread can continue with other tasks
        // For demonstration, let's just print dots while processing
        while (processingThread.isAlive()) {
            try {
                Thread.sleep(500); // Sleep for 0.5 seconds
                System.out.print("."); // Print dots to indicate processing
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("\nProcessing completed.");
    }
}
//folderPath = C:\elixrlabs\corejavapplication\logfiles