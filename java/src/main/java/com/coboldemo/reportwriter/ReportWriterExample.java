package com.coboldemo.reportwriter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Java equivalent of report_writer/report_test.cbl
 *
 * Demonstrates the COBOL Report Writer feature (REPORT SECTION, RD, INITIATE,
 * GENERATE, TERMINATE). In Java, formatted report output is handled manually
 * with PrintWriter and String.format, tracking line counts and page breaks.
 *
 * COBOL Mapping:
 *   REPORT SECTION / RD       → manual page layout tracking
 *   PAGE LIMIT / HEADING etc. → constants for line counts
 *   INITIATE                  → open output, print header
 *   GENERATE                  → print formatted detail line
 *   TERMINATE                 → close output
 *   PAGE-COUNTER              → manual counter variable
 *
 * Input file: report_input.txt (classpath resource or local file)
 * Record format: studentId(6) + studentName(20) + major(3) + numCourses(2)
 */
public class ReportWriterExample {

    private static final int PAGE_LIMIT = 66;
    private static final int HEADING_LINE = 1;
    private static final int FIRST_DETAIL = 6;
    private static final int LAST_DETAIL = 42;
    private static final int FOOTING_LINE = 52;

    private static int currentLine = 0;
    private static int pageCounter = 0;

    public static void main(String[] args) throws IOException {
        System.out.println("Starting test report program.");

        // Try to read input from classpath resource first, then fall back to file
        BufferedReader inputReader;
        var resourceStream = ReportWriterExample.class.getResourceAsStream("/report_input.txt");
        if (resourceStream != null) {
            inputReader = new BufferedReader(new InputStreamReader(resourceStream));
        } else {
            inputReader = Files.newBufferedReader(Path.of("report_input.txt"));
        }

        PrintWriter reportWriter = new PrintWriter(Files.newBufferedWriter(Path.of("report.txt")));

        // INITIATE r-test-report
        System.out.println("Init test report.");
        pageCounter = 0;
        currentLine = 0;
        printPageHeader(reportWriter);

        // Read and generate report lines
        String line;
        while ((line = inputReader.readLine()) != null) {
            if (line.isBlank()) continue;

            // Parse fixed-width record
            String padded = String.format("%-31s", line);
            String studentId = padded.substring(0, 6);
            String studentName = padded.substring(6, 26);
            String major = padded.substring(26, 29);
            String numCourses = padded.substring(29, 31);

            System.out.println("Generate report line.");

            // Check if we need a new page
            if (currentLine > LAST_DETAIL) {
                // Print page footer area (blank lines to footing)
                while (currentLine < PAGE_LIMIT) {
                    reportWriter.println();
                    currentLine++;
                }
                printPageHeader(reportWriter);
            }

            // Advance to first detail line if needed
            while (currentLine < FIRST_DETAIL) {
                reportWriter.println();
                currentLine++;
            }

            // TYPE DETAIL: formatted detail line
            reportWriter.printf("   %-6s      %-20s   %-3s %s%n",
                    studentId, studentName, major, numCourses);
            currentLine++;
        }

        // TERMINATE r-test-report
        System.out.println("Terminate report.");
        reportWriter.flush();
        reportWriter.close();
        inputReader.close();

        System.out.println("Done.");
    }

    /** Print the report header at the top of each page. */
    private static void printPageHeader(PrintWriter writer) {
        pageCounter++;
        currentLine = HEADING_LINE;

        // TYPE REPORT HEADING line 1
        writer.printf("%43s%21s%n", "", "Customer Order Report");
        currentLine++;

        // Line 2: page number
        writer.printf("%99s%4s%3d%n", "", "PAGE", pageCounter);
        currentLine++;
    }
}
