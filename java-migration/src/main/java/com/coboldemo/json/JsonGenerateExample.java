package com.coboldemo.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Port of {@code json_generate/json_generate.cbl} — serializes a record to
 * JSON. The COBOL program used {@code JSON GENERATE ... NAME OF ... IS
 * "name"} to rename fields; here the {@link JsonProperty} annotation does
 * the same job.
 */
public final class JsonGenerateExample {

    private JsonGenerateExample() {
    }

    /**
     * Record that mirrors {@code ws-record} in the original COBOL source.
     * {@link JsonInclude} omits blank fields, matching
     * {@code SUPPRESS WHEN SPACES} on the COBOL side.
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public static final class Record {
        @JsonProperty("name")
        private String name;

        @JsonProperty("value")
        private String value;

        @JsonProperty("blank")
        private String blank;

        @JsonProperty("enabled")
        private String enabled;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getBlank() {
            return blank;
        }

        public void setBlank(String blank) {
            this.blank = blank;
        }

        public String getEnabled() {
            return enabled;
        }

        public void setEnabled(String enabled) {
            this.enabled = enabled;
        }
    }

    public static void main(String[] args) throws JsonProcessingException {
        Record record = new Record();
        record.setName("Test Name");
        record.setValue("Test Value");
        record.setEnabled("true");

        ObjectMapper mapper = new ObjectMapper();
        String json;
        try {
            json = mapper.writeValueAsString(record);
            System.out.println("JSON document successfully generated.");
        } catch (JsonProcessingException e) {
            System.out.println("Error generating JSON: " + e.getMessage());
            throw e;
        }

        System.out.println("Generated JSON for record: " + record.getName() + "/" + record.getValue()
                + "/" + record.getEnabled());
        System.out.println("----------------------------");
        System.out.println(json);
        System.out.println("----------------------------");
        System.out.println("JSON output character count: " + json.length());
        System.out.println("Done.");
    }
}
