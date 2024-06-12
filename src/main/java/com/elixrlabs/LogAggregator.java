package com.elixrlabs;

import com.elixrlabs.utils.LogAggregatorConstants;
import com.elixrlabs.utils.Validations;

import java.io.File;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutionException;

/**
 * LogAggregator class is the main entry point for the log aggregation application.
 * It handles user input, validates the input folder, and processes log files asynchronously.
 */
public class LogAggregator {
    /**
     * The main method to start the log aggregation process.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        LogAggregator logAggregator = new LogAggregator();
        logAggregator.start();
    }

    /**
     * Starts the log aggregation process by interacting with the user
     * validating the input, and processing the logs.
     */
    private void start() {
        System.out.println(LogAggregatorConstants.WELCOME_MESSAGE);
        System.out.println(LogAggregatorConstants.UNDER_LINE);

        String folderPath = getUserInputFolderPath();
        if (folderPath == null) {
            return;
        }

        System.out.println(LogAggregatorConstants.PROCESSING_MESSAGE);

        String outputFile = processLogs(folderPath, LogAggregatorConstants.OUTPUT_FILE_PATH);
        if (outputFile != null) {
            File file = new File(outputFile);
            System.out.println(LogAggregatorConstants.NEW_LINE + " Output filename : " + file.getName() + LogAggregatorConstants.NEW_LINE + " The location is " + file.getAbsolutePath());
        } else {
            System.out.println(LogAggregatorConstants.PROCESSING_FAILED_MESSAGE);
        }
    }

    /**
     * Prompts the user to input the folder path and validates it.
     *
     * @return the validated folder path, or null if the validation fails
     */
    private String getUserInputFolderPath() {
        Scanner scanner = new Scanner(System.in);
        System.out.print(LogAggregatorConstants.ENTER_PATH_FOLDER_OF_LOGFILES);
        String folderPath = scanner.nextLine().trim();
        scanner.close();

        if (!Validations.isvalidDirectory(folderPath)) {
            System.err.println(LogAggregatorConstants.INVALID_FOLDER_PATH_MESSAGE);
            return null;
        }

        return folderPath;
    }

    /**
     * Processes the log files in the specified folder asynchronously.
     *
     * @param folderPath     the path of the folder containing log files.
     * @param outputFilePath the path where the outpu.t file will be saved
     * @return the path of the output file if processing is successful, null otherwise
     */
    private String processLogs(String folderPath, String outputFilePath) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Callable<String> task = new ProcessingTask(folderPath, outputFilePath);
        Future<String> future = executorService.submit(task);

        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            System.err.println(LogAggregatorConstants.ERROR_DURING_PROCESSING_MESSAGE + e.getMessage());
            return null;
        } finally {
            executorService.shutdown();
        }
    }
}
