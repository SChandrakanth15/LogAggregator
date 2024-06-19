package com.elixrlabs.logaggregator.jdbc;

import com.elixrlabs.logaggregator.constants.AuditTableConstants;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuditEntryOperation {
    public void insertDataIntoAuditTable(Connection databaseConnection, AuditLogEntry auditLogEntry) throws SQLException {
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
                try (ResultSet generatedKeys = insertStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        System.out.println(AuditTableConstants.GENERATED_SERIAL_NUMBER + generatedKeys.getInt(1));
                    }
                }
            } else {
                System.out.println(AuditTableConstants.RECORD_INSERTED_FAILED);
            }
        }
    }
}
