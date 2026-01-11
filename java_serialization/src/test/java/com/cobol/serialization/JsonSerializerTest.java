package com.cobol.serialization;

import com.cobol.serialization.exception.JsonSerializationException;
import com.cobol.serialization.model.WsRecord;
import com.cobol.serialization.serializer.JsonSerializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for JsonSerializer.
 * Validates that the Java implementation produces output equivalent to COBOL's JSON GENERATE.
 * 
 * Expected COBOL output:
 * {"ws-record":{"name":"Test Name","value":"Test Value","ws-record-blank":" ","enabled":"true"}}
 * Character count: 94
 */
class JsonSerializerTest {
    
    private JsonSerializer serializer;
    private WsRecord testRecord;
    
    @BeforeEach
    void setUp() {
        serializer = new JsonSerializer();
        testRecord = new WsRecord();
        testRecord.setName("Test Name");
        testRecord.setValue("Test Value");
        testRecord.setFlagEnabled(true);
    }
    
    @Test
    @DisplayName("Should generate JSON matching COBOL output format")
    void testGenerateJsonMatchesCobolOutput() throws JsonSerializationException {
        String json = serializer.generate(testRecord);
        
        String expectedJson = "{\"ws-record\":{\"name\":\"Test Name\",\"value\":\"Test Value\",\"ws-record-blank\":\" \",\"enabled\":\"true\"}}";
        assertEquals(expectedJson, json);
    }
    
    @Test
    @DisplayName("Should track character count matching COBOL COUNT IN")
    void testCharacterCountMatchesCobol() throws JsonSerializationException {
        serializer.generate(testRecord);
        
        assertEquals(94, serializer.getCharCount());
    }
    
    @Test
    @DisplayName("Should map field names according to NAME OF clause")
    void testFieldNameMapping() throws JsonSerializationException {
        String json = serializer.generate(testRecord);
        
        assertTrue(json.contains("\"name\":"));
        assertTrue(json.contains("\"value\":"));
        assertTrue(json.contains("\"enabled\":"));
        assertFalse(json.contains("\"ws-record-name\":"));
        assertFalse(json.contains("\"ws-record-value\":"));
        assertFalse(json.contains("\"ws-record-flag\":"));
    }
    
    @Test
    @DisplayName("Should include root element ws-record")
    void testRootElementPresent() throws JsonSerializationException {
        String json = serializer.generate(testRecord);
        
        assertTrue(json.startsWith("{\"ws-record\":"));
    }
    
    @Test
    @DisplayName("Should handle flag enabled state correctly")
    void testFlagEnabledState() throws JsonSerializationException {
        testRecord.setFlagEnabled(true);
        String json = serializer.generate(testRecord);
        
        assertTrue(json.contains("\"enabled\":\"true\""));
    }
    
    @Test
    @DisplayName("Should handle flag disabled state correctly")
    void testFlagDisabledState() throws JsonSerializationException {
        testRecord.setFlagEnabled(false);
        String json = serializer.generate(testRecord);
        
        assertTrue(json.contains("\"enabled\":\"false\""));
    }
    
    @Test
    @DisplayName("Should format blank field as single space when empty")
    void testBlankFieldFormattedAsSpace() throws JsonSerializationException {
        testRecord.setBlank("");
        String json = serializer.generate(testRecord);
        
        assertTrue(json.contains("\"ws-record-blank\":\" \""));
    }
    
    @Test
    @DisplayName("Should suppress blank fields when suppressBlanks is true")
    void testSuppressBlankFields() throws JsonSerializationException {
        testRecord.setBlank("");
        String json = serializer.generate(testRecord, true);
        
        assertFalse(json.contains("ws-record-blank"));
    }
    
    @Test
    @DisplayName("Should include blank fields when suppressBlanks is false")
    void testIncludeBlankFields() throws JsonSerializationException {
        testRecord.setBlank("");
        String json = serializer.generate(testRecord, false);
        
        assertTrue(json.contains("ws-record-blank"));
    }
    
    @Test
    @DisplayName("Should throw JsonSerializationException for null record")
    void testNullRecordThrowsException() {
        JsonSerializationException exception = assertThrows(
            JsonSerializationException.class,
            () -> serializer.generate(null)
        );
        
        assertEquals(JsonSerializationException.ERROR_INVALID_DATA, exception.getJsonCode());
    }
    
    @Test
    @DisplayName("Should handle non-empty blank field correctly")
    void testNonEmptyBlankField() throws JsonSerializationException {
        testRecord.setBlank("Some Data");
        String json = serializer.generate(testRecord);
        
        assertTrue(json.contains("\"ws-record-blank\":\"Some Data\""));
    }
    
    @Test
    @DisplayName("Should preserve non-empty blank field even with suppression enabled")
    void testNonEmptyBlankFieldWithSuppression() throws JsonSerializationException {
        testRecord.setBlank("Some Data");
        String json = serializer.generate(testRecord, true);
        
        assertTrue(json.contains("\"ws-record-blank\":\"Some Data\""));
    }
}
