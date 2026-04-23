package com.coboldemo.reportwriter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Java port of report_writer/report_test.cbl.
 *
 * Reads fixed-width records from {@code input.txt} and writes a paginated
 * report to {@code report.txt}, mirroring the RD settings from the original
 * (PAGE LIMIT 66, FIRST DETAIL 6, LAST DETAIL 42, FOOTING 52).
 */
public final class ReportTest {

    private static final Path INPUT = Path.of("input.txt");
    private static final Path OUTPUT = Path.of("report.txt");

    private static final int PAGE_LIMIT = 66;
    private static final int FIRST_DETAIL_LINE = 6;
    private static final int LAST_DETAIL_LINE = 42;
    private static final int FOOTING_LINE = 52;

    private ReportTest() {
    }

    public static void main(String[] args) throws IOException {
        System.out.println("Starting test report program.");

        if (!Files.exists(INPUT)) {
            // Seed a demo input file so the example runs out of the box.
            writeSampleInput();
        }

        List<StudentRecord> records = readRecords(INPUT);

        try (PrintWriter writer = new PrintWriter(
                Files.newBufferedWriter(OUTPUT))) {
            System.out.println("Init test report.");
            writeReport(writer, records);
            System.out.println("Terminate report.");
        }

        System.out.println("Done.");
    }

    private static void writeSampleInput() throws IOException {
        List<StudentRecord> seed = new ArrayList<>();
        seed.add(new StudentRecord(100001, "Alice Anderson", "CSC", 5));
        seed.add(new StudentRecord(100002, "Bob Brown", "PHY", 4));
        seed.add(new StudentRecord(100003, "Carol Chen", "MAT", 6));
        try (PrintWriter writer = new PrintWriter(
                Files.newBufferedWriter(INPUT))) {
            for (StudentRecord r : seed) {
                writer.println(r.toFixedWidthLine());
            }
        }
    }

    private static List<StudentRecord> readRecords(Path path)
            throws IOException {
        List<StudentRecord> out = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }
                out.add(StudentRecord.fromFixedWidthLine(line));
            }
        }
        return out;
    }

    private static void writeReport(PrintWriter writer,
                                    List<StudentRecord> records) {
        int page = 0;
        int currentDetailLine = 0;
        boolean pageOpen = false;

        for (StudentRecord record : records) {
            System.out.println("Generate report line.");
            if (!pageOpen || currentDetailLine >= LAST_DETAIL_LINE) {
                if (pageOpen) {
                    writeFooter(writer, page);
                }
                page++;
                writeHeader(writer, page);
                currentDetailLine = FIRST_DETAIL_LINE;
                pageOpen = true;
            }

            writer.printf(" %-3d %6d  %-20s  %-3s  %02d%n",
                    currentDetailLine,
                    record.id,
                    record.name,
                    record.major,
                    record.numCourses);
            currentDetailLine++;
        }

        if (pageOpen) {
            writeFooter(writer, page);
        }
    }

    private static void writeHeader(PrintWriter writer, int page) {
        writer.println(center("Customer Order Report", 60));
        writer.printf("%" + 60 + "s%n", "PAGE " + String.format("%3d", page));
        writer.println();
        writer.println("LINE   ID       NAME                  MAJOR  COURSES");
        writer.println("-----  -------  --------------------  -----  -------");
    }

    private static void writeFooter(PrintWriter writer, int page) {
        writer.println();
        writer.println("-- End of page " + page + " (page limit "
                + PAGE_LIMIT + ", footing at " + FOOTING_LINE + ") --");
        writer.println();
    }

    private static String center(String text, int width) {
        if (text.length() >= width) {
            return text;
        }
        int pad = (width - text.length()) / 2;
        return " ".repeat(pad) + text;
    }

    /** Fixed-width student record (6+20+3+2 = 31 chars). */
    public static final class StudentRecord {
        final int id;
        final String name;
        final String major;
        final int numCourses;

        public StudentRecord(int id, String name, String major,
                             int numCourses) {
            this.id = id;
            this.name = name;
            this.major = major;
            this.numCourses = numCourses;
        }

        String toFixedWidthLine() {
            return String.format("%06d", id)
                    + padRight(name, 20)
                    + padRight(major, 3)
                    + String.format("%02d", numCourses);
        }

        static StudentRecord fromFixedWidthLine(String line) {
            String padded = padRight(line, 31);
            int id = Integer.parseInt(padded.substring(0, 6).trim());
            String name = padded.substring(6, 26).trim();
            String major = padded.substring(26, 29).trim();
            int numCourses = Integer.parseInt(padded.substring(29, 31).trim());
            return new StudentRecord(id, name, major, numCourses);
        }

        private static String padRight(String value, int width) {
            String v = value == null ? "" : value;
            if (v.length() >= width) {
                return v.substring(0, width);
            }
            return v + " ".repeat(width - v.length());
        }
    }
}
