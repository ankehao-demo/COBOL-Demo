package com.coboldemo.mergesort;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Migrated from merge_sort/merge_sort_test.cbl
 * Demonstrates file-based merge and sort operations on customer records.
 */
public class MergeSortExample {

    // Field widths matching COBOL record layout
    private static final int ID_WIDTH = 5;
    private static final int LAST_NAME_WIDTH = 50;
    private static final int FIRST_NAME_WIDTH = 50;
    private static final int CONTRACT_ID_WIDTH = 5;
    private static final int COMMENT_WIDTH = 25;
    private static final int RECORD_WIDTH = ID_WIDTH + LAST_NAME_WIDTH + FIRST_NAME_WIDTH
            + CONTRACT_ID_WIDTH + COMMENT_WIDTH;

    static class CustomerRecord {
        int id;
        String lastName;
        String firstName;
        int contractId;
        String comment;

        CustomerRecord(int id, String lastName, String firstName, int contractId, String comment) {
            this.id = id;
            this.lastName = lastName;
            this.firstName = firstName;
            this.contractId = contractId;
            this.comment = comment;
        }

        String toFixedWidth() {
            return String.format("%05d", id)
                    + padRight(lastName, LAST_NAME_WIDTH)
                    + padRight(firstName, FIRST_NAME_WIDTH)
                    + String.format("%05d", contractId)
                    + padRight(comment, COMMENT_WIDTH);
        }

        static CustomerRecord fromFixedWidth(String line) {
            if (line.length() < RECORD_WIDTH) {
                line = padRight(line, RECORD_WIDTH);
            }
            int pos = 0;
            int id = Integer.parseInt(line.substring(pos, pos + ID_WIDTH).trim());
            pos += ID_WIDTH;
            String lastName = line.substring(pos, pos + LAST_NAME_WIDTH);
            pos += LAST_NAME_WIDTH;
            String firstName = line.substring(pos, pos + FIRST_NAME_WIDTH);
            pos += FIRST_NAME_WIDTH;
            int contractId = Integer.parseInt(line.substring(pos, pos + CONTRACT_ID_WIDTH).trim());
            pos += CONTRACT_ID_WIDTH;
            String comment = line.substring(pos, Math.min(pos + COMMENT_WIDTH, line.length()));
            return new CustomerRecord(id, lastName, firstName, contractId, comment);
        }
    }

    public static void main(String[] args) {
        createTestData();
        mergeAndDisplayFiles();
        sortAndDisplayFile();
        System.out.println("Done.");
    }

    private static void createTestData() {
        System.out.println("Creating test data files...");

        // File 1 - East customers
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("test-file-1.txt"))) {
            writeRecord(writer, new CustomerRecord(1, "last-1", "first-1", 5423, "comment-1"));
            writeRecord(writer, new CustomerRecord(5, "last-5", "first-5", 12323, "comment-5"));
            writeRecord(writer, new CustomerRecord(10, "last-10", "first-10", 653, "comment-10"));
            writeRecord(writer, new CustomerRecord(50, "last-50", "first-50", 5050, "comment-50"));
            writeRecord(writer, new CustomerRecord(25, "last-25", "first-25", 7725, "comment-25"));
            writeRecord(writer, new CustomerRecord(75, "last-75", "first-75", 1175, "comment-75"));
        } catch (IOException e) {
            System.err.println("Failed to create test-file-1.txt: " + e.getMessage());
            System.exit(1);
        }

        // File 2 - West customers
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("test-file-2.txt"))) {
            writeRecord(writer, new CustomerRecord(999, "last-999", "first-999", 1610, "comment-99"));
            writeRecord(writer, new CustomerRecord(3, "last-03", "first-03", 3331, "comment-03"));
            writeRecord(writer, new CustomerRecord(30, "last-30", "first-30", 8765, "comment-30"));
            writeRecord(writer, new CustomerRecord(85, "last-85", "first-85", 4567, "comment-85"));
            writeRecord(writer, new CustomerRecord(24, "last-24", "first-24", 247, "comment-24"));
        } catch (IOException e) {
            System.err.println("Failed to create test-file-2.txt: " + e.getMessage());
            System.exit(1);
        }
    }

    private static void mergeAndDisplayFiles() {
        System.out.println("Merging and sorting files...");

        List<CustomerRecord> allRecords = new ArrayList<>();
        allRecords.addAll(readFile("test-file-1.txt"));
        allRecords.addAll(readFile("test-file-2.txt"));

        // Merge-sort by customer ID ascending
        allRecords.sort(Comparator.comparingInt(r -> r.id));

        // Write merged output
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("merge-output.txt"))) {
            for (CustomerRecord record : allRecords) {
                writer.write(record.toFixedWidth());
                writer.newLine();
                System.out.println(record.toFixedWidth());
            }
        } catch (IOException e) {
            System.err.println("Error writing merge-output.txt: " + e.getMessage());
            System.exit(1);
        }
    }

    private static void sortAndDisplayFile() {
        System.out.println("Sorting merged file on descending contract id....");

        List<CustomerRecord> records = readFile("merge-output.txt");

        // Sort by contract ID descending
        records.sort(Comparator.comparingInt((CustomerRecord r) -> r.contractId).reversed());

        // Write sorted output
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("sorted-contract-id.txt"))) {
            for (CustomerRecord record : records) {
                writer.write(record.toFixedWidth());
                writer.newLine();
                System.out.println(record.toFixedWidth());
            }
        } catch (IOException e) {
            System.err.println("Error writing sorted-contract-id.txt: " + e.getMessage());
            System.exit(1);
        }
    }

    private static List<CustomerRecord> readFile(String filename) {
        List<CustomerRecord> records = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isEmpty()) {
                    records.add(CustomerRecord.fromFixedWidth(line));
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading " + filename + ": " + e.getMessage());
            System.exit(1);
        }
        return records;
    }

    private static void writeRecord(BufferedWriter writer, CustomerRecord record) throws IOException {
        writer.write(record.toFixedWidth());
        writer.newLine();
    }

    private static String padRight(String s, int width) {
        if (s == null) s = "";
        if (s.length() >= width) return s.substring(0, width);
        return String.format("%-" + width + "s", s);
    }
}
