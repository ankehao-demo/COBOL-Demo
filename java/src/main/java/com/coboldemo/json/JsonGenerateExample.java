package com.coboldemo.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * Migrated from: json_generate/json_generate.cbl
 * Original author: Erik Eriksen (2022-06-15)
 * Purpose: Demonstrates JSON GENERATE command equivalent using Jackson.
 *
 * Mapping:
 * - JSON GENERATE -> ObjectMapper.writeValueAsString()
 * - Field name mapping -> @JsonProperty annotations
 * - SUPPRESS WHEN SPACES -> @JsonInclude(Include.NON_EMPTY)
 */
public class JsonGenerateExample {

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    static class Record {
        @JsonProperty("Name")
        private String name;

        @JsonProperty("Value")
        private int value;

        @JsonProperty("Blank")
        private String blank;

        @JsonProperty("Enabled")
        private String enabled;

        public Record(String name, int value, String blank, String enabled) {
            this.name = name;
            this.value = value;
            this.blank = blank;
            this.enabled = enabled;
        }

        // Getters required for Jackson serialization
        public String getName() { return name; }
        public int getValue() { return value; }
        public String getBlank() { return blank; }
        public String getEnabled() { return enabled; }
    }

    public static void main(String[] args) throws JsonProcessingException {
        System.out.println("JSON GENERATE Example");
        System.out.println("=====================");
        System.out.println();

        // Create record matching COBOL data
        Record record = new Record("Test Record", 42, "", "Y");

        // Display record fields
        System.out.println("Record fields:");
        System.out.println("  Name:    '" + record.getName() + "'");
        System.out.println("  Value:   " + record.getValue());
        System.out.println("  Blank:   '" + record.getBlank() + "'");
        System.out.println("  Enabled: '" + record.getEnabled() + "'");
        System.out.println();

        // Generate JSON (pretty printed)
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        String json = mapper.writeValueAsString(record);
        System.out.println("Generated JSON:");
        System.out.println(json);
        System.out.println();

        // JSON-CODE equivalent (0 = success)
        System.out.println("JSON-CODE: 0 (success)");

        // Also show compact JSON
        ObjectMapper compactMapper = new ObjectMapper();
        String compactJson = compactMapper.writeValueAsString(record);
        System.out.println();
        System.out.println("Compact JSON:");
        System.out.println(compactJson);
        System.out.println("Length: " + compactJson.length());
    }
}
