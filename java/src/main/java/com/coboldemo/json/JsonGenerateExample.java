package com.coboldemo.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * Migrated from json_generate/json_generate.cbl
 * Demonstrates JSON GENERATE using Jackson ObjectMapper.
 */
public class JsonGenerateExample {

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    static class Record {
        @JsonProperty("name")
        private String recordName;

        @JsonProperty("value")
        private String recordValue;

        @JsonProperty("blank")
        private String recordBlank;

        @JsonProperty("enabled")
        private String recordFlag;

        public Record() {}

        public String getRecordName() { return recordName; }
        public void setRecordName(String recordName) { this.recordName = recordName; }

        public String getRecordValue() { return recordValue; }
        public void setRecordValue(String recordValue) { this.recordValue = recordValue; }

        public String getRecordBlank() { return recordBlank; }
        public void setRecordBlank(String recordBlank) { this.recordBlank = recordBlank; }

        public String getRecordFlag() { return recordFlag; }
        public void setRecordFlag(String recordFlag) { this.recordFlag = recordFlag; }

        @Override
        public String toString() {
            return recordName + recordValue + recordBlank + recordFlag;
        }
    }

    public static void main(String[] args) {
        Record record = new Record();
        record.setRecordName("Test Name");
        record.setRecordValue("Test Value");
        record.setRecordBlank(""); // empty - will be suppressed by NON_EMPTY
        record.setRecordFlag("true");

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.INDENT_OUTPUT, false);

        try {
            String jsonOutput = mapper.writeValueAsString(record);

            System.out.println("JSON document successfully generated.");
            System.out.println("Generated JSON for record: " + record);
            System.out.println("----------------------------");
            System.out.println(jsonOutput);
            System.out.println("----------------------------");
            System.out.println("JSON output character count: " + jsonOutput.length());
            System.out.println("Done.");

        } catch (Exception e) {
            System.err.println("Error generating JSON: " + e.getMessage());
            System.exit(1);
        }
    }
}
