package com.example.cobol.reportwriter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Java port of {@code report_writer/report_test.cbl}.
 *
 * <p>The COBOL program reads a fixed-width input file describing student
 * records and produces a paginated report with a heading, a detail line per
 * student, and a page counter. Java has no built-in COBOL Report Writer, so
 * we implement the pagination logic manually with {@link PrintWriter}.
 *
 * <p>The input file is shipped as a resource at
 * {@code reportwriter/input.txt} and is also written out to
 * {@code input.txt} alongside the generated {@code report.txt} when the
 * demo is run, mirroring the original "input.txt → report.txt" workflow.
 */
public final class ReportTest {

    private static final int PAGE_LIMIT = 66;
    private static final int FIRST_DETAIL = 6;
    private static final int LAST_DETAIL = 42;

    /** Fixed-width record matching the COBOL FD layout. */
    public static final class Student {
        public final String studentId; //  6 digits
        public final String name;      // 20 chars
        public final String major;     //  3 chars
        public final String numCourses;//  2 digits

        public Student(String id, String name, String major, String numCourses) {
            this.studentId = id;
            this.name = name;
            this.major = major;
            this.numCourses = numCourses;
        }
    }

    private ReportTest() {}

    public static void main(String[] args) throws IOException {
        System.out.println("Starting test report program.");

        List<Student> students = readInput();
        Path output = Paths.get("report.txt");
        try (BufferedWriter writer = Files.newBufferedWriter(output);
             PrintWriter pw = new PrintWriter(writer)) {
            writeReport(pw, students);
        }

        System.out.println("Done. Wrote " + output.toAbsolutePath());
    }

    /**
     * Reads the bundled student input file. We try the working directory
     * first (so users can drop a custom {@code input.txt} next to the jar)
     * and fall back to the classpath resource.
     */
    static List<Student> readInput() throws IOException {
        Path local = Paths.get("input.txt");
        if (Files.exists(local)) {
            try (BufferedReader r = Files.newBufferedReader(local)) {
                return parse(r);
            }
        }
        try (InputStream in = ReportTest.class.getResourceAsStream("/reportwriter/input.txt")) {
            if (in == null) {
                throw new IOException("input.txt not found on disk or on the classpath");
            }
            try (BufferedReader r = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
                return parse(r);
            }
        }
    }

    static List<Student> parse(BufferedReader r) throws IOException {
        List<Student> out = new ArrayList<>();
        String line;
        while ((line = r.readLine()) != null) {
            if (line.isEmpty()) {
                continue;
            }
            String padded = line + " ".repeat(Math.max(0, 31 - line.length()));
            out.add(new Student(
                padded.substring(0, 6),
                padded.substring(6, 26),
                padded.substring(26, 29),
                padded.substring(29, 31)
            ));
        }
        return out;
    }

    static void writeReport(PrintWriter pw, List<Student> students) {
        int page = 0;
        int currentLine = 0;
        for (Student s : students) {
            if (currentLine == 0 || currentLine >= LAST_DETAIL) {
                if (page > 0) {
                    // Form-feed between pages.
                    pw.write('\f');
                }
                page++;
                writeHeader(pw, page);
                currentLine = FIRST_DETAIL;
            }
            writeDetail(pw, s);
            currentLine++;
            System.out.println("Generate report line.");
        }
    }

    private static void writeHeader(PrintWriter pw, int page) {
        // Line 1: "Customer Order Report" centered roughly at column 44.
        pw.printf("%44s%s%n", "", "Customer Order Report");
        // Line 2: "PAGE %3d" near column 100. Pad to 100 cols then write.
        String pageStr = String.format("%3d", page);
        pw.printf("%99s%s%s%n", "", "PAGE", pageStr);
        // Blank lines down to FIRST_DETAIL=6.
        for (int i = 3; i < FIRST_DETAIL; i++) {
            pw.println();
        }
    }

    private static void writeDetail(PrintWriter pw, Student s) {
        // Columns: 4=id, 15=name, 40=major, 46=num courses.
        StringBuilder line = new StringBuilder(" ".repeat(80));
        write(line, 3, s.studentId);
        write(line, 14, s.name);
        write(line, 39, s.major);
        write(line, 45, s.numCourses);
        pw.println(line.toString().stripTrailing());
    }

    private static void write(StringBuilder line, int col, String value) {
        for (int i = 0; i < value.length() && (col + i) < line.length(); i++) {
            line.setCharAt(col + i, value.charAt(i));
        }
    }
}
