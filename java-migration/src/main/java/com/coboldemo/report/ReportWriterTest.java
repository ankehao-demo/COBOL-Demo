package com.coboldemo.report;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Java port of {@code report_writer/report_test.cbl}.
 *
 * Reads fixed-width student records from {@code input.txt} (matching the
 * layout id(6), name(20), major(3), numCourses(2)) and writes a paginated
 * "Customer Order Report" to {@code report.txt}.
 *
 * The COBOL program uses the GnuCOBOL Report Writer feature with page
 * limits of 66 lines per page and 42 detail lines. We reproduce those
 * limits explicitly here using {@link PrintWriter}.
 */
public class ReportWriterTest {

    private static final int RECORD_WIDTH = 6 + 20 + 3 + 2;
    private static final int LINES_PER_PAGE = 66;
    private static final int FIRST_DETAIL = 6;
    private static final int LAST_DETAIL = 42;

    public static final class StudentRecord {
        public final int id;
        public final String name;
        public final String major;
        public final int numCourses;

        public StudentRecord(int id, String name, String major, int numCourses) {
            this.id = id;
            this.name = name;
            this.major = major;
            this.numCourses = numCourses;
        }
    }

    public static void main(String[] args) throws IOException {
        Path input = locateInput(args);
        Path output = Paths.get("report.txt");

        System.out.println("Starting test report program.");
        System.out.println("Init test report.");

        try (BufferedReader r = Files.newBufferedReader(input, StandardCharsets.US_ASCII);
             PrintWriter w = new PrintWriter(Files.newBufferedWriter(output, StandardCharsets.US_ASCII))) {

            int pageNumber = 1;
            int detailLineNumber = FIRST_DETAIL;
            writePageHeader(w, pageNumber);

            String line;
            while ((line = r.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }
                StudentRecord rec = parse(line);
                System.out.println("Generate report line.");

                if (detailLineNumber > LAST_DETAIL) {
                    pageNumber++;
                    writePageHeader(w, pageNumber);
                    detailLineNumber = FIRST_DETAIL;
                }
                writeDetail(w, rec);
                detailLineNumber++;
            }

            System.out.println("Terminate report.");
        }

        System.out.println("Done.");
        System.out.println("Report written to " + output.toAbsolutePath());
    }

    private static Path locateInput(String[] args) {
        if (args.length > 0) {
            return Paths.get(args[0]);
        }
        // Look in CWD first, then up one level into the original
        // report_writer directory of the COBOL repo.
        Path candidate = Paths.get("input.txt");
        if (Files.exists(candidate)) {
            return candidate;
        }
        Path repoRelative = Paths.get("..", "report_writer", "input.txt");
        if (Files.exists(repoRelative)) {
            return repoRelative;
        }
        return candidate;
    }

    private static StudentRecord parse(String raw) {
        String padded = raw;
        if (padded.length() < RECORD_WIDTH) {
            padded = String.format("%-" + RECORD_WIDTH + "s", padded);
        }
        int id = Integer.parseInt(padded.substring(0, 6));
        String name = padded.substring(6, 26).trim();
        String major = padded.substring(26, 29).trim();
        int numCourses = parseIntSafe(padded.substring(29, 31).trim());
        return new StudentRecord(id, name, major, numCourses);
    }

    private static int parseIntSafe(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private static void writePageHeader(PrintWriter w, int pageNumber) {
        // Line 1, column 44: "Customer Order Report"
        w.println(padToColumn("", 44) + "Customer Order Report");
        // Line 2, column 100: "PAGE", column 105: page number (zz9)
        w.println(padToColumn("", 100) + "PAGE " + String.format("%3d", pageNumber));
        // Blank lines up to FIRST_DETAIL (line 6)
        for (int i = 3; i < FIRST_DETAIL; i++) {
            w.println();
        }
    }

    private static void writeDetail(PrintWriter w, StudentRecord rec) {
        StringBuilder sb = new StringBuilder();
        // column 4: 6-digit id
        sb.append(padToColumn(sb.toString(), 4));
        sb.append(String.format("%06d", rec.id));
        // column 15: 20-char name
        appendAtColumn(sb, 15, padRight(rec.name, 20));
        // column 40: 3-char major
        appendAtColumn(sb, 40, padRight(rec.major, 3));
        // column 46: 2-digit course count
        appendAtColumn(sb, 46, String.format("%02d", rec.numCourses));
        w.println(sb);
    }

    private static String padToColumn(String existing, int targetCol) {
        int pad = targetCol - 1 - existing.length();
        if (pad <= 0) {
            return "";
        }
        return " ".repeat(pad);
    }

    private static void appendAtColumn(StringBuilder sb, int targetCol, String value) {
        int pad = targetCol - 1 - sb.length();
        if (pad > 0) {
            sb.append(" ".repeat(pad));
        }
        sb.append(value);
    }

    private static String padRight(String s, int width) {
        if (s.length() >= width) {
            return s.substring(0, width);
        }
        return String.format("%-" + width + "s", s);
    }
}
