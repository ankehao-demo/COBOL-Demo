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
 * Java port of merge_sort/merge_sort_test.cbl.
 *
 * Emulates COBOL SORT/MERGE over line-sequential files with a fixed-width
 * customer record layout: id (5) + last (50) + first (50) + contract (5) +
 * comment (25).
 */
public final class MergeSortExample {

    private static final Path FILE_1 = Path.of("test-file-1.txt");
    private static final Path FILE_2 = Path.of("test-file-2.txt");
    private static final Path MERGE_OUT = Path.of("merge-output.txt");
    private static final Path SORTED_CONTRACT = Path.of(
            "sorted-contract-id.txt");

    private MergeSortExample() {
    }

    public static void main(String[] args) throws IOException {
        createTestData();
        mergeAndDisplayFiles();
        sortAndDisplayFile();
        System.out.println("Done.");
    }

    private static void createTestData() throws IOException {
        System.out.println("Creating test data files...");

        List<CustomerRecord> east = new ArrayList<>();
        east.add(new CustomerRecord(1, "last-1", "first-1", 5423, "comment-1"));
        east.add(new CustomerRecord(5, "last-5", "first-5", 12323, "comment-5"));
        east.add(new CustomerRecord(10, "last-10", "first-10", 653, "comment-10"));
        east.add(new CustomerRecord(50, "last-50", "first-50", 5050, "comment-50"));
        east.add(new CustomerRecord(25, "last-25", "first-25", 7725, "comment-25"));
        east.add(new CustomerRecord(75, "last-75", "first-75", 1175, "comment-75"));
        writeRecords(FILE_1, east);

        List<CustomerRecord> west = new ArrayList<>();
        west.add(new CustomerRecord(999, "last-999", "first-999", 1610, "comment-99"));
        west.add(new CustomerRecord(3, "last-03", "first-03", 3331, "comment-03"));
        west.add(new CustomerRecord(30, "last-30", "first-30", 8765, "comment-30"));
        west.add(new CustomerRecord(85, "last-85", "first-85", 4567, "comment-85"));
        west.add(new CustomerRecord(24, "last-24", "first-24", 247, "comment-24"));
        writeRecords(FILE_2, west);
    }

    private static void mergeAndDisplayFiles() throws IOException {
        System.out.println("Merging and sorting files...");

        List<CustomerRecord> merged = new ArrayList<>();
        merged.addAll(readRecords(FILE_1));
        merged.addAll(readRecords(FILE_2));
        merged.sort(Comparator.comparingInt(CustomerRecord::getId));

        writeRecords(MERGE_OUT, merged);
        merged.forEach(System.out::println);
    }

    private static void sortAndDisplayFile() throws IOException {
        System.out.println("Sorting merged file on descending contract id....");
        List<CustomerRecord> merged = readRecords(MERGE_OUT);
        merged.sort(Comparator.comparingInt(CustomerRecord::getContractId)
                .reversed());
        writeRecords(SORTED_CONTRACT, merged);
        merged.forEach(System.out::println);
    }

    private static void writeRecords(Path path, List<CustomerRecord> records)
            throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            for (CustomerRecord record : records) {
                writer.write(record.toFixedWidthLine());
                writer.newLine();
            }
        }
    }

    private static List<CustomerRecord> readRecords(Path path)
            throws IOException {
        List<CustomerRecord> out = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isEmpty()) {
                    continue;
                }
                out.add(CustomerRecord.fromFixedWidthLine(line));
            }
        }
        return out;
    }

    /** Mirrors the COBOL f-customer-record-* group fields. */
    public static final class CustomerRecord {

        private final int id;
        private final String lastName;
        private final String firstName;
        private final int contractId;
        private final String comment;

        public CustomerRecord(int id, String lastName, String firstName,
                              int contractId, String comment) {
            this.id = id;
            this.lastName = lastName;
            this.firstName = firstName;
            this.contractId = contractId;
            this.comment = comment;
        }

        public int getId() {
            return id;
        }

        public int getContractId() {
            return contractId;
        }

        String toFixedWidthLine() {
            return String.format("%05d", id)
                    + padRight(lastName, 50)
                    + padRight(firstName, 50)
                    + String.format("%05d", contractId)
                    + padRight(comment, 25);
        }

        static CustomerRecord fromFixedWidthLine(String line) {
            String padded = padRight(line, 135);
            int id = Integer.parseInt(padded.substring(0, 5).trim());
            String last = padded.substring(5, 55).trim();
            String first = padded.substring(55, 105).trim();
            int contract = Integer.parseInt(padded.substring(105, 110).trim());
            String comment = padded.substring(110, 135).trim();
            return new CustomerRecord(id, last, first, contract, comment);
        }

        private static String padRight(String value, int width) {
            String v = value == null ? "" : value;
            if (v.length() >= width) {
                return v.substring(0, width);
            }
            return v + " ".repeat(width - v.length());
        }

        @Override
        public String toString() {
            return toFixedWidthLine();
        }
    }
}
