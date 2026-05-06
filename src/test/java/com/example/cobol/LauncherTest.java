package com.example.cobol;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.Test;

class LauncherTest {

    @Test
    void printsUsageWhenCalledWithNoArgs() throws Exception {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        PrintStream original = System.out;
        try {
            System.setOut(new PrintStream(bytes));
            Launcher.main(new String[0]);
        } finally {
            System.setOut(original);
        }
        String out = bytes.toString();
        assertTrue(out.contains("Available demos:"), out);
        assertTrue(out.contains("accept"), out);
        assertTrue(out.contains("merge-sort"), out);
        assertTrue(out.contains("xml-generate"), out);
    }
}
