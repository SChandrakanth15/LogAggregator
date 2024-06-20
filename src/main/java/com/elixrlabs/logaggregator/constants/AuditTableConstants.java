package com.elixrlabs.logaggregator.constants;

public class AuditTableConstants {
    public static final String DRIVER_CLASS_NAME = "com.mysql.cj.jdbc.Driver";
    public static final String INSERT_SQL = "INSERT INTO audit (pathfolder, no_of_files, name_of_file, date_time_of_operation, result, output_file_name, error_message) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?)";
    public static final String PASSWORD = "ROOT";
    public static final String URL = "jdbc:mysql://127.0.0.1:3306/logaggregator";
    public static final String USER = "root";
    public static final String GENERATED_SERIAL_NUMBER = "Generated serial number ";
    public static final String RECORD_INSERTED_FAILED = "Failed to insert the record into the audit table.";
}
