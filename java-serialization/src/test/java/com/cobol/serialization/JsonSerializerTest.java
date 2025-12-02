package com.cobol.serialization;

import com.cobol.serialization.json.JsonSerializer;
import com.cobol.serialization.model.Record;
import com.cobol.serialization.util.SerializationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for JsonSerializer.
 * Tests verify feature parity with COBOL JSON GENERATE functionality.
 */
class JsonSerializerTest {

    private JsonSerializer serializer;

    @BeforeEach
    void setUp() {
        serializer = new JsonSerializer();
    }

    @Test
    void testBasicJsonGeneration() {
        Record record = new Record();
        record.setName("Test Name");
        record.setValue("Test Value");
        record.setEnabledFlag(true);

        SerializationResult result = serializer.serialize(record);

        assertTrue(result.isSuccess());
        assertNotNull(result.getOutput());
        assertTrue(result.getCharacterCount() > 0);
        assertEquals(0, result.getErrorCode());
    }

    @Test
    void testFieldNameMapping() {
        Record record = new Record();
        record.setName("Test Name");
        record.setValue("Test Value");
        record.setEnabledFlag(true);

        SerializationResult result = serializer.serialize(record);

        assertTrue(result.isSuccess());
        assertTrue(result.getOutput().contains("\"name\""));
        assertTrue(result.getOutput().contains("\"value\""));
        assertTrue(result.getOutput().contains("\"enabled\""));
    }

    @Test
    void testCharacterCountTracking() {
        Record record = new Record();
        record.setName("Test");
        record.setValue("Value");

        SerializationResult result = serializer.serialize(record);

        assertTrue(result.isSuccess());
        assertEquals(result.getOutput().length(), result.getCharacterCount());
    }

    @Test
    void testNullInputHandling() {
        SerializationResult result = serializer.serialize(null);

        assertFalse(result.isSuccess());
        assertEquals(JsonSerializer.ERROR_NULL_INPUT, result.getErrorCode());
        assertNotNull(result.getErrorMessage());
    }

    @Test
    void testEnabledFlagTrue() {
        Record record = new Record();
        record.setName("Test");
        record.setEnabledFlag(true);

        SerializationResult result = serializer.serialize(record);

        assertTrue(result.isSuccess());
        assertTrue(result.getOutput().contains("\"enabled\":\"true\""));
    }

    @Test
    void testEnabledFlagFalse() {
        Record record = new Record();
        record.setName("Test");
        record.setEnabledFlag(false);

        SerializationResult result = serializer.serialize(record);

        assertTrue(result.isSuccess());
        assertTrue(result.getOutput().contains("\"enabled\":\"false\""));
    }

    @Test
    void testPrettyPrint() {
        Record record = new Record();
        record.setName("Test");
        record.setValue("Value");

        JsonSerializer prettySerializer = new JsonSerializer().withPrettyPrint(true);
        SerializationResult result = prettySerializer.serialize(record);

        assertTrue(result.isSuccess());
        assertTrue(result.getOutput().contains("\n"));
    }

    @Test
    void testCompactOutput() {
        Record record = new Record();
        record.setName("Test");
        record.setValue("Value");

        JsonSerializer compactSerializer = new JsonSerializer().withPrettyPrint(false);
        SerializationResult result = compactSerializer.serialize(record);

        assertTrue(result.isSuccess());
        assertFalse(result.getOutput().contains("\n"));
    }

    @Test
    void testJsonStructure() {
        Record record = new Record();
        record.setName("Test Name");
        record.setValue("Test Value");
        record.setBlank("");
        record.setEnabledFlag(true);

        SerializationResult result = serializer.serialize(record);

        assertTrue(result.isSuccess());
        String output = result.getOutput();
        assertTrue(output.startsWith("{"));
        assertTrue(output.endsWith("}"));
    }

    @Test
    void testSpecialCharactersInValues() {
        Record record = new Record();
        record.setName("Test\"Name");
        record.setValue("Test\\Value");

        SerializationResult result = serializer.serialize(record);

        assertTrue(result.isSuccess());
        assertTrue(result.getOutput().contains("\\\""));
        assertTrue(result.getOutput().contains("\\\\"));
    }
}
