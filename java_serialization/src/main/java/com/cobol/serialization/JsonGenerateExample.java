package com.cobol.serialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * Java equivalent of json_generate/json_generate.cbl
 * 
 * This class demonstrates JSON serialization using Jackson,
 * replicating the COBOL JSON GENERATE command functionality.
 * 
 * COBOL features replicated:
 * - JSON GENERATE command -> ObjectMapper.writeValueAsString()
 * - NAME OF clauses -> @JsonProperty annotations on Record class
 * - COUNT IN -> String.length() to track character count
 * - ON EXCEPTION / NOT ON EXCEPTION -> try-catch blocks
 */
public class JsonGenerateExample {

    public static void main(String[] args) {
        Record record = new Record();
        record.setName("Test Name");
        record.setValue("Test Value");
        record.setEnabledFlag(true);

        RecordWrapper wrapper = new RecordWrapper(record);

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            String jsonOutput = objectMapper.writeValueAsString(wrapper);
            int jsonCharCount = jsonOutput.length();

            System.out.println("JSON document successfully generated.");
            System.out.println("Generated JSON for record: " + record);
            System.out.println("----------------------------");
            System.out.println(jsonOutput.trim());
            System.out.println("----------------------------");
            System.out.println("JSON output character count: " + jsonCharCount);
            System.out.println("Done.");

        } catch (JsonProcessingException e) {
            System.out.println("Error generating JSON error " + e.getMessage());
            System.exit(1);
        }
    }
}
