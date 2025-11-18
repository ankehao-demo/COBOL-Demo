package com.example.serialization;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import com.fasterxml.jackson.annotation.JsonInclude;

public class XmlGenerateExample {
    
    public static void main(String[] args) {
        try {
            Record record = new Record();
            record.setName("Test Name");
            record.setValue("Test Value");
            record.setEnabled(true);
            
            XmlMapper xmlMapper = new XmlMapper();
            xmlMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
            xmlMapper.disable(SerializationFeature.INDENT_OUTPUT);
            
            String xmlBody = xmlMapper.writeValueAsString(record);
            
            String xmlOutput = "<?xml version=\"1.0\"?>\n" + xmlBody;
            
            int charCount = xmlOutput.length();
            
            System.out.println("XML document successfully generated.");
            System.out.println("Generated xml for record: " + record.toString());
            System.out.println("----------------------------");
            System.out.println(xmlOutput);
            System.out.println("----------------------------");
            System.out.printf("XML output character count: %04d%n", charCount);
            System.out.println("Done.");
            
        } catch (Exception e) {
            System.err.println("Error generating xml error: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
