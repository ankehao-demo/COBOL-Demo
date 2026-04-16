package com.coboldemo.reportwriter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Java migration of report_writer/report_test.cbl
 *
 * Original COBOL program (by Erik Eriksen, 2021-01-13):
 *   - Reads input.txt containing fixed-width student records
 *   - Uses COBOL Report Writer (RD) to generate a formatted text report
 *   - Report includes page headers with title and page numbers,
 *     column headers, and detail lines for each student
 *
 * COBOL-to-Java mapping:
 *   FD fd-test-input-file         -> BufferedReader (reading input.txt)
 *   FD fd-report-file             -> PrintWriter (writing report.txt)
 *   RD r-test-report              -> Manual report formatting with PrintWriter
 *   PAGE LIMIT IS 66              -> LINE_LIMIT constant controlling page breaks
 *   HEADING IS 1                  -> Report header printed at top of each page
 *   FIRST DETAIL 6                -> Detail lines start after header
 *   INITIATE r-test-report        -> Print initial page header
 *   GENERATE report-line          -> Print formatted detail line
 *   TERMINATE r-test-report       -> Finalize report output
 *   PIC 9(6)                      -> String (student ID, preserved as-is)
 *   PIC X(20)                     -> String (student name)
 *   PIC XXX                       -> String (major, 3 chars)
 *   PIC 99                        -> String (num courses, 2 chars)
 *   PAGE-COUNTER                  -> int pageNumber, incremented on page break
 *   ws-eof-sw / 88 ws-eof        -> readLine() == null check
 */
public class ReportTest {

    // Fixed-width field sizes for input record (matching COBOL FILE SECTION)
    private static final int STUDENT_ID_WIDTH = 6;
    private static final int STUDENT_NAME_WIDTH = 20;
    private static final int MAJOR_WIDTH = 3;
    private static final int NUM_COURSES_WIDTH = 2;

    /**
     * Line limit per page for report output.
     * In the COBOL program: RD r-test-report PAGE LIMIT IS 66
     * The task specifies LINE-LIMIT 20 for the Java version.
     */
    private static final int LINE_LIMIT = 20;

    /**
     * Tracks the current line number on the page.
     * Equivalent to COBOL's internal LINE-COUNTER special register.
     */
    private int lineCounter;

    /**
     * Tracks the current page number.
     * Equivalent to COBOL's PAGE-COUNTER special register.
     */
    private int pageNumber;

    /**
     * The report output writer.
     * Equivalent to COBOL's FD fd-report-file.
     */
    private PrintWriter reportWriter;

    public ReportTest(PrintWriter reportWriter) {
        this.reportWriter = reportWriter;
        this.lineCounter = 0;
        this.pageNumber = 0;
    }

    /**
     * Prints the report header (page header with title and page number).
     * Equivalent to COBOL:
     *   01 report-header TYPE REPORT HEADING.
     *       05 LINE 1 COLUMN 44 PIC X(21) VALUE "Customer Order Report".
     *       05 LINE 2.
     *           10 COLUMN 100 PIC X(4) VALUE "PAGE".
     *           10 COLUMN 105 PIC ZZ9 SOURCE PAGE-COUNTER.
     *
     * Note: The COBOL report uses "Customer Order Report" as the title
     * but the task calls this a "Student Report". We use "Student Report"
     * as specified in the task requirements.
     */
    private void printPageHeader() {
        pageNumber++;
        // Title line centered
        reportWriter.println();
        reportWriter.printf("%-43s%s%n", "", "Student Report");
        // Page number line right-aligned
        reportWriter.printf("%100s%3d%n", "PAGE", pageNumber);
        reportWriter.println();

        // Column headers (equivalent to the report detail line layout)
        reportWriter.printf("   %-12s%-25s%-8s%s%n",
                "Student ID", "Name", "Major", "Courses");
        reportWriter.printf("   %-12s%-25s%-8s%s%n",
                "----------", "--------------------", "-----", "-------");

        lineCounter = 6; // Headers occupy the first 6 lines (FIRST DETAIL 6)
    }

    /**
     * Initializes the report. Equivalent to COBOL: INITIATE r-test-report
     * Prints the first page header.
     */
    private void initiateReport() {
        System.out.println("Init test report.");
        printPageHeader();
    }

    /**
     * Generates a detail line for a student record.
     * Equivalent to COBOL: GENERATE report-line
     *
     * COBOL report-line definition:
     *   01 report-line TYPE DETAIL LINE PLUS 1.
     *       05 COLUMN 4  PIC 9(6) SOURCE f-test-student-id.
     *       05 COLUMN 15 PIC X(20) SOURCE f-test-student-name.
     *       05 COLUMN 40 PIC XXX SOURCE f-test-major.
     *       05 COLUMN 46 PIC 99 SOURCE f-test-num-courses.
     */
    private void generateDetailLine(String studentId, String name, String major, String numCourses) {
        System.out.println("Generate report line.");

        // Check if we need a page break (LINE PLUS 1 would exceed LAST DETAIL)
        if (lineCounter >= LINE_LIMIT) {
            reportWriter.println(); // Page separator
            printPageHeader();
        }

        // Print detail line matching column layout
        reportWriter.printf("   %-12s%-25s%-8s%s%n", studentId, name, major, numCourses);
        lineCounter++;
    }

    /**
     * Terminates the report. Equivalent to COBOL: TERMINATE r-test-report
     */
    private void terminateReport() {
        System.out.println("Terminate report.");
        reportWriter.flush();
    }

    /**
     * Parses a fixed-width input record into its component fields.
     * Equivalent to COBOL's implicit record parsing when reading from FD:
     *   05 f-test-student-id     PIC 9(6)
     *   05 f-test-student-name   PIC X(20)
     *   05 f-test-major          PIC XXX
     *   05 f-test-num-courses    PIC 99
     */
    private static String[] parseInputRecord(String line) {
        int pos = 0;
        String studentId = line.substring(pos, pos + STUDENT_ID_WIDTH).trim();
        pos += STUDENT_ID_WIDTH;
        String name = line.substring(pos, pos + STUDENT_NAME_WIDTH).trim();
        pos += STUDENT_NAME_WIDTH;
        String major = line.substring(pos, pos + MAJOR_WIDTH).trim();
        pos += MAJOR_WIDTH;
        String numCourses = line.substring(pos, Math.min(pos + NUM_COURSES_WIDTH, line.length())).trim();
        return new String[]{studentId, name, major, numCourses};
    }

    /**
     * Main entry point. Mirrors the COBOL PROCEDURE DIVISION main-procedure:
     *   DISPLAY "Starting test report program."
     *   OPEN INPUT fd-test-input-file OUTPUT fd-report-file
     *   INITIATE r-test-report
     *   PERFORM UNTIL ws-eof
     *       READ fd-test-input-file AT END SET ws-eof TO TRUE
     *       GENERATE report-line
     *   END-PERFORM
     *   TERMINATE r-test-report
     *   CLOSE fd-test-input-file fd-report-file
     *   DISPLAY "Done."
     *   GOBACK
     */
    public static void main(String[] args) throws IOException {
        System.out.println("Starting test report program.");

        // Determine input source: classpath resource or file path argument
        // COBOL: SELECT fd-test-input-file ASSIGN TO "input.txt"
        BufferedReader inputReader;
        if (args.length > 0) {
            inputReader = Files.newBufferedReader(Path.of(args[0]));
        } else {
            InputStream is = ReportTest.class.getResourceAsStream("/input.txt");
            if (is == null) {
                System.err.println("Error: input.txt not found on classpath or as argument.");
                System.exit(1);
                return;
            }
            inputReader = new BufferedReader(new InputStreamReader(is));
        }

        // Determine output path
        // COBOL: SELECT fd-report-file ASSIGN TO "report.txt"
        Path reportPath = Path.of("report.txt");
        if (args.length > 1) {
            reportPath = Path.of(args[1]);
        }

        // OPEN INPUT fd-test-input-file OUTPUT fd-report-file
        try (BufferedReader reader = inputReader;
             PrintWriter writer = new PrintWriter(Files.newBufferedWriter(reportPath))) {

            ReportTest report = new ReportTest(writer);

            // INITIATE r-test-report
            report.initiateReport();

            // PERFORM UNTIL ws-eof
            //     READ fd-test-input-file AT END SET ws-eof TO TRUE
            //     GENERATE report-line
            // END-PERFORM
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }
                String[] fields = parseInputRecord(line);
                report.generateDetailLine(fields[0], fields[1], fields[2], fields[3]);
            }

            // TERMINATE r-test-report
            report.terminateReport();
        }

        // CLOSE fd-test-input-file fd-report-file (handled by try-with-resources)

        System.out.println("Done.");
    }
}
