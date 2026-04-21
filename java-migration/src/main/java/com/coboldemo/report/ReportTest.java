package com.coboldemo.report;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Port of {@code report_writer/report_test.cbl} — reads a list of students
 * from {@code input.txt} and produces a paginated, columnar report in
 * {@code report.txt}.
 *
 * <p>The COBOL program uses the Report Writer feature
 * ({@code RD ... PAGE LIMIT IS 66 ...}). We emulate the same page layout by
 * tracking a line counter and emitting a header every time it rolls over.
 */
public final class ReportTest {

    private static final Path INPUT_PATH = Path.of("input.txt");
    private static final Path OUTPUT_PATH = Path.of("report.txt");

    /** Fixed-width record: PIC 9(6) / PIC X(20) / PIC XXX / PIC 99. */
    public record StudentRecord(int studentId, String studentName,
                                String major, int numCourses) {

        public static StudentRecord parse(String line) {
            int id = Integer.parseInt(line.substring(0, 6));
            String name = line.substring(6, 26);
            String major = line.substring(26, 29);
            int courses = Integer.parseInt(line.substring(29, 31));
            return new StudentRecord(id, name, major, courses);
        }

        public String toFixedWidth() {
            return String.format("%06d%-20s%-3s%02d", studentId, studentName, major, numCourses);
        }
    }

    private ReportTest() {
    }

    public static void main(String[] args) throws IOException {
        System.out.println("Starting test report program.");

        if (!Files.exists(INPUT_PATH)) {
            createSampleInput();
        }

        List<StudentRecord> records = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(INPUT_PATH)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isEmpty()) {
                    records.add(StudentRecord.parse(line));
                }
            }
        }

        System.out.println("Init test report.");
        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(OUTPUT_PATH))) {
            int page = 1;
            int pageLimit = 66;
            int lineOnPage = 1;
            writeHeader(writer, page);
            // COBOL RD: first detail at line 6.
            lineOnPage = 6;
            for (StudentRecord record : records) {
                System.out.println("Generate report line.");
                if (lineOnPage > 42) { // last detail line per COBOL RD.
                    writer.println();
                    page++;
                    writeHeader(writer, page);
                    lineOnPage = 6;
                }
                writer.println(formatDetail(record));
                lineOnPage++;
                if (lineOnPage > pageLimit) {
                    writeHeader(writer, ++page);
                    lineOnPage = 6;
                }
            }
        }

        System.out.println("Terminate report.");
        System.out.println("Done.");
    }

    private static void createSampleInput() throws IOException {
        List<StudentRecord> sample = List.of(
                new StudentRecord(100001, "Alice Smith", "CSC", 5),
                new StudentRecord(100002, "Bob Jones", "MAT", 4),
                new StudentRecord(100003, "Carol Davis", "PHY", 6)
        );
        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(INPUT_PATH))) {
            for (StudentRecord record : sample) {
                writer.println(record.toFixedWidth());
            }
        }
    }

    private static void writeHeader(PrintWriter writer, int page) {
        writer.println(String.format("%44s%-21s", "", "Customer Order Report"));
        writer.println(String.format("%100s%-4s %3d", "", "PAGE", page));
        writer.println();
    }

    private static String formatDetail(StudentRecord record) {
        return String.format("   %06d %-20s %-3s %02d",
                record.studentId(), record.studentName(), record.major(), record.numCourses());
    }
}
