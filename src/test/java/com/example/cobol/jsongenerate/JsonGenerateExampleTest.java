package com.example.cobol.jsongenerate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

class JsonGenerateExampleTest {

    @Test
    void generatesExpectedJsonFields() throws Exception {
        JsonGenerateExample.Record r = new JsonGenerateExample.Record();
        r.name = "Test Name";
        r.value = "Test Value";
        r.enabled = "true";

        String json = new ObjectMapper().writeValueAsString(r);
        assertTrue(json.contains("\"name\":\"Test Name\""), json);
        assertTrue(json.contains("\"value\":\"Test Value\""), json);
        assertTrue(json.contains("\"enabled\":\"true\""), json);
    }

    @Test
    void mainPrintsCharacterCount() throws Exception {
        java.io.ByteArrayOutputStream bytes = new java.io.ByteArrayOutputStream();
        java.io.PrintStream original = System.out;
        try {
            System.setOut(new java.io.PrintStream(bytes));
            JsonGenerateExample.main(new String[0]);
        } finally {
            System.setOut(original);
        }
        String out = bytes.toString();
        assertTrue(out.contains("Done."), out);
        assertEquals(1, out.split("Done\\.", -1).length - 1, out);
    }
}
