package com.example.cobol.mergesort;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Java port of {@code merge_sort/merge_sort_test.cbl}.
 *
 * <p>Reproduces the original program's flow:
 * <ol>
 *   <li>Create two fixed-width text files of customer records (east + west).</li>
 *   <li>Merge them by ascending {@code customer-id} into one combined file.</li>
 *   <li>Sort the merged file by descending {@code contract-id} into another file.</li>
 *   <li>Print each step's output to stdout.</li>
 * </ol>
 *
 * <p>Customer record layout (matches the COBOL FD precisely):
 * <pre>
 *   id            5  digits
 *   last name    50  chars
 *   first name   50  chars
 *   contract id   5  digits
 *   comment      25  chars
 * </pre>
 */
public final class MergeSortExample {

    private static final int RECORD_LENGTH = 5 + 50 + 50 + 5 + 25;

    /** Customer record. */
    public static final class Customer {
        public final int id;
        public final String lastName;
        public final String firstName;
        public final int contractId;
        public final String comment;

        public Customer(int id, String lastName, String firstName,
                        int contractId, String comment) {
            this.id = id;
            this.lastName = lastName;
            this.firstName = firstName;
            this.contractId = contractId;
            this.comment = comment;
        }

        public String toFixedWidth() {
            return String.format("%05d", id)
                    + pad(lastName, 50)
                    + pad(firstName, 50)
                    + String.format("%05d", contractId)
                    + pad(comment, 25);
        }

        public static Customer parse(String line) {
            int idLen = 5;
            int nameLen = 50;
            int contractLen = 5;
            int commentLen = 25;

            String padded = line + " ".repeat(Math.max(0, RECORD_LENGTH - line.length()));
            int p = 0;
            int id = Integer.parseInt(padded.substring(p, p + idLen).trim());
            p += idLen;
            String last = padded.substring(p, p + nameLen);
            p += nameLen;
            String first = padded.substring(p, p + nameLen);
            p += nameLen;
            int contract = Integer.parseInt(padded.substring(p, p + contractLen).trim());
            p += contractLen;
            String comment = padded.substring(p, p + commentLen);
            return new Customer(id, last, first, contract, comment);
        }
    }

    private MergeSortExample() {}

    public static void main(String[] args) {
        Path tmpDir;
        try {
            tmpDir = Files.createTempDirectory("merge-sort-demo-");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        Path file1 = tmpDir.resolve("test-file-1.txt");
        Path file2 = tmpDir.resolve("test-file-2.txt");
        Path merged = tmpDir.resolve("merge-output.txt");
        Path sorted = tmpDir.resolve("sorted-contract-id.txt");

        createTestData(file1, file2);

        // Merge — read both files, sort by ascending id, write to merged file.
        System.out.println("Merging and sorting files...");
        List<Customer> all = new ArrayList<>();
        all.addAll(readAll(file1));
        all.addAll(readAll(file2));
        all.sort(Comparator.comparingInt((Customer c) -> c.id));
        writeAll(merged, all);
        for (Customer c : all) {
            System.out.println(c.toFixedWidth());
        }

        // Sort — read merged file, sort by descending contract id.
        System.out.println("Sorting merged file on descending contract id....");
        List<Customer> bySorted = readAll(merged);
        bySorted.sort(Comparator.comparingInt((Customer c) -> c.contractId).reversed());
        writeAll(sorted, bySorted);
        for (Customer c : bySorted) {
            System.out.println(c.toFixedWidth());
        }

        System.out.println("Done.");
    }

    private static void createTestData(Path file1, Path file2) {
        System.out.println("Creating test data files...");

        List<Customer> east = List.of(
            new Customer(1, "last-1", "first-1", 5423, "comment-1"),
            new Customer(5, "last-5", "first-5", 12323, "comment-5"),
            new Customer(10, "last-10", "first-10", 653, "comment-10"),
            new Customer(50, "last-50", "first-50", 5050, "comment-50"),
            new Customer(25, "last-25", "first-25", 7725, "comment-25"),
            new Customer(75, "last-75", "first-75", 1175, "comment-75")
        );

        List<Customer> west = List.of(
            new Customer(999, "last-999", "first-999", 1610, "comment-99"),
            new Customer(3, "last-03", "first-03", 3331, "comment-03"),
            new Customer(30, "last-30", "first-30", 8765, "comment-30"),
            new Customer(85, "last-85", "first-85", 4567, "comment-85"),
            new Customer(24, "last-24", "first-24", 247, "comment-24")
        );

        writeAll(file1, east);
        writeAll(file2, west);
    }

    private static List<Customer> readAll(Path path) {
        List<Customer> out = new ArrayList<>();
        try (BufferedReader r = Files.newBufferedReader(path)) {
            String line;
            while ((line = r.readLine()) != null) {
                if (!line.isEmpty()) {
                    out.add(Customer.parse(line));
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return out;
    }

    private static void writeAll(Path path, List<Customer> records) {
        try (BufferedWriter w = Files.newBufferedWriter(path)) {
            for (Customer c : records) {
                w.write(c.toFixedWidth());
                w.newLine();
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static String pad(String s, int width) {
        if (s.length() >= width) {
            return s.substring(0, width);
        }
        return String.format("%-" + width + "s", s);
    }
}
