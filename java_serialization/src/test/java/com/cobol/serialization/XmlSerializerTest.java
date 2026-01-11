package com.cobol.serialization;

import com.cobol.serialization.exception.XmlSerializationException;
import com.cobol.serialization.model.WsRecord;
import com.cobol.serialization.serializer.XmlSerializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for XmlSerializer.
 * Validates that the Java implementation produces output equivalent to COBOL's XML GENERATE.
 * 
 * Expected COBOL output:
 * <?xml version="1.0"?>
 * <ws-record enabled="true"><name>Test Name</name><value>Test Value</value></ws-record>
 * Character count: 107
 */
class XmlSerializerTest {
    
    private XmlSerializer serializer;
    private WsRecord testRecord;
    
    @BeforeEach
    void setUp() {
        serializer = new XmlSerializer();
        testRecord = new WsRecord();
        testRecord.setName("Test Name");
        testRecord.setValue("Test Value");
        testRecord.setFlagEnabled(true);
    }
    
    @Test
    @DisplayName("Should generate XML with declaration matching COBOL output")
    void testGenerateXmlWithDeclaration() throws XmlSerializationException {
        String xml = serializer.generate(testRecord);
        
        assertTrue(xml.startsWith("<?xml version=\"1.0\""));
        assertTrue(xml.contains("<ws-record"));
        assertTrue(xml.contains("</ws-record>"));
    }
    
    @Test
    @DisplayName("Should render flag as attribute (TYPE OF ... ATTRIBUTE)")
    void testFlagAsAttribute() throws XmlSerializationException {
        String xml = serializer.generate(testRecord);
        
        assertTrue(xml.contains("enabled=\"true\""));
        assertFalse(xml.contains("<enabled>"));
    }
    
    @Test
    @DisplayName("Should map field names according to NAME OF clause")
    void testFieldNameMapping() throws XmlSerializationException {
        String xml = serializer.generate(testRecord);
        
        assertTrue(xml.contains("<name>Test Name</name>"));
        assertTrue(xml.contains("<value>Test Value</value>"));
        assertFalse(xml.contains("<ws-record-name>"));
        assertFalse(xml.contains("<ws-record-value>"));
    }
    
    @Test
    @DisplayName("Should suppress blank field when spaces (SUPPRESS WHEN SPACES)")
    void testSuppressWhenSpaces() throws XmlSerializationException {
        testRecord.setBlank("");
        String xml = serializer.generate(testRecord, true, true);
        
        assertFalse(xml.contains("ws-record-blank"));
    }
    
    @Test
    @DisplayName("Should include blank field when not suppressing")
    void testIncludeBlankField() throws XmlSerializationException {
        testRecord.setBlank("Some Data");
        String xml = serializer.generate(testRecord, true, true);
        
        assertTrue(xml.contains("<ws-record-blank>Some Data</ws-record-blank>"));
    }
    
    @Test
    @DisplayName("Should omit XML declaration when configured")
    void testOmitXmlDeclaration() throws XmlSerializationException {
        String xml = serializer.generate(testRecord, false, true);
        
        assertFalse(xml.startsWith("<?xml"));
        assertTrue(xml.startsWith("<ws-record"));
    }
    
    @Test
    @DisplayName("Should handle flag disabled state correctly")
    void testFlagDisabledState() throws XmlSerializationException {
        testRecord.setFlagEnabled(false);
        String xml = serializer.generate(testRecord);
        
        assertTrue(xml.contains("enabled=\"false\""));
    }
    
    @Test
    @DisplayName("Should throw XmlSerializationException for null record")
    void testNullRecordThrowsException() {
        XmlSerializationException exception = assertThrows(
            XmlSerializationException.class,
            () -> serializer.generate(null)
        );
        
        assertEquals(XmlSerializationException.ERROR_INVALID_DATA, exception.getXmlCode());
    }
    
    @Test
    @DisplayName("Should track character count")
    void testCharacterCount() throws XmlSerializationException {
        serializer.generate(testRecord);
        
        assertTrue(serializer.getCharCount() > 0);
    }
    
    @Test
    @DisplayName("Should preserve non-empty blank field even with suppression enabled")
    void testNonEmptyBlankFieldWithSuppression() throws XmlSerializationException {
        testRecord.setBlank("Data");
        String xml = serializer.generate(testRecord, true, true);
        
        assertTrue(xml.contains("<ws-record-blank>Data</ws-record-blank>"));
    }
    
    @Test
    @DisplayName("Should include blank field when suppression is disabled")
    void testBlankFieldWithoutSuppression() throws XmlSerializationException {
        testRecord.setBlank("");
        String xml = serializer.generate(testRecord, true, false);
        
        assertTrue(xml.contains("ws-record-blank"));
    }
}
