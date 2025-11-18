package com.example.mergesort;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileIO {

    public static List<CustomerRecord> readRecords(String filename) throws IOException {
        List<CustomerRecord> records = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    records.add(CustomerRecord.fromString(line));
                }
            }
        }
        
        return records;
    }

    public static void writeRecords(String filename, List<CustomerRecord> records) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (CustomerRecord record : records) {
                writer.write(record.toString());
                writer.newLine();
            }
        }
    }

    public static void createTestData() throws IOException {
        System.out.println("Creating test data files...");
        
        List<CustomerRecord> eastRecords = new ArrayList<>();
        eastRecords.add(new CustomerRecord(1, "last-1", "first-1", 5423, "comment-1"));
        eastRecords.add(new CustomerRecord(5, "last-5", "first-5", 12323, "comment-5"));
        eastRecords.add(new CustomerRecord(10, "last-10", "first-10", 653, "comment-10"));
        eastRecords.add(new CustomerRecord(50, "last-50", "first-50", 5050, "comment-50"));
        eastRecords.add(new CustomerRecord(25, "last-25", "first-25", 7725, "comment-25"));
        eastRecords.add(new CustomerRecord(75, "last-75", "first-75", 1175, "comment-75"));
        
        writeRecords("test-file-1.txt", eastRecords);
        
        List<CustomerRecord> westRecords = new ArrayList<>();
        westRecords.add(new CustomerRecord(999, "last-999", "first-999", 1610, "comment-99"));
        westRecords.add(new CustomerRecord(3, "last-03", "first-03", 3331, "comment-03"));
        westRecords.add(new CustomerRecord(30, "last-30", "first-30", 8765, "comment-30"));
        westRecords.add(new CustomerRecord(85, "last-85", "first-85", 4567, "comment-85"));
        westRecords.add(new CustomerRecord(24, "last-24", "first-24", 247, "comment-24"));
        
        writeRecords("test-file-2.txt", westRecords);
    }
}
