package com.cobol.serialization;

import com.cobol.serialization.model.WsRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for WsRecord POJO.
 * Validates that the Java model correctly mirrors COBOL record structure.
 */
class WsRecordTest {
    
    private WsRecord record;
    
    @BeforeEach
    void setUp() {
        record = new WsRecord();
    }
    
    @Test
    @DisplayName("Should initialize with default values")
    void testDefaultValues() {
        assertEquals("", record.getName());
        assertEquals("", record.getValue());
        assertEquals("", record.getBlank());
        assertEquals("false", record.getFlag());
        assertTrue(record.isFlagDisabled());
        assertFalse(record.isFlagEnabled());
    }
    
    @Test
    @DisplayName("Should truncate name to 10 characters (PIC X(10))")
    void testNameTruncation() {
        record.setName("This is a very long name");
        assertEquals("This is a ", record.getName());
    }
    
    @Test
    @DisplayName("Should truncate value to 10 characters (PIC X(10))")
    void testValueTruncation() {
        record.setValue("This is a very long value");
        assertEquals("This is a ", record.getValue());
    }
    
    @Test
    @DisplayName("Should truncate blank to 10 characters (PIC X(10))")
    void testBlankTruncation() {
        record.setBlank("This is a very long blank");
        assertEquals("This is a ", record.getBlank());
    }
    
    @Test
    @DisplayName("Should implement 88-level condition ws-record-flag-enabled")
    void testFlagEnabled() {
        record.setFlagEnabled(true);
        assertTrue(record.isFlagEnabled());
        assertFalse(record.isFlagDisabled());
        assertEquals("true", record.getFlag());
    }
    
    @Test
    @DisplayName("Should implement 88-level condition ws-record-flag-disabled")
    void testFlagDisabled() {
        record.setFlagEnabled(false);
        assertFalse(record.isFlagEnabled());
        assertTrue(record.isFlagDisabled());
        assertEquals("false", record.getFlag());
    }
    
    @Test
    @DisplayName("Should only accept valid flag values")
    void testInvalidFlagValue() {
        record.setFlag("invalid");
        assertEquals("false", record.getFlag());
    }
    
    @Test
    @DisplayName("Should detect empty blank field")
    void testIsBlankEmpty() {
        record.setBlank("");
        assertTrue(record.isBlankEmpty());
        
        record.setBlank("   ");
        assertTrue(record.isBlankEmpty());
        
        record.setBlank("data");
        assertFalse(record.isBlankEmpty());
    }
    
    @Test
    @DisplayName("Should generate COBOL-style padded string")
    void testToCobolString() {
        record.setName("Test");
        record.setValue("Value");
        record.setBlank("");
        record.setFlagEnabled(true);
        
        String cobolString = record.toCobolString();
        assertEquals(35, cobolString.length());
        assertTrue(cobolString.startsWith("Test      "));
        assertTrue(cobolString.endsWith("true "));
    }
    
    @Test
    @DisplayName("Should handle null values gracefully")
    void testNullValues() {
        record.setName(null);
        record.setValue(null);
        record.setBlank(null);
        
        assertEquals("", record.getName());
        assertEquals("", record.getValue());
        assertEquals("", record.getBlank());
    }
    
    @Test
    @DisplayName("Should construct with all parameters")
    void testParameterizedConstructor() {
        WsRecord paramRecord = new WsRecord("Name", "Value", "Blank", true);
        
        assertEquals("Name", paramRecord.getName());
        assertEquals("Value", paramRecord.getValue());
        assertEquals("Blank", paramRecord.getBlank());
        assertTrue(paramRecord.isFlagEnabled());
    }
}
