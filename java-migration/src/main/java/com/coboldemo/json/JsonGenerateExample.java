package com.coboldemo.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Java port of json_generate/json_generate.cbl.
 *
 * Uses Jackson's {@link ObjectMapper} in lieu of COBOL's {@code JSON GENERATE}
 * statement. {@code SUPPRESS WHEN SPACES} is implemented via
 * {@link JsonInclude} on the blank field.
 */
public final class JsonGenerateExample {

    private JsonGenerateExample() {
    }

    public static void main(String[] args) throws Exception {
        Record record = new Record();
        record.setName("Test Name");
        record.setValue("Test Value");
        // ws-record-blank intentionally left empty to demonstrate SUPPRESS.
        record.setBlank("");
        record.setEnabled("true");

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(record);

        System.out.println("JSON document successfully generated.");
        System.out.println("Generated JSON for record: " + record);
        System.out.println("----------------------------");
        System.out.println(json);
        System.out.println("----------------------------");
        System.out.println("JSON output character count: " + json.length());
        System.out.println("Done.");
    }

    /**
     * Mirrors ws-record in the original program. Jackson property order is
     * pinned to match the COBOL generator so the rendered JSON lines up with
     * the reference output.
     */
    @JsonPropertyOrder({"name", "value", "ws-record-blank", "enabled"})
    public static final class Record {

        @JsonProperty("name")
        private String name;

        @JsonProperty("value")
        private String value;

        // SUPPRESS WHEN SPACES -> omit when the value is an empty string.
        @JsonInclude(Include.NON_EMPTY)
        @JsonProperty("ws-record-blank")
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

        @Override
        public String toString() {
            return "Record{name='" + name + "', value='" + value
                    + "', blank='" + blank + "', enabled='" + enabled + "'}";
        }
    }
}
