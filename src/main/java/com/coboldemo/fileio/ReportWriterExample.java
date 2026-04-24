package com.coboldemo.fileio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Migrated from: report_writer/report_test.cbl
 *
 * Demonstrates COBOL Report Writer functionality. The COBOL version uses
 * the declarative RD (Report Description) with PAGE LIMIT, HEADING,
 * FIRST DETAIL, LAST DETAIL, and FOOTING. This Java version reimplements
 * it imperatively: reading input.txt line by line, parsing fixed-width
 * fields, and writing a formatted report with headers and page breaks.
 */
public class ReportWriterExample {

    private static final int PAGE_LIMIT = 66;
    private static final int FIRST_DETAIL_LINE = 6;
    private static final int LAST_DETAIL_LINE = 42;
    private static int currentLine = 0;
    private static int pageCounter = 0;

    public static void main(String[] args) throws IOException {
        Path inputPath = Path.of("report_writer", "input.txt");
        Path reportPath = Path.of("report.txt");

        if (!Files.exists(inputPath)) {
            // Try alternate location
            inputPath = Path.of("input.txt");
        }

        System.out.println("Starting test report program.");

        try (BufferedReader reader = Files.newBufferedReader(inputPath);
             PrintWriter writer = new PrintWriter(Files.newBufferedWriter(reportPath))) {

            System.out.println("Init test report.");

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;

                // Parse fixed-width fields from input
                // PIC 9(6) + PIC X(20) + PIC XXX + PIC 99
                String studentId = safeSubstring(line, 0, 6);
                String studentName = safeSubstring(line, 6, 26);
                String major = safeSubstring(line, 26, 29);
                String numCourses = safeSubstring(line, 29, 31);

                // Check if we need a new page
                if (currentLine == 0 || currentLine >= LAST_DETAIL_LINE) {
                    writePageHeader(writer);
                }

                System.out.println("Generate report line.");
                writeDetailLine(writer, studentId, studentName, major, numCourses);
            }

            System.out.println("Terminate report.");
        }

        System.out.println("Done.");
    }

    private static void writePageHeader(PrintWriter writer) {
        pageCounter++;

        // Start a new page (form feed if not the first page)
        if (pageCounter > 1) {
            writer.println("\f");
        }

        // Report header at line 1, column 44
        writer.printf("%-43s%s%n", "", "Customer Order Report");

        // Page number at line 2
        writer.printf("%-99s%s%3d%n", "", "PAGE", pageCounter);

        // Blank lines until first detail line
        for (int i = 3; i < FIRST_DETAIL_LINE; i++) {
            writer.println();
        }

        currentLine = FIRST_DETAIL_LINE;
    }

    private static void writeDetailLine(PrintWriter writer, String studentId,
                                         String studentName, String major,
                                         String numCourses) {
        writer.printf("   %s     %s     %s %s%n",
                studentId, studentName, major, numCourses);
        currentLine++;
    }

    private static String safeSubstring(String s, int start, int end) {
        if (s == null) return "";
        if (start >= s.length()) return "";
        if (end > s.length()) end = s.length();
        return s.substring(start, end);
    }
}
