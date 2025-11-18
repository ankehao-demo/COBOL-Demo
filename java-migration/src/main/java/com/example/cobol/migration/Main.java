package com.example.cobol.migration;

import com.example.cobol.migration.model.Record;
import com.example.cobol.migration.serializer.JsonSerializer;
import com.example.cobol.migration.serializer.XmlSerializer;

public class Main {
    
    public static void main(String[] args) {
        Record record = new Record();
        record.setName("Test Name");
        record.setValue("Test Value");
        record.setBlank("");
        record.setEnabledFlag(true);
        
        System.out.println("=== XML Serialization ===");
        XmlSerializer xmlSerializer = new XmlSerializer();
        XmlSerializer.SerializationResult xmlResult = xmlSerializer.serialize(record);
        
        if (xmlResult.isSuccess()) {
            System.out.println("XML document successfully generated.");
            System.out.println("Generated xml for record: " + record);
            System.out.println("----------------------------");
            System.out.println(xmlResult.getOutput());
            System.out.println("----------------------------");
            System.out.println("XML output character count: " + xmlResult.getCharCount());
        } else {
            System.err.println("Error generating xml: " + xmlResult.getErrorMessage());
            System.exit(1);
        }
        
        System.out.println("\n=== JSON Serialization ===");
        JsonSerializer jsonSerializer = new JsonSerializer();
        JsonSerializer.SerializationResult jsonResult = jsonSerializer.serialize(record);
        
        if (jsonResult.isSuccess()) {
            System.out.println("JSON document successfully generated.");
            System.out.println("Generated JSON for record: " + record);
            System.out.println("----------------------------");
            System.out.println(jsonResult.getOutput());
            System.out.println("----------------------------");
            System.out.println("JSON output character count: " + jsonResult.getCharCount());
        } else {
            System.err.println("Error generating JSON: " + jsonResult.getErrorMessage());
            System.exit(1);
        }
        
        System.out.println("\nDone.");
    }
}
