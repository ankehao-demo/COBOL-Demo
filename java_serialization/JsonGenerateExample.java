package com.example.serialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.annotation.JsonInclude;

public class JsonGenerateExample {
    
    public static void main(String[] args) {
        try {
            Record record = new Record();
            record.setName("Test Name");
            record.setValue("Test Value");
            record.setBlank(" ");
            record.setEnabled(true);
            
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.disable(SerializationFeature.INDENT_OUTPUT);
            objectMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
            
            String jsonOutput = "{\"ws-record\":" + objectMapper.writeValueAsString(record) + "}";
            
            int charCount = jsonOutput.length();
            
            System.out.println("JSON document successfully generated.");
            System.out.println("Generated JSON for record: " + record.toString());
            System.out.println("----------------------------");
            System.out.println(jsonOutput);
            System.out.println("----------------------------");
            System.out.printf("JSON output character count: %04d%n", charCount);
            System.out.println("Done.");
            
        } catch (Exception e) {
            System.err.println("Error generating JSON error: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
