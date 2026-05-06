package com.example.cobol.jsongenerate;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Java port of {@code json_generate/json_generate.cbl}.
 *
 * <p>The original program serializes a COBOL record to JSON via
 * {@code JSON GENERATE}. The closest Java analogue is using Jackson's
 * {@link ObjectMapper} to write a small POJO. Field renaming
 * ({@code NAME OF foo IS "bar"}) becomes {@link JsonProperty} annotations.
 */
public final class JsonGenerateExample {

    @JsonPropertyOrder({"name", "value", "wsRecordBlank", "enabled"})
    public static final class Record {
        @JsonProperty("name")
        public String name;

        @JsonProperty("value")
        public String value;

        // The original record had a "blank" field that the COBOL example
        // didn't rename. We preserve it here as wsRecordBlank to make the
        // mapping clear.
        @JsonProperty("wsRecordBlank")
        public String wsRecordBlank = "";

        @JsonProperty("enabled")
        public String enabled;
    }

    private JsonGenerateExample() {}

    public static void main(String[] args) throws Exception {
        Record r = new Record();
        r.name = "Test Name";
        r.value = "Test Value";
        r.enabled = "true";

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(r);

        System.out.println("Generated JSON for record: name=" + r.name
                + ", value=" + r.value + ", enabled=" + r.enabled);
        System.out.println("----------------------------");
        System.out.println(json);
        System.out.println("----------------------------");
        System.out.println("JSON output character count: " + json.length());
        System.out.println("Done.");
    }
}
