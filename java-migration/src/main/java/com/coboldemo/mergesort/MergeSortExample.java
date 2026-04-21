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
 * Port of {@code merge_sort/merge_sort_test.cbl} — creates two fixed-width
 * customer files, merges them into one file sorted by customer id, then
 * resorts that file by contract id descending.
 *
 * <p>The COBOL {@code MERGE} and {@code SORT} verbs read/write line
 * sequential files with a fixed record layout. We preserve the layout by
 * writing one record per line using {@link String#format(String, Object...)}
 * with field widths that match the original PIC clauses.
 */
public final class MergeSortExample {

    private static final Path FILE_1 = Path.of("test-file-1.txt");
    private static final Path FILE_2 = Path.of("test-file-2.txt");
    private static final Path MERGED = Path.of("merge-output.txt");
    private static final Path SORTED = Path.of("sorted-contract-id.txt");

    /** Fixed-width customer record mirroring the COBOL FD layout. */
    public record CustomerRecord(int id, String lastName, String firstName,
                                 int contractId, String comment) {

        public String toFixedWidth() {
            return String.format("%05d%-50s%-50s%05d%-25s",
                    id, lastName, firstName, contractId, comment);
        }

        public static CustomerRecord parse(String line) {
            int id = Integer.parseInt(line.substring(0, 5));
            String last = line.substring(5, 55);
            String first = line.substring(55, 105);
            int contract = Integer.parseInt(line.substring(105, 110));
            String comment = line.length() > 110 ? line.substring(110) : "";
            return new CustomerRecord(id, last, first, contract, comment);
        }
    }

    private MergeSortExample() {
    }

    public static void main(String[] args) throws IOException {
        createTestData();
        mergeAndDisplay();
        sortAndDisplay();
        System.out.println("Done.");
    }

    private static void createTestData() throws IOException {
        System.out.println("Creating test data files...");
        List<CustomerRecord> east = List.of(
                new CustomerRecord(1, "last-1", "first-1", 5423, "comment-1"),
                new CustomerRecord(5, "last-5", "first-5", 12323, "comment-5"),
                new CustomerRecord(10, "last-10", "first-10", 653, "comment-10"),
                new CustomerRecord(50, "last-50", "first-50", 5050, "comment-50"),
                new CustomerRecord(25, "last-25", "first-25", 7725, "comment-25"),
                new CustomerRecord(75, "last-75", "first-75", 1175, "comment-75")
        );
        List<CustomerRecord> west = List.of(
                new CustomerRecord(999, "last-999", "first-999", 1610, "comment-99"),
                new CustomerRecord(3, "last-03", "first-03", 3331, "comment-03"),
                new CustomerRecord(30, "last-30", "first-30", 8765, "comment-30"),
                new CustomerRecord(85, "last-85", "first-85", 4567, "comment-85"),
                new CustomerRecord(24, "last-24", "first-24", 247, "comment-24")
        );
        writeRecords(FILE_1, east);
        writeRecords(FILE_2, west);
    }

    private static void writeRecords(Path path, List<CustomerRecord> records) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            for (CustomerRecord record : records) {
                writer.write(record.toFixedWidth());
                writer.newLine();
            }
        }
    }

    private static List<CustomerRecord> readRecords(Path path) throws IOException {
        List<CustomerRecord> records = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isEmpty()) {
                    records.add(CustomerRecord.parse(line));
                }
            }
        }
        return records;
    }

    private static void mergeAndDisplay() throws IOException {
        System.out.println("Merging and sorting files...");
        List<CustomerRecord> merged = new ArrayList<>();
        merged.addAll(readRecords(FILE_1));
        merged.addAll(readRecords(FILE_2));
        merged.sort(Comparator.comparingInt(CustomerRecord::id));
        writeRecords(MERGED, merged);
        for (CustomerRecord record : merged) {
            System.out.println(record.toFixedWidth());
        }
    }

    private static void sortAndDisplay() throws IOException {
        System.out.println("Sorting merged file on descending contract id....");
        List<CustomerRecord> records = readRecords(MERGED);
        records.sort(Comparator.comparingInt(CustomerRecord::contractId).reversed());
        writeRecords(SORTED, records);
        for (CustomerRecord record : records) {
            System.out.println(record.toFixedWidth());
        }
    }
}
