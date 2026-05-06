package com.example.cobol.search;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.jupiter.api.Test;

class SearchExampleTest {

    @Test
    void mainHandlesScriptedInput() {
        InputStream original = System.in;
        try {
            String input = "1\n1\n101\n500\n2\n";
            System.setIn(new ByteArrayInputStream(input.getBytes()));
            assertDoesNotThrow(() -> SearchExample.main(new String[0]));
        } finally {
            System.setIn(original);
        }
    }
}
