package com.coboldemo.mergesort;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Java port of {@code merge_sort/merge_sort_test.cbl}.
 *
 * Recreates the COBOL example which:
 * <ul>
 *   <li>Writes two fixed-format text files of customer records.</li>
 *   <li>Merges them on ascending customer id and writes the result to
 *       a third file (analogue of {@code MERGE ... USING ... GIVING}).</li>
 *   <li>Sorts the merged file on descending contract id and writes that
 *       to a fourth file (analogue of {@code SORT ... USING ... GIVING}).</li>
 * </ul>
 *
 * The fixed COBOL record layout is preserved when reading/writing so the
 * output is byte-compatible with the COBOL program:
 * id(5) lastName(50) firstName(50) contractId(5) comment(25).
 */
public class MergeSortExample {

    private static final int WIDTH_ID = 5;
    private static final int WIDTH_NAME = 50;
    private static final int WIDTH_CONTRACT = 5;
    private static final int WIDTH_COMMENT = 25;
    private static final int RECORD_WIDTH =
            WIDTH_ID + WIDTH_NAME + WIDTH_NAME + WIDTH_CONTRACT + WIDTH_COMMENT;

    public static final class CustomerRecord {
        public final int id;
        public final String lastName;
        public final String firstName;
        public final int contractId;
        public final String comment;

        public CustomerRecord(int id, String lastName, String firstName, int contractId, String comment) {
            this.id = id;
            this.lastName = lastName;
            this.firstName = firstName;
            this.contractId = contractId;
            this.comment = comment;
        }

        public String toFixedRecord() {
            return String.format("%05d", id)
                    + padRight(lastName, WIDTH_NAME)
                    + padRight(firstName, WIDTH_NAME)
                    + String.format("%05d", contractId)
                    + padRight(comment, WIDTH_COMMENT);
        }

        public static CustomerRecord parse(String line) {
            String padded = line;
            if (padded.length() < RECORD_WIDTH) {
                padded = padRight(padded, RECORD_WIDTH);
            }
            int id = Integer.parseInt(padded.substring(0, WIDTH_ID));
            int p = WIDTH_ID;
            String last = padded.substring(p, p + WIDTH_NAME);
            p += WIDTH_NAME;
            String first = padded.substring(p, p + WIDTH_NAME);
            p += WIDTH_NAME;
            int contractId = Integer.parseInt(padded.substring(p, p + WIDTH_CONTRACT));
            p += WIDTH_CONTRACT;
            String comment = padded.substring(p, p + WIDTH_COMMENT);
            return new CustomerRecord(id, last, first, contractId, comment);
        }
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
        writeRecords(Paths.get("test-file-1.txt"), east);

        List<CustomerRecord> west = new ArrayList<>();
        west.add(new CustomerRecord(999, "last-999", "first-999", 1610, "comment-99"));
        west.add(new CustomerRecord(3, "last-03", "first-03", 3331, "comment-03"));
        west.add(new CustomerRecord(30, "last-30", "first-30", 8765, "comment-30"));
        west.add(new CustomerRecord(85, "last-85", "first-85", 4567, "comment-85"));
        west.add(new CustomerRecord(24, "last-24", "first-24", 247, "comment-24"));
        writeRecords(Paths.get("test-file-2.txt"), west);
    }

    private static void mergeAndDisplayFiles() throws IOException {
        System.out.println("Merging and sorting files...");

        List<CustomerRecord> all = new ArrayList<>();
        all.addAll(readRecords(Paths.get("test-file-1.txt")));
        all.addAll(readRecords(Paths.get("test-file-2.txt")));
        all.sort(Comparator.comparingInt(r -> r.id));

        writeRecords(Paths.get("merge-output.txt"), all);
        for (CustomerRecord r : all) {
            System.out.println(r.toFixedRecord());
        }
    }

    private static void sortAndDisplayFile() throws IOException {
        System.out.println("Sorting merged file on descending contract id....");

        List<CustomerRecord> all = readRecords(Paths.get("merge-output.txt"));
        all.sort(Comparator.comparingInt((CustomerRecord r) -> r.contractId).reversed());

        writeRecords(Paths.get("sorted-contract-id.txt"), all);
        for (CustomerRecord r : all) {
            System.out.println(r.toFixedRecord());
        }
    }

    private static void writeRecords(Path file, List<CustomerRecord> records) throws IOException {
        try (BufferedWriter w = Files.newBufferedWriter(file)) {
            for (CustomerRecord r : records) {
                w.write(r.toFixedRecord());
                w.newLine();
            }
        }
    }

    private static List<CustomerRecord> readRecords(Path file) throws IOException {
        List<CustomerRecord> out = new ArrayList<>();
        try (BufferedReader r = Files.newBufferedReader(file)) {
            String line;
            while ((line = r.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }
                out.add(CustomerRecord.parse(line));
            }
        }
        return out;
    }

    private static String padRight(String s, int width) {
        String safe = s == null ? "" : s;
        if (safe.length() >= width) {
            return safe.substring(0, width);
        }
        return String.format("%-" + width + "s", safe);
    }
}
