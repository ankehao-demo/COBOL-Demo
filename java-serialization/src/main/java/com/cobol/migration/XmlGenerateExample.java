package com.cobol.migration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;

public class XmlGenerateExample {
    
    public static void main(String[] args) {
        try {
            String name = "Test Name";
            String value = "Test Value";
            String blank = "";
            String enabled = "true";
            
            Record record = new Record(name, value, blank, enabled);
            
            XmlMapper xmlMapper = new XmlMapper();
            xmlMapper.disable(SerializationFeature.INDENT_OUTPUT);
            xmlMapper.enable(ToXmlGenerator.Feature.WRITE_XML_DECLARATION);
            xmlMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
            
            String xmlOutput = xmlMapper.writeValueAsString(record);
            
            xmlOutput = xmlOutput.replace("<?xml version='1.0' encoding='UTF-8'?>", "<?xml version=\"1.0\"?>");
            
            int xmlCharCount = xmlOutput.length();
            
            System.out.println("XML document successfully generated.");
            System.out.println("Generated xml for record: " + 
                padRight(name, 10) + " " + 
                padRight(value, 10) + " " + 
                padRight(blank, 10) + " " + 
                enabled + " ");
            System.out.println("----------------------------");
            System.out.println(xmlOutput);
            System.out.println("----------------------------");
            System.out.println("XML output character count: " + String.format("%04d", xmlCharCount));
            System.out.println("Done.");
            
        } catch (Exception e) {
            System.err.println("Error generating xml error: " + e.getMessage());
            System.exit(1);
        }
    }
    
    private static String padRight(String s, int n) {
        if (s == null) {
            s = "";
        }
        if (s.length() >= n) {
            return s.substring(0, n);
        }
        return String.format("%-" + n + "s", s);
    }
}
