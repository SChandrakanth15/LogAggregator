package com.elixrlabs.logaggregator.jdbc;

import lombok.Data;

@Data
public class AuditLogEntry {
    private int slno;
    private String pathfolder;
    private int no_of_files;
    private String name_of_file;
    private String date_time_of_operation;
    private String result;
    private String output_file_name;
    private String error_message;
}
