package com.coboldemo.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Java port of {@code json_generate/json_generate.cbl}.
 *
 * Builds a small record and serialises it to JSON, mirroring the COBOL
 * {@code JSON GENERATE} behaviour. Empty fields are suppressed
 * (analogue of {@code SUPPRESS WHEN SPACES}) and the boolean is rendered
 * as {@code "enabled": "true"} to match the COBOL output where the 88-level
 * flag's external value is the string "true".
 */
public class JsonGenerateExample {

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonPropertyOrder({"name", "value", "blank", "enabled"})
    public static final class Record {
        @JsonProperty("name")
        public String name;

        @JsonProperty("value")
        public String value;

        @JsonProperty("blank")
        public String blank;

        @JsonProperty("enabled")
        public String enabled;
    }

    public static void main(String[] args) {
        Record record = new Record();
        record.name = "Test Name";
        record.value = "Test Value";
        // blank is intentionally left null to be suppressed.
        record.enabled = "true";

        ObjectMapper mapper = new ObjectMapper();
        String json;
        try {
            json = mapper.writeValueAsString(record);
            System.out.println("JSON document successfully generated.");
        } catch (JsonProcessingException e) {
            System.out.println("Error generating JSON error " + e.getMessage());
            return;
        }

        System.out.println("Generated JSON for record: "
                + safe(record.name) + safe(record.value) + safe(record.blank) + safe(record.enabled));
        System.out.println("----------------------------");
        System.out.println(json);
        System.out.println("----------------------------");
        System.out.println("JSON output character count: " + json.length());
        System.out.println("Done.");
    }

    private static String safe(String s) {
        return s == null ? "" : s;
    }
}
