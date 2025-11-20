package com.cobol.serialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;

public class SerializationService {
    
    private final ObjectMapper jsonMapper;
    private final XmlMapper xmlMapper;
    
    public SerializationService() {
        this.jsonMapper = new ObjectMapper();
        
        this.xmlMapper = new XmlMapper();
        this.xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
        this.xmlMapper.configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true);
    }
    
    public String generateJson(Record record) throws JsonProcessingException {
        try {
            String json = jsonMapper.writeValueAsString(record);
            System.out.println("JSON document successfully generated.");
            return json;
        } catch (JsonProcessingException e) {
            System.err.println("Error generating JSON: " + e.getMessage());
            throw e;
        }
    }
    
    public int getJsonCharacterCount(String json) {
        return json != null ? json.length() : 0;
    }
    
    public String generateXml(Record record) throws JsonProcessingException {
        try {
            String xml = xmlMapper.writeValueAsString(record);
            System.out.println("XML document successfully generated.");
            return xml;
        } catch (JsonProcessingException e) {
            System.err.println("Error generating XML: " + e.getMessage());
            throw e;
        }
    }
    
    public int getXmlCharacterCount(String xml) {
        return xml != null ? xml.length() : 0;
    }
}
