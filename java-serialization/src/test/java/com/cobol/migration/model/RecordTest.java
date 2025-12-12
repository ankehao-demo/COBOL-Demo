package com.cobol.migration.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Record POJO.
 * 
 * Phase 2 TODO:
 * - Test constructor and getters/setters
 * - Test equals/hashCode when implemented
 * - Test builder pattern when implemented
 */
class RecordTest {

    @Test
    void testRecordCreation() {
        Record record = new Record("Test Name", "Test Value", "", true);
        
        assertEquals("Test Name", record.getName());
        assertEquals("Test Value", record.getValue());
        assertEquals("", record.getBlank());
        assertTrue(record.isEnabled());
    }

    @Test
    void testDefaultConstructor() {
        Record record = new Record();
        
        assertNull(record.getName());
        assertNull(record.getValue());
        assertNull(record.getBlank());
        assertFalse(record.isEnabled());
    }

    @Test
    void testSetters() {
        Record record = new Record();
        
        record.setName("Name");
        record.setValue("Value");
        record.setBlank("   ");
        record.setEnabled(true);
        
        assertEquals("Name", record.getName());
        assertEquals("Value", record.getValue());
        assertEquals("   ", record.getBlank());
        assertTrue(record.isEnabled());
    }
}
