package com.elixrlabs;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LogAggregator {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("please provide the folder path");
            return;
        }
        String folderPath = args[0];
        System.out.println("Processing…");
        System.out.println("The folder path is : " + folderPath);

        List<String> mergedLogs = new ArrayList<>();

    }

    public static void processLogFiles(String folderPath, List<String> mergedLogs) {
        //to get the files in the folder and read them all.
        File folder = new File(folderPath);
        File[] files = folder.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                System.out.println("File " + file.getName());
            }
            try {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line;
                while ((line = reader.readLine()) != null) {
                   // System.out.println(line);
                    mergedLogs.add(line);
                }
                reader.close();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }
}

