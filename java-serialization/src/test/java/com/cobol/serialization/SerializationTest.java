package com.cobol.serialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SerializationTest {
    
    private SerializationService service;
    private Record testRecord;
    
    @BeforeEach
    public void setUp() {
        service = new SerializationService();
        testRecord = new Record("Test Name", "Test Value", "true", "");
    }
    
    @Test
    public void testJsonGeneration() throws JsonProcessingException {
        String json = service.generateJson(testRecord);
        
        assertNotNull(json);
        assertTrue(json.contains("\"name\":\"Test Name\""));
        assertTrue(json.contains("\"value\":\"Test Value\""));
        assertTrue(json.contains("\"enabled\":\"true\""));
        assertFalse(json.contains("recordBlank"));
        
        System.out.println("Generated JSON for record: " + testRecord);
        System.out.println("----------------------------");
        System.out.println(json);
        System.out.println("----------------------------");
        System.out.println("JSON output character count: " + service.getJsonCharacterCount(json));
    }
    
    @Test
    public void testXmlGeneration() throws JsonProcessingException {
        String xml = service.generateXml(testRecord);
        
        assertNotNull(xml);
        assertTrue(xml.contains("<name>Test Name</name>"));
        assertTrue(xml.contains("<value>Test Value</value>"));
        assertTrue(xml.contains("enabled=\"true\""));
        assertFalse(xml.contains("recordBlank"));
        assertTrue(xml.contains("<?xml"));
        
        System.out.println("Generated XML for record: " + testRecord);
        System.out.println("----------------------------");
        System.out.println(xml);
        System.out.println("----------------------------");
        System.out.println("XML output character count: " + service.getXmlCharacterCount(xml));
    }
    
    @Test
    public void testJsonCharacterCount() throws JsonProcessingException {
        String json = service.generateJson(testRecord);
        int count = service.getJsonCharacterCount(json);
        
        assertTrue(count > 0);
        assertEquals(json.length(), count);
    }
    
    @Test
    public void testXmlCharacterCount() throws JsonProcessingException {
        String xml = service.generateXml(testRecord);
        int count = service.getXmlCharacterCount(xml);
        
        assertTrue(count > 0);
        assertEquals(xml.length(), count);
    }
    
    @Test
    public void testBlankFieldSuppression() throws JsonProcessingException {
        Record recordWithBlank = new Record("Test Name", "Test Value", "true", "   ");
        String json = service.generateJson(recordWithBlank);
        
        assertFalse(json.contains("recordBlank"));
    }
    
    @Test
    public void testXmlAttributeForFlag() throws JsonProcessingException {
        String xml = service.generateXml(testRecord);
        
        assertTrue(xml.contains("enabled=\"true\""));
        assertFalse(xml.contains("<enabled>"));
    }
    
    @Test
    public void testFieldNameMapping() throws JsonProcessingException {
        String json = service.generateJson(testRecord);
        
        assertTrue(json.contains("\"name\""));
        assertTrue(json.contains("\"value\""));
        assertTrue(json.contains("\"enabled\""));
        
        assertFalse(json.contains("recordName"));
        assertFalse(json.contains("recordValue"));
        assertFalse(json.contains("recordFlag"));
    }
    
    @Test
    public void testErrorHandling() throws JsonProcessingException {
        Record nullRecord = null;
        
        String json = service.generateJson(nullRecord);
        assertEquals("null", json);
    }
}
