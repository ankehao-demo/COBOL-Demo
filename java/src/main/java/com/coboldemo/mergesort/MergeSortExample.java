package com.coboldemo.mergesort;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Migrated from: merge_sort/merge_sort_test.cbl
 * Original author: Erik Eriksen (2022-05-09, updated 2022-05-13)
 * Purpose: Demonstrates MERGE and SORT operations on file records.
 *
 * The program:
 * 1. Creates two test data files with customer records
 * 2. MERGE: reads both files, merges by customer ID ascending
 * 3. SORT: sorts the merged result by contract ID descending
 * 4. Displays the results at each step
 */
public class MergeSortExample {

    static class CustomerRecord {
        String id;         // PIC X(5)
        String lastName;   // PIC X(20)
        String firstName;  // PIC X(15)
        String contractId; // PIC X(10)
        String comment;    // PIC X(30)

        CustomerRecord(String id, String lastName, String firstName,
                       String contractId, String comment) {
            this.id = id;
            this.lastName = lastName;
            this.firstName = firstName;
            this.contractId = contractId;
            this.comment = comment;
        }

        String toFixedWidth() {
            return String.format("%-5s%-20s%-15s%-10s%-30s",
                    id, lastName, firstName, contractId, comment);
        }

        static CustomerRecord fromFixedWidth(String line) {
            if (line.length() < 80) {
                line = String.format("%-80s", line);
            }
            return new CustomerRecord(
                    line.substring(0, 5).trim(),
                    line.substring(5, 25).trim(),
                    line.substring(25, 40).trim(),
                    line.substring(40, 50).trim(),
                    line.substring(50, 80).trim()
            );
        }

        @Override
        public String toString() {
            return String.format("ID: %-5s  Name: %-15s %-20s  Contract: %-10s  Comment: %s",
                    id, firstName, lastName, contractId, comment);
        }
    }

    public static void main(String[] args) throws IOException {
        Path tempDir = Files.createTempDirectory("mergesort");
        Path file1 = tempDir.resolve("test_data_1.dat");
        Path file2 = tempDir.resolve("test_data_2.dat");
        Path mergedFile = tempDir.resolve("merged_output.dat");
        Path sortedFile = tempDir.resolve("sorted_output.dat");

        try {
            // Step 1: Create test data files
            System.out.println("Creating test data files...");
            createTestData1(file1);
            createTestData2(file2);

            // Display file 1
            System.out.println();
            System.out.println("File 1 contents:");
            System.out.println("================");
            displayFile(file1);

            // Display file 2
            System.out.println();
            System.out.println("File 2 contents:");
            System.out.println("================");
            displayFile(file2);

            // Step 2: MERGE - read both files and merge by customer ID ascending
            System.out.println();
            System.out.println("MERGE by Customer ID (ascending):");
            System.out.println("=================================");
            List<CustomerRecord> records1 = readRecords(file1);
            List<CustomerRecord> records2 = readRecords(file2);

            List<CustomerRecord> allRecords = new ArrayList<>();
            allRecords.addAll(records1);
            allRecords.addAll(records2);

            // Sort by customer ID ascending (merge sort behavior)
            allRecords.sort(Comparator.comparing(r -> r.id));
            writeRecords(mergedFile, allRecords);
            displayRecords(allRecords);

            // Step 3: SORT - sort merged result by contract ID descending
            System.out.println();
            System.out.println("SORT by Contract ID (descending):");
            System.out.println("=================================");
            allRecords.sort(Comparator.comparing((CustomerRecord r) -> r.contractId).reversed());
            writeRecords(sortedFile, allRecords);
            displayRecords(allRecords);

            System.out.println();
            System.out.println("Output files written to: " + tempDir);

        } finally {
            // Clean up temp files
            Files.deleteIfExists(file1);
            Files.deleteIfExists(file2);
            Files.deleteIfExists(mergedFile);
            Files.deleteIfExists(sortedFile);
            Files.deleteIfExists(tempDir);
        }
    }

    private static void createTestData1(Path file) throws IOException {
        List<CustomerRecord> records = List.of(
                new CustomerRecord("00001", "Smith", "John", "CT-001", "First customer"),
                new CustomerRecord("00003", "Johnson", "Sarah", "CT-003", "Third customer"),
                new CustomerRecord("00005", "Williams", "Bob", "CT-005", "Fifth customer"),
                new CustomerRecord("00007", "Brown", "Alice", "CT-007", "Seventh customer"),
                new CustomerRecord("00009", "Davis", "Charlie", "CT-009", "Ninth customer")
        );
        writeRecords(file, records);
    }

    private static void createTestData2(Path file) throws IOException {
        List<CustomerRecord> records = List.of(
                new CustomerRecord("00002", "Jones", "Emily", "CT-002", "Second customer"),
                new CustomerRecord("00004", "Miller", "David", "CT-004", "Fourth customer"),
                new CustomerRecord("00006", "Wilson", "Grace", "CT-006", "Sixth customer"),
                new CustomerRecord("00008", "Moore", "Frank", "CT-008", "Eighth customer"),
                new CustomerRecord("00010", "Taylor", "Helen", "CT-010", "Tenth customer")
        );
        writeRecords(file, records);
    }

    private static List<CustomerRecord> readRecords(Path file) throws IOException {
        List<CustomerRecord> records = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(file)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isBlank()) {
                    records.add(CustomerRecord.fromFixedWidth(line));
                }
            }
        }
        return records;
    }

    private static void writeRecords(Path file, List<CustomerRecord> records) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(file)) {
            for (CustomerRecord record : records) {
                writer.write(record.toFixedWidth());
                writer.newLine();
            }
        }
    }

    private static void displayFile(Path file) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(file)) {
            String line;
            while ((line = reader.readLine()) != null) {
                CustomerRecord record = CustomerRecord.fromFixedWidth(line);
                System.out.println("  " + record);
            }
        }
    }

    private static void displayRecords(List<CustomerRecord> records) {
        for (CustomerRecord record : records) {
            System.out.println("  " + record);
        }
    }
}
