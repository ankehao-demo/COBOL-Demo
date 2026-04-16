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
 * Java migration of merge_sort/merge_sort_test.cbl
 *
 * Original COBOL program (by Erik Eriksen, 2021-09-19):
 *   - Creates two temporary files with customer records in fixed-width format
 *   - Uses MERGE to merge both files sorted by customer ID (ascending)
 *   - Uses SORT to sort the merged output by contract ID (descending)
 *   - Writes output files and displays results
 *
 * COBOL-to-Java mapping:
 *   FD (File Description)      -> BufferedReader / BufferedWriter
 *   SD (Sort Description)      -> in-memory List + Comparator
 *   MERGE ... ASCENDING KEY    -> merge two lists, sort by customer ID ascending
 *   SORT ... DESCENDING KEY    -> sort merged list by contract ID descending
 *   WRITE record               -> BufferedWriter.write() with fixed-width format
 *   READ ... AT END            -> BufferedReader.readLine() == null
 *   DISPLAY                    -> System.out.println()
 *   PIC 9(4)                   -> int, formatted to 4 digits
 *   PIC X(20)                  -> String, padded/truncated to 20 chars
 *   PIC 9(6)                   -> int, formatted to 6 digits
 *   PIC X(30)                  -> String, padded/truncated to 30 chars
 */
public class MergeSortExample {

    // Fixed-width field sizes matching the COBOL record layout specified in the task
    private static final int CUSTOMER_ID_WIDTH = 4;
    private static final int LAST_NAME_WIDTH = 20;
    private static final int FIRST_NAME_WIDTH = 20;
    private static final int CONTRACT_ID_WIDTH = 6;
    private static final int COMMENT_WIDTH = 30;

    /**
     * CustomerRecord class representing a COBOL record (01-level group item).
     * Maps to the COBOL file section record definition:
     *   05 f-customer-id          PIC 9(4)
     *   05 f-customer-last-name   PIC X(20)
     *   05 f-customer-first-name  PIC X(20)
     *   05 f-customer-contract-id PIC 9(6)
     *   05 f-customer-comment     PIC X(30)
     */
    static class CustomerRecord {
        private int id;
        private String lastName;
        private String firstName;
        private int contractId;
        private String comment;

        public CustomerRecord(int id, String lastName, String firstName, int contractId, String comment) {
            this.id = id;
            this.lastName = lastName;
            this.firstName = firstName;
            this.contractId = contractId;
            this.comment = comment;
        }

        public int getId() {
            return id;
        }

        public String getLastName() {
            return lastName;
        }

        public String getFirstName() {
            return firstName;
        }

        public int getContractId() {
            return contractId;
        }

        public String getComment() {
            return comment;
        }

        /**
         * Converts the record to fixed-width format for file output.
         * Mirrors the COBOL WRITE statement which outputs the entire record
         * as a single fixed-width line.
         */
        public String toFixedWidth() {
            return padRight(String.format("%0" + CUSTOMER_ID_WIDTH + "d", id), CUSTOMER_ID_WIDTH)
                    + padRight(lastName, LAST_NAME_WIDTH)
                    + padRight(firstName, FIRST_NAME_WIDTH)
                    + padRight(String.format("%0" + CONTRACT_ID_WIDTH + "d", contractId), CONTRACT_ID_WIDTH)
                    + padRight(comment, COMMENT_WIDTH);
        }

        @Override
        public String toString() {
            return toFixedWidth();
        }
    }

    /**
     * Pads or truncates a string to the specified width.
     * Equivalent to COBOL's automatic padding with spaces for PIC X fields.
     */
    private static String padRight(String s, int width) {
        if (s == null) {
            s = "";
        }
        if (s.length() > width) {
            return s.substring(0, width);
        }
        return String.format("%-" + width + "s", s);
    }

    /**
     * Parses a fixed-width line into a CustomerRecord.
     * Equivalent to COBOL's implicit record parsing when reading from an FD file.
     */
    private static CustomerRecord parseRecord(String line) {
        int pos = 0;
        int id = Integer.parseInt(line.substring(pos, pos + CUSTOMER_ID_WIDTH).trim());
        pos += CUSTOMER_ID_WIDTH;
        String lastName = line.substring(pos, pos + LAST_NAME_WIDTH).trim();
        pos += LAST_NAME_WIDTH;
        String firstName = line.substring(pos, pos + FIRST_NAME_WIDTH).trim();
        pos += FIRST_NAME_WIDTH;
        int contractId = Integer.parseInt(line.substring(pos, pos + CONTRACT_ID_WIDTH).trim());
        pos += CONTRACT_ID_WIDTH;
        String comment = line.substring(pos, Math.min(pos + COMMENT_WIDTH, line.length())).trim();
        return new CustomerRecord(id, lastName, firstName, contractId, comment);
    }

    /**
     * Writes a list of CustomerRecords to a file in fixed-width format.
     * Equivalent to COBOL: OPEN OUTPUT fd-file / WRITE record / CLOSE fd-file
     */
    private static void writeRecordsToFile(Path filePath, List<CustomerRecord> records) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
            for (int i = 0; i < records.size(); i++) {
                writer.write(records.get(i).toFixedWidth());
                if (i < records.size() - 1) {
                    writer.newLine();
                }
            }
        }
    }

    /**
     * Reads all CustomerRecords from a fixed-width file.
     * Equivalent to COBOL: OPEN INPUT fd-file / READ ... AT END / CLOSE fd-file
     */
    private static List<CustomerRecord> readRecordsFromFile(Path filePath) throws IOException {
        List<CustomerRecord> records = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isBlank()) {
                    records.add(parseRecord(line));
                }
            }
        }
        return records;
    }

    /**
     * Displays records to standard output.
     * Equivalent to COBOL: DISPLAY f-customer-record
     */
    private static void displayRecords(List<CustomerRecord> records) {
        for (CustomerRecord record : records) {
            System.out.println(record.toFixedWidth());
        }
    }

    /**
     * Creates test data files with customer records.
     * Equivalent to COBOL paragraph: create-test-data
     * Maps COBOL MOVE + WRITE statements to Java list creation + file writing.
     */
    private static void createTestData(Path file1Path, Path file2Path) throws IOException {
        System.out.println("Creating test data files...");

        // File 1 records (fd-test-file-1 / "east" customers)
        List<CustomerRecord> file1Records = List.of(
                new CustomerRecord(1234, "Last-1234", "First-1234", 111111, "Comment-1234-1"),
                new CustomerRecord(9000, "Last-9000", "First-9000", 222222, "Comment-9000-1"),
                new CustomerRecord(100, "Last-0100", "First-0100", 333333, "Comment-0100-1")
        );
        writeRecordsToFile(file1Path, file1Records);

        // File 2 records (fd-test-file-2 / "west" customers)
        List<CustomerRecord> file2Records = List.of(
                new CustomerRecord(5555, "Last-5555", "First-5555", 444444, "Comment-5555-2"),
                new CustomerRecord(1111, "Last-1111", "First-1111", 555555, "Comment-1111-2"),
                new CustomerRecord(1, "Last-0001", "First-0001", 666666, "Comment-0001-2"),
                new CustomerRecord(9999, "Last-9999", "First-9999", 777777, "Comment-9999-2")
        );
        writeRecordsToFile(file2Path, file2Records);
    }

    /**
     * Merges two files and sorts by customer ID ascending.
     * Equivalent to COBOL:
     *   MERGE fd-sorting-file
     *       ON ASCENDING KEY f-customer-id
     *       USING fd-test-file-1 fd-test-file-2
     *       GIVING fd-merged-file
     *
     * In Java, we read both files into lists, combine them,
     * and sort using a Comparator on customer ID.
     */
    private static List<CustomerRecord> mergeFiles(Path file1Path, Path file2Path, Path mergeOutputPath)
            throws IOException {
        System.out.println("Merging and sorting files...");

        // Read both input files (equivalent to USING fd-test-file-1 fd-test-file-2)
        List<CustomerRecord> file1Records = readRecordsFromFile(file1Path);
        List<CustomerRecord> file2Records = readRecordsFromFile(file2Path);

        // Display input file contents
        System.out.println("--- Input File 1 ---");
        displayRecords(file1Records);
        System.out.println("--- Input File 2 ---");
        displayRecords(file2Records);

        // Merge: combine both lists and sort by customer ID ascending
        // (equivalent to MERGE ... ON ASCENDING KEY f-customer-id)
        List<CustomerRecord> merged = new ArrayList<>();
        merged.addAll(file1Records);
        merged.addAll(file2Records);
        merged.sort(Comparator.comparingInt(CustomerRecord::getId));

        // Write merged output (equivalent to GIVING fd-merged-file)
        writeRecordsToFile(mergeOutputPath, merged);

        // Display merged output (equivalent to OPEN INPUT + READ loop + DISPLAY)
        System.out.println("--- Merged Output (sorted by Customer ID ascending) ---");
        displayRecords(merged);

        return merged;
    }

    /**
     * Sorts the merged records by contract ID descending.
     * Equivalent to COBOL:
     *   SORT fd-sorting-file
     *       ON DESCENDING KEY f-customer-contract-id
     *       USING fd-merged-file
     *       GIVING fd-sorted-contract-id
     */
    private static void sortByContractId(List<CustomerRecord> merged, Path sortedOutputPath) throws IOException {
        System.out.println("Sorting merged file on descending contract id....");

        // Sort by contract ID descending
        // (equivalent to SORT ... ON DESCENDING KEY f-customer-contract-id)
        List<CustomerRecord> sorted = new ArrayList<>(merged);
        sorted.sort(Comparator.comparingInt(CustomerRecord::getContractId).reversed());

        // Write sorted output (equivalent to GIVING fd-sorted-contract-id)
        writeRecordsToFile(sortedOutputPath, sorted);

        // Display sorted output (equivalent to OPEN INPUT + READ loop + DISPLAY)
        System.out.println("--- Sorted Output (sorted by Contract ID descending) ---");
        displayRecords(sorted);
    }

    /**
     * Main entry point. Mirrors the COBOL PROCEDURE DIVISION main-procedure paragraph:
     *   PERFORM create-test-data
     *   PERFORM merge-and-display-files
     *   PERFORM sort-and-display-file
     *   DISPLAY "Done."
     *   STOP RUN
     */
    public static void main(String[] args) throws IOException {
        // Create temp directory for working files
        Path tempDir = Files.createTempDirectory("mergesort");
        Path file1 = tempDir.resolve("test-file-1.txt");
        Path file2 = tempDir.resolve("test-file-2.txt");
        Path mergeOutput = tempDir.resolve("merge-output.txt");
        Path sortedOutput = tempDir.resolve("sorted-contract-id.txt");

        try {
            // PERFORM create-test-data
            createTestData(file1, file2);

            // PERFORM merge-and-display-files
            List<CustomerRecord> merged = mergeFiles(file1, file2, mergeOutput);

            // PERFORM sort-and-display-file
            sortByContractId(merged, sortedOutput);

            System.out.println("Done.");
        } finally {
            // Clean up temporary files
            Files.deleteIfExists(file1);
            Files.deleteIfExists(file2);
            Files.deleteIfExists(mergeOutput);
            Files.deleteIfExists(sortedOutput);
            Files.deleteIfExists(tempDir);
        }
    }
}
