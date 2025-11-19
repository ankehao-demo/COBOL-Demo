package com.example.cobol;

import com.example.cobol.model.Record;
import com.example.cobol.util.JsonSerializer;

public class JsonGenerateExample {
    
    public static void main(String[] args) {
        Record record = new Record("Test Name", "Test Value", true);
        
        try {
            String json = JsonSerializer.serialize(record);
            
            System.out.println("JSON document successfully generated.");
            System.out.println("Generated JSON for record: " + record);
            System.out.println("----------------------------");
            System.out.println(json.trim());
            System.out.println("----------------------------");
            System.out.println("JSON output character count: " + json.length());
            System.out.println("Done.");
            
        } catch (Exception e) {
            System.err.println("Error generating JSON error " + e.getMessage());
            System.exit(1);
        }
    }
}
