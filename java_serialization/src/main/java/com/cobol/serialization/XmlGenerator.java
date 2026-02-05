package com.cobol.serialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;

import javax.xml.stream.XMLOutputFactory;
import java.io.StringWriter;

/**
 * XML Generator - Java equivalent of COBOL xml_generate.cbl
 * 
 * Replicates the COBOL XML GENERATE statement functionality:
 * <pre>
 * xml generate ws-xml-output
 *     from ws-record
 *     count in ws-xml-char-count
 *     with xml-declaration
 *     name of
 *         ws-record-name is "name",
 *         ws-record-value is "value",
 *         ws-record-flag is "enabled"
 *     type of ws-record-flag is attribute
 *     suppress when spaces
 *     on exception
 *         display "Error generating xml error " XML-CODE
 *         stop run
 *     not on exception
 *         display "XML document successfully generated."
 * end-xml
 * </pre>
 * 
 * Features implemented:
 * - XML declaration (with xml-declaration)
 * - Field renaming (name of clause)
 * - Attribute support (type of ws-record-flag is attribute)
 * - Suppress empty/spaces fields (suppress when spaces)
 * - Character count tracking (count in)
 * - Exception handling (on exception / not on exception)
 */
public class XmlGenerator {

    private final XmlMapper xmlMapper;

    public XmlGenerator() {
        this.xmlMapper = createXmlMapper();
    }

    private XmlMapper createXmlMapper() {
        XmlMapper mapper = new XmlMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true);
        return mapper;
    }

    /**
     * Result class to hold both the XML output and character count
     * (equivalent to COBOL's ws-xml-output and ws-xml-char-count)
     */
    public static class XmlResult {
        private final String xmlOutput;
        private final int charCount;

        public XmlResult(String xmlOutput, int charCount) {
            this.xmlOutput = xmlOutput;
            this.charCount = charCount;
        }

        public String getXmlOutput() {
            return xmlOutput;
        }

        public int getCharCount() {
            return charCount;
        }
    }

    /**
     * Generates XML from a Record object.
     * 
     * Equivalent to COBOL: XML GENERATE ws-xml-output FROM ws-record
     * 
     * @param record The record to serialize
     * @return XmlResult containing the XML string and character count
     */
    public XmlResult generate(Record record) {
        try {
            Record recordToSerialize = suppressWhenSpaces(record);
            
            String xmlOutput = xmlMapper.writeValueAsString(recordToSerialize);
            int charCount = xmlOutput.length();
            
            System.out.println("XML document successfully generated.");
            
            return new XmlResult(xmlOutput, charCount);
            
        } catch (JsonProcessingException e) {
            System.out.println("Error generating xml error " + e.getMessage());
            throw new RuntimeException("XML generation failed", e);
        }
    }

    /**
     * Implements COBOL's "suppress when spaces" functionality.
     * Creates a copy of the record with space-only fields set to null.
     * 
     * @param original The original record
     * @return A new record with space-only fields nullified
     */
    private Record suppressWhenSpaces(Record original) {
        Record result = new Record();
        
        result.setName(isBlankOrSpaces(original.getName()) ? null : original.getName());
        result.setValue(isBlankOrSpaces(original.getValue()) ? null : original.getValue());
        result.setBlank(isBlankOrSpaces(original.getBlank()) ? null : original.getBlank());
        result.setEnabled(original.getEnabled());
        
        return result;
    }

    /**
     * Checks if a string is null, empty, or contains only spaces.
     * 
     * @param str The string to check
     * @return true if the string should be suppressed
     */
    private boolean isBlankOrSpaces(String str) {
        return str == null || str.trim().isEmpty();
    }
}
