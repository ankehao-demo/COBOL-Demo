package com.coboldemo.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Java equivalent of json_generate/json_generate.cbl
 *
 * Demonstrates the COBOL JSON GENERATE command using Jackson ObjectMapper.
 *
 * COBOL Mapping:
 *   JSON GENERATE ws-json-output FROM ws-record   → ObjectMapper.writeValueAsString()
 *   NAME OF ws-record-name IS "name"              → @JsonProperty("name")
 *   COUNT IN ws-json-char-count                   → jsonString.length()
 *   ON EXCEPTION ... JSON-CODE                    → try-catch(JsonProcessingException)
 */
public class JsonGenerateExample {

    /** Record POJO with @JsonProperty annotations for field name mapping. */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    static class Record {
        @JsonProperty("name")
        String recordName;

        @JsonProperty("value")
        String recordValue;

        @JsonProperty("ws-record-blank")
        String recordBlank;

        @JsonProperty("enabled")
        String recordFlag;
    }

    public static void main(String[] args) {
        Record record = new Record();
        record.recordName = "Test Name";
        record.recordValue = "Test Value";
        record.recordBlank = "";  // Will be suppressed by @JsonInclude.NON_EMPTY
        record.recordFlag = "true";

        ObjectMapper mapper = new ObjectMapper();

        try {
            // JSON GENERATE ws-json-output FROM ws-record
            String jsonOutput = mapper.writeValueAsString(record);

            // NOT ON EXCEPTION
            System.out.println("JSON document successfully generated.");

            // Display results
            String recordDisplay = record.recordName + record.recordValue
                    + record.recordBlank + record.recordFlag;
            System.out.println("Generated JSON for record: " + recordDisplay);
            System.out.println("----------------------------");
            System.out.println(jsonOutput);
            System.out.println("----------------------------");

            // COUNT IN ws-json-char-count
            System.out.println("JSON output character count: " + jsonOutput.length());
            System.out.println("Done.");

        } catch (JsonProcessingException e) {
            // ON EXCEPTION
            System.out.println("Error generating JSON error " + e.getMessage());
        }
    }
}
