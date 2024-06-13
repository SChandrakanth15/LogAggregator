package com.elixrlabs.logaggregator.jdbc;

public class AuditTable {
    private int slno;
    private String pathfolder;
    private int no_of_files;
    private String name_of_file;
    private String date_time_of_operation;
    private String result;
    private String output_file_name;
    private String error_message;

    public int getSlno() {
        return slno;
    }

    public void setSlno(int slno) {
        this.slno = slno;
    }

    public String getPathfolder() {
        return pathfolder;
    }

    public void setPathfolder(String pathfolder) {
        this.pathfolder = pathfolder;
    }

    public int getNo_of_files() {
        return no_of_files;
    }

    public void setNo_of_files(int no_of_files) {
        this.no_of_files = no_of_files;
    }

    public String getName_of_file() {
        return name_of_file;
    }

    public void setName_of_file(String name_of_file) {
        this.name_of_file = name_of_file;
    }

    public String getDate_time_of_operation() {
        return date_time_of_operation;
    }

    public void setDate_time_of_operation(String date_time_of_operation) {
        this.date_time_of_operation = date_time_of_operation;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getOutput_file_name() {
        return output_file_name;
    }

    public void setOutput_file_name(String output_file_name) {
        this.output_file_name = output_file_name;
    }

    public String getError_message() {
        return error_message;
    }

    public void setError_message(String error_message) {
        this.error_message = error_message;
    }

    @Override
    public String toString() {
        return "AuditTable{" +
                "slno=" + slno +
                ", pathfolder='" + pathfolder + '\'' +
                ", no_of_files=" + no_of_files +
                ", name_of_file='" + name_of_file + '\'' +
                ", date_time_of_operation='" + date_time_of_operation + '\'' +
                ", result='" + result + '\'' +
                ", output_file_name='" + output_file_name + '\'' +
                ", error_message='" + error_message + '\'' +
                '}';
    }
}
