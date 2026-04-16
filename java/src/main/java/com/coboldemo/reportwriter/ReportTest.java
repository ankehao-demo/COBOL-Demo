package com.coboldemo.reportwriter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Migrated from: report_writer/report_test.cbl
 * Original author: Erik Eriksen (2022-05-20)
 * Purpose: Demonstrates REPORT WRITER with page formatting.
 *
 * Reads input.txt (fixed-width format) and generates a formatted report with:
 * - Page headers with page counter
 * - Detail lines with student information
 * - Page footers
 */
public class ReportTest {

    // Constants matching COBOL RD definition
    private static final int PAGE_LIMIT = 20;
    private static final int HEADING_LINE = 1;
    private static final int FIRST_DETAIL = 5;
    private static final int LAST_DETAIL = 15;
    private static final int FOOTING_LINE = 20;

    static class StudentRecord {
        String studentId; // PIC X(6)
        String name;      // PIC X(20)
        String major;     // PIC X(3)
        int numCourses;   // PIC 99

        StudentRecord(String studentId, String name, String major, int numCourses) {
            this.studentId = studentId;
            this.name = name;
            this.major = major;
            this.numCourses = numCourses;
        }
    }

    public static void main(String[] args) throws IOException {
        // Read input data
        List<StudentRecord> records = readInputData();

        // Generate report
        String report = generateReport(records);
        System.out.println(report);
    }

    private static List<StudentRecord> readInputData() throws IOException {
        List<StudentRecord> records = new ArrayList<>();

        // Try to read from classpath resource first, then from file
        InputStream is = ReportTest.class.getResourceAsStream("/input.txt");
        if (is == null) {
            // Try relative path
            is = ReportTest.class.getResourceAsStream("/report_writer/input.txt");
        }

        BufferedReader reader;
        if (is != null) {
            reader = new BufferedReader(new InputStreamReader(is));
        } else {
            // Fallback: use sample data
            System.out.println("Note: input.txt not found on classpath, using embedded sample data.");
            records.add(new StudentRecord("003345", "Test Name2", "PHY", 12));
            records.add(new StudentRecord("000100", "Test Name3", "MAT", 3));
            records.add(new StudentRecord("001234", "Test Name4", "ENG", 7));
            records.add(new StudentRecord("100039", "Test Name5", "CS ", 15));
            records.add(new StudentRecord("000020", "Test Name1", "CS ", 10));
            records.add(new StudentRecord("200100", "Test Name6", "BIO", 5));
            records.add(new StudentRecord("300012", "Test Name7", "CHE", 8));
            return records;
        }

        try (reader) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.length() >= 31) {
                    String padded = String.format("%-31s", line);
                    String studentId = padded.substring(0, 6).trim();
                    String name = padded.substring(6, 26).trim();
                    String major = padded.substring(26, 29).trim();
                    int numCourses;
                    try {
                        numCourses = Integer.parseInt(padded.substring(29, 31).trim());
                    } catch (NumberFormatException e) {
                        numCourses = 0;
                    }
                    records.add(new StudentRecord(studentId, name, major, numCourses));
                }
            }
        }

        return records;
    }

    private static String generateReport(List<StudentRecord> records) {
        StringWriter sw = new StringWriter();
        PrintWriter out = new PrintWriter(sw);

        int pageNum = 1;
        int lineNum = FIRST_DETAIL;
        int recordIndex = 0;

        // Print first page header
        printPageHeader(out, pageNum);

        for (StudentRecord record : records) {
            // Check if we need a new page
            if (lineNum > LAST_DETAIL) {
                printPageFooter(out, pageNum);
                pageNum++;
                printPageHeader(out, pageNum);
                lineNum = FIRST_DETAIL;
            }

            // Print detail line
            out.printf("  %-6s  %-20s  %-3s  %2d%n",
                    record.studentId, record.name, record.major, record.numCourses);
            lineNum++;
            recordIndex++;
        }

        // Print final page footer
        printPageFooter(out, pageNum);

        out.printf("%nTotal records processed: %d%n", recordIndex);

        out.flush();
        return sw.toString();
    }

    private static void printPageHeader(PrintWriter out, int pageNum) {
        out.println();
        out.printf("  STUDENT REPORT                              Page %d%n", pageNum);
        out.println("  ====================================================");
        out.printf("  %-6s  %-20s  %-3s  %s%n", "ID", "NAME", "MAJ", "COURSES");
        out.println("  ------  --------------------  ---  -------");
    }

    private static void printPageFooter(PrintWriter out, int pageNum) {
        out.println("  ====================================================");
        out.printf("  End of Page %d%n", pageNum);
    }
}
