package com.example.cobol.reportwriter;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.junit.jupiter.api.Test;

class ReportTestTest {

    @Test
    void parsesBundledInput() throws IOException {
        try (java.io.InputStream in = ReportTestTest.class
                .getResourceAsStream("/reportwriter/input.txt")) {
            assertTrue(in != null, "bundled input.txt should be on the classpath");
            try (BufferedReader r = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
                List<ReportTest.Student> students = ReportTest.parse(r);
                assertFalse(students.isEmpty(), "expected sample input to contain students");
            }
        }
    }

    @Test
    void writesPagedReportWithHeader() throws IOException {
        StringWriter sw = new StringWriter();
        try (PrintWriter pw = new PrintWriter(sw)) {
            ReportTest.writeReport(pw, List.of(
                new ReportTest.Student("000001", "Test Name1          ", "PHY", "12"),
                new ReportTest.Student("000002", "Test Name2          ", "MAT", "08")
            ));
        }
        String out = sw.toString();
        assertTrue(out.contains("Customer Order Report"), out);
        assertTrue(out.contains("PAGE"), out);
    }
}
