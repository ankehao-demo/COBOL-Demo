package com.coboldemo.serialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JsonGenerateExampleTest {

    @Test
    void recordSerializesToJson() throws Exception {
        JsonGenerateExample.Record record = new JsonGenerateExample.Record(
                "Test Name", "Test Value", "", "true");

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(record);

        assertTrue(json.contains("\"name\""));
        assertTrue(json.contains("\"Test Name\""));
        assertTrue(json.contains("\"value\""));
        assertTrue(json.contains("\"Test Value\""));
        assertTrue(json.contains("\"enabled\""));
        assertTrue(json.contains("\"true\""));
        // Blank field should be excluded (JsonInclude.Include.NON_EMPTY)
        assertFalse(json.contains("ws-record-blank"));
    }

    @Test
    void recordDeserializesFromJson() throws Exception {
        String json = "{\"name\":\"A\",\"value\":\"B\",\"enabled\":\"Y\"}";
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerateExample.Record record = mapper.readValue(json, JsonGenerateExample.Record.class);

        assertEquals("A", record.getRecordName());
        assertEquals("B", record.getRecordValue());
        assertEquals("Y", record.getRecordFlag());
    }
}
