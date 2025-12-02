package com.cobol.serialization;

import com.cobol.serialization.model.Record;
import com.cobol.serialization.util.SerializationResult;
import com.cobol.serialization.xml.XmlSerializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for XmlSerializer.
 * Tests verify feature parity with COBOL XML GENERATE functionality.
 */
class XmlSerializerTest {

    private XmlSerializer serializer;

    @BeforeEach
    void setUp() {
        serializer = new XmlSerializer();
    }

    @Test
    void testBasicXmlGeneration() {
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
    void testXmlDeclarationIncluded() {
        Record record = new Record();
        record.setName("Test");
        record.setValue("Value");

        XmlSerializer serializerWithDeclaration = new XmlSerializer()
                .withXmlDeclaration(true);

        SerializationResult result = serializerWithDeclaration.serialize(record);

        assertTrue(result.isSuccess());
        assertTrue(result.getOutput().startsWith("<?xml"));
    }

    @Test
    void testXmlDeclarationExcluded() {
        Record record = new Record();
        record.setName("Test");
        record.setValue("Value");

        XmlSerializer serializerWithoutDeclaration = new XmlSerializer()
                .withXmlDeclaration(false);

        SerializationResult result = serializerWithoutDeclaration.serialize(record);

        assertTrue(result.isSuccess());
        assertFalse(result.getOutput().startsWith("<?xml"));
    }

    @Test
    void testSuppressWhenSpaces() {
        Record record = new Record();
        record.setName("Test Name");
        record.setValue("Test Value");
        record.setBlank("");
        record.setEnabledFlag(true);

        XmlSerializer serializerWithSuppress = new XmlSerializer()
                .withSuppressWhenSpaces(true);

        SerializationResult result = serializerWithSuppress.serialize(record);

        assertTrue(result.isSuccess());
        assertFalse(result.getOutput().contains("ws-record-blank"));
    }

    @Test
    void testNoSuppressWhenSpaces() {
        Record record = new Record();
        record.setName("Test Name");
        record.setValue("Test Value");
        record.setBlank("Not Empty");
        record.setEnabledFlag(true);

        XmlSerializer serializerWithoutSuppress = new XmlSerializer()
                .withSuppressWhenSpaces(false);

        SerializationResult result = serializerWithoutSuppress.serialize(record);

        assertTrue(result.isSuccess());
        assertTrue(result.getOutput().contains("ws-record-blank"));
    }

    @Test
    void testFieldNameMapping() {
        Record record = new Record();
        record.setName("Test Name");
        record.setValue("Test Value");
        record.setEnabledFlag(true);

        SerializationResult result = serializer.serialize(record);

        assertTrue(result.isSuccess());
        assertTrue(result.getOutput().contains("name"));
        assertTrue(result.getOutput().contains("value"));
        assertTrue(result.getOutput().contains("enabled"));
    }

    @Test
    void testAttributeGeneration() {
        Record record = new Record();
        record.setName("Test");
        record.setValue("Value");
        record.setEnabledFlag(true);

        SerializationResult result = serializer.serialize(record);

        assertTrue(result.isSuccess());
        assertTrue(result.getOutput().contains("enabled=\"true\""));
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
        assertEquals(XmlSerializer.ERROR_NULL_INPUT, result.getErrorCode());
        assertNotNull(result.getErrorMessage());
    }

    @Test
    void testEnabledFlagTrue() {
        Record record = new Record();
        record.setName("Test");
        record.setEnabledFlag(true);

        SerializationResult result = serializer.serialize(record);

        assertTrue(result.isSuccess());
        assertTrue(result.getOutput().contains("enabled=\"true\""));
    }

    @Test
    void testEnabledFlagFalse() {
        Record record = new Record();
        record.setName("Test");
        record.setEnabledFlag(false);

        SerializationResult result = serializer.serialize(record);

        assertTrue(result.isSuccess());
        assertTrue(result.getOutput().contains("enabled=\"false\""));
    }
}
