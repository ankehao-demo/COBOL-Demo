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
 * Java equivalent of merge_sort/merge_sort_test.cbl
 *
 * Demonstrates COBOL SORT and MERGE operations on fixed-width record files.
 * In Java, we read records into a list, sort in memory, and write back out.
 *
 * COBOL Mapping:
 *   FD file definitions       → BufferedReader / BufferedWriter
 *   SD sort file              → not needed; sort in memory
 *   Fixed-length records      → parse with String.substring()
 *   SORT ... ON ASCENDING KEY → list.sort(Comparator.comparing(...))
 *   MERGE ... USING ... GIVING → read both files, combine, sort, write
 */
public class MergeSortExample {

    /** Customer record matching the COBOL FD layout (135 chars total). */
    static class CustomerRecord {
        int id;            // PIC 9(5)
        String lastName;   // PIC X(50)
        String firstName;  // PIC X(50)
        int contractId;    // PIC 9(5)
        String comment;    // PIC X(25)

        CustomerRecord(int id, String lastName, String firstName, int contractId, String comment) {
            this.id = id;
            this.lastName = lastName;
            this.firstName = firstName;
            this.contractId = contractId;
            this.comment = comment;
        }

        /** Format as fixed-width string matching COBOL record layout. */
        String toFixedWidth() {
            return String.format("%05d%-50s%-50s%05d%-25s", id, lastName, firstName, contractId, comment);
        }

        /** Parse a fixed-width string into a CustomerRecord. */
        static CustomerRecord fromFixedWidth(String line) {
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
        Path file1 = Path.of("test-file-1.txt");
        Path file2 = Path.of("test-file-2.txt");
        Path mergedFile = Path.of("merge-output.txt");
        Path sortedFile = Path.of("sorted-contract-id.txt");

        // Create test data files
        createTestData(file1, file2);

        // Merge and sort by ascending customer ID
        mergeAndDisplayFiles(file1, file2, mergedFile);

        // Sort merged file by descending contract ID
        sortAndDisplayFile(mergedFile, sortedFile);

        System.out.println("Done.");

        // Clean up temp files
        Files.deleteIfExists(file1);
        Files.deleteIfExists(file2);
        Files.deleteIfExists(mergedFile);
        Files.deleteIfExists(sortedFile);
    }

    private static void mergeAndDisplayFiles(Path file1, Path file2, Path mergedFile) throws IOException {
        System.out.println("Merging and sorting files...");

        // Read both files into one list
        List<CustomerRecord> allRecords = new ArrayList<>();
        allRecords.addAll(readFile(file1));
        allRecords.addAll(readFile(file2));

        // MERGE ... ON ASCENDING KEY f-customer-id → sort by id ascending
        allRecords.sort(Comparator.comparingInt(r -> r.id));

        // Write merged output
        writeFile(mergedFile, allRecords);

        // Display merged results
        for (CustomerRecord r : allRecords) {
            System.out.println(r);
        }
    }

    private static void sortAndDisplayFile(Path mergedFile, Path sortedFile) throws IOException {
        System.out.println("Sorting merged file on descending contract id....");

        List<CustomerRecord> records = readFile(mergedFile);

        // SORT ... ON DESCENDING KEY f-customer-contract-id
        records.sort(Comparator.comparingInt((CustomerRecord r) -> r.contractId).reversed());

        writeFile(sortedFile, records);

        for (CustomerRecord r : records) {
            System.out.println(r);
        }
    }

    private static void createTestData(Path file1, Path file2) throws IOException {
        System.out.println("Creating test data files...");

        // East region customers (file 1)
        List<CustomerRecord> east = List.of(
            new CustomerRecord(1, "last-1", "first-1", 5423, "comment-1"),
            new CustomerRecord(5, "last-5", "first-5", 12323, "comment-5"),
            new CustomerRecord(10, "last-10", "first-10", 653, "comment-10"),
            new CustomerRecord(50, "last-50", "first-50", 5050, "comment-50"),
            new CustomerRecord(25, "last-25", "first-25", 7725, "comment-25"),
            new CustomerRecord(75, "last-75", "first-75", 1175, "comment-75")
        );
        writeFile(file1, east);

        // West region customers (file 2)
        List<CustomerRecord> west = List.of(
            new CustomerRecord(999, "last-999", "first-999", 1610, "comment-99"),
            new CustomerRecord(3, "last-03", "first-03", 3331, "comment-03"),
            new CustomerRecord(30, "last-30", "first-30", 8765, "comment-30"),
            new CustomerRecord(85, "last-85", "first-85", 4567, "comment-85"),
            new CustomerRecord(24, "last-24", "first-24", 247, "comment-24")
        );
        writeFile(file2, west);
    }

    private static List<CustomerRecord> readFile(Path path) throws IOException {
        List<CustomerRecord> records = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isBlank()) {
                    records.add(CustomerRecord.fromFixedWidth(line));
                }
            }
        }
        return records;
    }

    private static void writeFile(Path path, List<CustomerRecord> records) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            for (CustomerRecord r : records) {
                writer.write(r.toFixedWidth());
                writer.newLine();
            }
        }
    }
}
