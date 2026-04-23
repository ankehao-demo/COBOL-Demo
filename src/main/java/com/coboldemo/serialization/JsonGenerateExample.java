package com.coboldemo.serialization;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Migrated from: json_generate/json_generate.cbl
 *
 * Demonstrates COBOL JSON GENERATE command. Maps to Jackson
 * ObjectMapper.writeValueAsString() with @JsonProperty annotations
 * for field name mapping.
 */
public class JsonGenerateExample {

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public static class Record {
        @JsonProperty("name")
        private String recordName;

        @JsonProperty("value")
        private String recordValue;

        @JsonProperty("ws-record-blank")
        private String recordBlank;

        @JsonProperty("enabled")
        private String recordFlag;

        public Record() {}

        public Record(String name, String value, String blank, String flag) {
            this.recordName = name;
            this.recordValue = value;
            this.recordBlank = blank;
            this.recordFlag = flag;
        }

        public String getRecordName() { return recordName; }
        public void setRecordName(String recordName) { this.recordName = recordName; }
        public String getRecordValue() { return recordValue; }
        public void setRecordValue(String recordValue) { this.recordValue = recordValue; }
        public String getRecordBlank() { return recordBlank; }
        public void setRecordBlank(String recordBlank) { this.recordBlank = recordBlank; }
        public String getRecordFlag() { return recordFlag; }
        public void setRecordFlag(String recordFlag) { this.recordFlag = recordFlag; }
    }

    public static void main(String[] args) {
        Record record = new Record("Test Name", "Test Value", "", "true");

        ObjectMapper mapper = new ObjectMapper();

        try {
            String jsonOutput = mapper.writeValueAsString(record);
            int charCount = jsonOutput.length();

            System.out.println("JSON document successfully generated.");
            System.out.println("Generated JSON for record: " + record.getRecordName()
                    + record.getRecordValue() + record.getRecordBlank() + record.getRecordFlag());
            System.out.println("----------------------------");
            System.out.println(jsonOutput);
            System.out.println("----------------------------");
            System.out.println("JSON output character count: " + charCount);
            System.out.println("Done.");
        } catch (JsonProcessingException e) {
            System.out.println("Error generating JSON: " + e.getMessage());
        }
    }
}
