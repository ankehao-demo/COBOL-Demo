package com.example.cobol.migration.serializer;

import com.example.cobol.migration.model.Record;
import com.example.cobol.migration.util.CobolDataConverter;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;

public class XmlSerializer {
    
    private final XmlMapper xmlMapper;
    
    public XmlSerializer() {
        this.xmlMapper = new XmlMapper();
        this.xmlMapper.configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true);
    }
    
    public SerializationResult serialize(Record record) {
        try {
            Record normalizedRecord = normalizeRecord(record);
            
            String xmlOutput = xmlMapper.writeValueAsString(normalizedRecord);
            
            int charCount = xmlOutput.length();
            
            return new SerializationResult(xmlOutput, charCount, true, null);
            
        } catch (Exception e) {
            String errorCode = "XML-ERROR-" + e.getClass().getSimpleName();
            return new SerializationResult(null, 0, false, errorCode + ": " + e.getMessage());
        }
    }
    
    private Record normalizeRecord(Record record) {
        Record normalized = new Record();
        normalized.setName(CobolDataConverter.normalizeForSerialization(record.getName()));
        normalized.setValue(CobolDataConverter.normalizeForSerialization(record.getValue()));
        normalized.setBlank(CobolDataConverter.normalizeForSerialization(record.getBlank()));
        normalized.setEnabled(record.getEnabled());
        return normalized;
    }
    
    public static class SerializationResult {
        private final String output;
        private final int charCount;
        private final boolean success;
        private final String errorMessage;
        
        public SerializationResult(String output, int charCount, boolean success, String errorMessage) {
            this.output = output;
            this.charCount = charCount;
            this.success = success;
            this.errorMessage = errorMessage;
        }
        
        public String getOutput() {
            return output;
        }
        
        public int getCharCount() {
            return charCount;
        }
        
        public boolean isSuccess() {
            return success;
        }
        
        public String getErrorMessage() {
            return errorMessage;
        }
    }
}
