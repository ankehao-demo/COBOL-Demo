package com.example.cobol.migration;

import com.example.cobol.migration.model.Record;
import com.example.cobol.migration.serializer.JsonSerializer;
import com.example.cobol.migration.serializer.XmlSerializer;
import com.example.cobol.migration.util.CobolDataConverter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SerializationTest {
    
    @Test
    public void testXmlSerializationBasic() {
        Record record = new Record();
        record.setName("Test Name");
        record.setValue("Test Value");
        record.setBlank("");
        record.setEnabledFlag(true);
        
        XmlSerializer serializer = new XmlSerializer();
        XmlSerializer.SerializationResult result = serializer.serialize(record);
        
        assertTrue(result.isSuccess(), "XML serialization should succeed");
        assertNotNull(result.getOutput(), "XML output should not be null");
        assertTrue(result.getOutput().contains("<?xml"), "XML should contain declaration");
        assertTrue(result.getOutput().contains("<ws-record"), "XML should contain root element");
        assertTrue(result.getOutput().contains("enabled=\"true\""), "XML should have enabled attribute");
        assertTrue(result.getOutput().contains("<name>Test Name</name>"), "XML should contain name element");
        assertTrue(result.getOutput().contains("<value>Test Value</value>"), "XML should contain value element");
        assertFalse(result.getOutput().contains("<blank>"), "XML should suppress blank field");
        assertTrue(result.getCharCount() > 0, "Character count should be positive");
    }
    
    @Test
    public void testJsonSerializationBasic() {
        Record record = new Record();
        record.setName("Test Name");
        record.setValue("Test Value");
        record.setBlank("");
        record.setEnabledFlag(true);
        
        JsonSerializer serializer = new JsonSerializer();
        JsonSerializer.SerializationResult result = serializer.serialize(record);
        
        assertTrue(result.isSuccess(), "JSON serialization should succeed");
        assertNotNull(result.getOutput(), "JSON output should not be null");
        assertTrue(result.getOutput().contains("\"name\":\"Test Name\""), "JSON should contain name field");
        assertTrue(result.getOutput().contains("\"value\":\"Test Value\""), "JSON should contain value field");
        assertTrue(result.getOutput().contains("\"enabled\":\"true\""), "JSON should contain enabled field");
        assertFalse(result.getOutput().contains("\"blank\""), "JSON should suppress blank field");
        assertTrue(result.getCharCount() > 0, "Character count should be positive");
    }
    
    @Test
    public void testXmlSerializationWithSpaces() {
        Record record = new Record();
        record.setName("Test Name  ");
        record.setValue("Test Value  ");
        record.setBlank("   ");
        record.setEnabledFlag(false);
        
        XmlSerializer serializer = new XmlSerializer();
        XmlSerializer.SerializationResult result = serializer.serialize(record);
        
        assertTrue(result.isSuccess(), "XML serialization should succeed");
        assertTrue(result.getOutput().contains("<name>Test Name</name>"), "XML should trim trailing spaces");
        assertTrue(result.getOutput().contains("<value>Test Value</value>"), "XML should trim trailing spaces");
        assertFalse(result.getOutput().contains("<blank>"), "XML should suppress space-only field");
        assertTrue(result.getOutput().contains("enabled=\"false\""), "XML should have enabled=false attribute");
    }
    
    @Test
    public void testJsonSerializationWithSpaces() {
        Record record = new Record();
        record.setName("Test Name  ");
        record.setValue("Test Value  ");
        record.setBlank("   ");
        record.setEnabledFlag(false);
        
        JsonSerializer serializer = new JsonSerializer();
        JsonSerializer.SerializationResult result = serializer.serialize(record);
        
        assertTrue(result.isSuccess(), "JSON serialization should succeed");
        assertTrue(result.getOutput().contains("\"name\":\"Test Name\""), "JSON should trim trailing spaces");
        assertTrue(result.getOutput().contains("\"value\":\"Test Value\""), "JSON should trim trailing spaces");
        assertFalse(result.getOutput().contains("\"blank\""), "JSON should suppress space-only field");
        assertTrue(result.getOutput().contains("\"enabled\":\"false\""), "JSON should have enabled=false");
    }
    
    @Test
    public void testCobolDataConverterTrimming() {
        assertEquals("Test", CobolDataConverter.trimCobolString("Test   "));
        assertEquals("Test", CobolDataConverter.trimCobolString("Test"));
        assertEquals("", CobolDataConverter.trimCobolString("   "));
        assertNull(CobolDataConverter.trimCobolString(null));
    }
    
    @Test
    public void testCobolDataConverterPadding() {
        assertEquals("Test      ", CobolDataConverter.padCobolString("Test", 10));
        assertEquals("Test", CobolDataConverter.padCobolString("TestTooLong", 4));
        assertEquals("          ", CobolDataConverter.padCobolString(null, 10));
    }
    
    @Test
    public void testCobolDataConverterNumeric() {
        assertEquals(1234, CobolDataConverter.cobolNumericToInt("1234"));
        assertEquals(1234, CobolDataConverter.cobolNumericToInt("  1234  "));
        assertEquals("0042", CobolDataConverter.intToCobolNumeric(42, 4));
    }
    
    @Test
    public void testRecordEnabledFlag() {
        Record record = new Record();
        
        record.setEnabledFlag(true);
        assertEquals("true", record.getEnabled());
        assertTrue(record.isEnabledFlag());
        
        record.setEnabledFlag(false);
        assertEquals("false", record.getEnabled());
        assertFalse(record.isEnabledFlag());
    }
    
    @Test
    public void testXmlSerializationEmptyFields() {
        Record record = new Record();
        record.setName("");
        record.setValue("");
        record.setBlank("");
        record.setEnabledFlag(true);
        
        XmlSerializer serializer = new XmlSerializer();
        XmlSerializer.SerializationResult result = serializer.serialize(record);
        
        assertTrue(result.isSuccess(), "XML serialization should succeed");
        assertFalse(result.getOutput().contains("<name>"), "XML should suppress empty name field");
        assertFalse(result.getOutput().contains("<value>"), "XML should suppress empty value field");
        assertFalse(result.getOutput().contains("<blank>"), "XML should suppress empty blank field");
        assertTrue(result.getOutput().contains("enabled=\"true\""), "XML should still have enabled attribute");
    }
    
    @Test
    public void testJsonSerializationEmptyFields() {
        Record record = new Record();
        record.setName("");
        record.setValue("");
        record.setBlank("");
        record.setEnabledFlag(true);
        
        JsonSerializer serializer = new JsonSerializer();
        JsonSerializer.SerializationResult result = serializer.serialize(record);
        
        assertTrue(result.isSuccess(), "JSON serialization should succeed");
        assertFalse(result.getOutput().contains("\"name\""), "JSON should suppress empty name field");
        assertFalse(result.getOutput().contains("\"value\""), "JSON should suppress empty value field");
        assertFalse(result.getOutput().contains("\"blank\""), "JSON should suppress empty blank field");
        assertTrue(result.getOutput().contains("\"enabled\":\"true\""), "JSON should still have enabled field");
    }
}
