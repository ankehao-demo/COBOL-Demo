package com.coboldemo.fileio;

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
 *
 * Demonstrates COBOL SORT and MERGE operations on customer records
 * stored in files. In Java, uses List.sort() with Comparators and
 * a merge algorithm to combine two sorted lists.
 */
public class MergeSortExample {

    public static class CustomerRecord {
        int id;
        String lastName;
        String firstName;
        int contractId;
        String comment;

        public CustomerRecord(int id, String lastName, String firstName,
                              int contractId, String comment) {
            this.id = id;
            this.lastName = lastName;
            this.firstName = firstName;
            this.contractId = contractId;
            this.comment = comment;
        }

        public String toFixedWidth() {
            return String.format("%05d%-50s%-50s%05d%-25s",
                    id, lastName, firstName, contractId, comment);
        }

        public static CustomerRecord fromFixedWidth(String line) {
            if (line.length() < 135) {
                line = String.format("%-135s", line);
            }
            int id = Integer.parseInt(line.substring(0, 5).trim());
            String lastName = line.substring(5, 55);
            String firstName = line.substring(55, 105);
            int contractId = Integer.parseInt(line.substring(105, 110).trim());
            String comment = line.substring(110, 135);
            return new CustomerRecord(id, lastName, firstName, contractId, comment);
        }

        @Override
        public String toString() {
            return toFixedWidth();
        }
    }

    public static void main(String[] args) throws IOException {
        Path testFile1 = Path.of("test-file-1.txt");
        Path testFile2 = Path.of("test-file-2.txt");
        Path mergedFile = Path.of("merge-output.txt");
        Path sortedFile = Path.of("sorted-contract-id.txt");

        try {
            createTestData(testFile1, testFile2);
            mergeAndDisplayFiles(testFile1, testFile2, mergedFile);
            sortAndDisplayFile(mergedFile, sortedFile);
            System.out.println("Done.");
        } finally {
            // Clean up temp files
            Files.deleteIfExists(testFile1);
            Files.deleteIfExists(testFile2);
            Files.deleteIfExists(mergedFile);
            Files.deleteIfExists(sortedFile);
        }
    }

    static void mergeAndDisplayFiles(Path file1, Path file2, Path outputFile) throws IOException {
        System.out.println("Merging and sorting files...");

        List<CustomerRecord> list1 = readRecords(file1);
        List<CustomerRecord> list2 = readRecords(file2);

        // Merge both lists and sort by customer ID ascending
        List<CustomerRecord> merged = new ArrayList<>();
        merged.addAll(list1);
        merged.addAll(list2);
        merged.sort(Comparator.comparingInt(r -> r.id));

        writeRecords(outputFile, merged);

        for (CustomerRecord record : merged) {
            System.out.println(record.toFixedWidth());
        }
    }

    static void sortAndDisplayFile(Path inputFile, Path outputFile) throws IOException {
        System.out.println("Sorting merged file on descending contract id....");

        List<CustomerRecord> records = readRecords(inputFile);
        records.sort(Comparator.comparingInt((CustomerRecord r) -> r.contractId).reversed());

        writeRecords(outputFile, records);

        for (CustomerRecord record : records) {
            System.out.println(record.toFixedWidth());
        }
    }

    static void createTestData(Path file1, Path file2) throws IOException {
        System.out.println("Creating test data files...");

        // East customers (file 1)
        List<CustomerRecord> east = List.of(
                new CustomerRecord(1, "last-1", "first-1", 5423, "comment-1"),
                new CustomerRecord(5, "last-5", "first-5", 12323, "comment-5"),
                new CustomerRecord(10, "last-10", "first-10", 653, "comment-10"),
                new CustomerRecord(50, "last-50", "first-50", 5050, "comment-50"),
                new CustomerRecord(25, "last-25", "first-25", 7725, "comment-25"),
                new CustomerRecord(75, "last-75", "first-75", 1175, "comment-75")
        );
        writeRecords(file1, east);

        // West customers (file 2)
        List<CustomerRecord> west = List.of(
                new CustomerRecord(999, "last-999", "first-999", 1610, "comment-99"),
                new CustomerRecord(3, "last-03", "first-03", 3331, "comment-03"),
                new CustomerRecord(30, "last-30", "first-30", 8765, "comment-30"),
                new CustomerRecord(85, "last-85", "first-85", 4567, "comment-85"),
                new CustomerRecord(24, "last-24", "first-24", 247, "comment-24")
        );
        writeRecords(file2, west);
    }

    static List<CustomerRecord> readRecords(Path file) throws IOException {
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

    static void writeRecords(Path file, List<CustomerRecord> records) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(file)) {
            for (CustomerRecord record : records) {
                writer.write(record.toFixedWidth());
                writer.newLine();
            }
        }
    }
}
