package com.elixrlabs.logaggregator.jdbc;

import com.elixrlabs.logaggregator.constants.AuditTableConstants;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuditEntryOperation {
    public static void main(String[] args) {
        try {
            Class.forName(AuditTableConstants.DRIVER_CLASS_NAME);
            try (Connection databaseConnection = DriverManager.getConnection(AuditTableConstants.URL, AuditTableConstants.USER, AuditTableConstants.PASSWORD)) {
                AuditLogEntry auditLogEntry = new AuditLogEntry();
                // Call the insertDataIntoAuditTable method with both Connection and AuditTable objects
                insertDataIntoAuditTable(databaseConnection, auditLogEntry);
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        } catch (ClassNotFoundException classNotFoundException) {
            classNotFoundException.printStackTrace();
        }
    }

    public static void insertDataIntoAuditTable(Connection databaseConnection, AuditLogEntry auditLogEntry) throws SQLException {
        try (PreparedStatement insertStatement = databaseConnection.prepareStatement(AuditTableConstants.INSERT_SQL, PreparedStatement.RETURN_GENERATED_KEYS)) {
            insertStatement.setString(1, auditLogEntry.getPathfolder());
            insertStatement.setInt(2, auditLogEntry.getNo_of_files());
            insertStatement.setString(3, auditLogEntry.getName_of_file());
            insertStatement.setString(4, auditLogEntry.getDate_time_of_operation());
            insertStatement.setString(5, auditLogEntry.getResult());
            insertStatement.setString(6, auditLogEntry.getOutput_file_name());
            insertStatement.setString(7, auditLogEntry.getError_message());

            int rowsInserted = insertStatement.executeUpdate();
            if (rowsInserted > 0) {
                //System.out.println("A new record inserted into the audit table.");
                try (ResultSet generatedKeys = insertStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int generatedSerialNumber = generatedKeys.getInt(1);
                        System.out.println("Generated serial number: " + generatedSerialNumber);
                    }
                }
            } else {
                System.out.println("Failed to insert the record into the audit table.");
            }
        }
    }
}
