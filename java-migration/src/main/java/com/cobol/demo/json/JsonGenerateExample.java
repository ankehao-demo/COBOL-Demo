package com.cobol.demo.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JsonGenerateExample {
    public static void main(String[] args) {
        try {
            Record record = new Record();
            record.setName("Test Name");
            record.setValue("Test Value");
            record.setEnabled(true);

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

            String jsonOutput = objectMapper.writeValueAsString(record);
            int jsonCharCount = jsonOutput.length();

            System.out.println("JSON document successfully generated.");
            System.out.println("Generated JSON for record: " + record.getName() + " " + record.getValue());
            System.out.println("----------------------------");
            System.out.println(jsonOutput.trim());
            System.out.println("----------------------------");
            System.out.println("JSON output character count: " + jsonCharCount);
            System.out.println("Done.");

        } catch (Exception e) {
            System.err.println("Error generating JSON: " + e.getMessage());
            System.exit(1);
        }
    }
}
