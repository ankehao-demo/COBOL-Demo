package com.example.cobol;

import com.example.cobol.model.Record;
import com.example.cobol.util.XmlSerializer;

import javax.xml.bind.JAXBException;

public class XmlGenerateExample {
    
    public static void main(String[] args) {
        Record record = new Record("Test Name", "Test Value", true);
        
        try {
            String xml = XmlSerializer.serializeWithDeclaration(record);
            
            System.out.println("XML document successfully generated.");
            System.out.println("Generated xml for record: " + record);
            System.out.println("----------------------------");
            System.out.println(xml.trim());
            System.out.println("----------------------------");
            System.out.println("XML output character count: " + xml.length());
            System.out.println("Done.");
            
        } catch (JAXBException e) {
            System.err.println("Error generating xml error " + e.getMessage());
            System.exit(1);
        }
    }
}
