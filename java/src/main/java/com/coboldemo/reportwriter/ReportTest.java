package com.coboldemo.reportwriter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 * Migrated from report_writer/report_test.cbl
 * Generates a formatted text report from fixed-width input records.
 */
public class ReportTest {

    private static final int PAGE_LIMIT = 66;
    private static final int FIRST_DETAIL = 6;
    private static final int LAST_DETAIL = 42;

    public static void main(String[] args) {
        System.out.println("Starting test report program.");

        try (InputStream is = ReportTest.class.getResourceAsStream("/input.txt");
             PrintWriter reportWriter = new PrintWriter("report.txt")) {

            if (is == null) {
                System.err.println("Error: input.txt not found in classpath resources.");
                System.exit(1);
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(is));

            System.out.println("Init test report.");

            int pageNumber = 1;
            int currentLine = 1;

            // Write report header
            writeHeader(reportWriter, pageNumber);
            currentLine = FIRST_DETAIL;

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isEmpty()) continue;

                // Check if we need a new page
                if (currentLine > LAST_DETAIL) {
                    pageNumber++;
                    // Pad remaining lines on current page
                    while (currentLine <= PAGE_LIMIT) {
                        reportWriter.println();
                        currentLine++;
                    }
                    writeHeader(reportWriter, pageNumber);
                    currentLine = FIRST_DETAIL;
                }

                // Parse fixed-width record
                // Student ID: positions 0-5 (6 chars)
                // Student Name: positions 6-25 (20 chars)
                // Major: positions 26-28 (3 chars)
                // Num Courses: positions 29-30 (2 chars)
                String paddedLine = padRight(line, 31);
                String studentId = paddedLine.substring(0, 6);
                String studentName = paddedLine.substring(6, 26);
                String major = paddedLine.substring(26, 29);
                String numCourses = paddedLine.substring(29, 31);

                System.out.println("Generate report line.");

                // Write detail line
                reportWriter.printf("   %s      %s    %s %s%n",
                        studentId, studentName, major, numCourses);
                currentLine++;
            }

            System.out.println("Terminate report.");

        } catch (IOException e) {
            System.err.println("Error processing report: " + e.getMessage());
            System.exit(1);
        }

        System.out.println("Done.");
    }

    private static void writeHeader(PrintWriter writer, int pageNumber) {
        // Line 1: Report title centered
        writer.printf("%44s%n", "Customer Order Report");

        // Line 2: Page number right-aligned
        writer.printf("%104s%3d%n", "PAGE", pageNumber);

        // Blank lines before first detail
        writer.println();
        writer.println();
        writer.println();
    }

    private static String padRight(String s, int width) {
        if (s.length() >= width) return s;
        return String.format("%-" + width + "s", s);
    }
}
