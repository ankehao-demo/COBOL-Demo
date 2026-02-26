package com.cobol.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

/**
 * Serializes a Record to JSON, replicating the behavior of
 * json_generate/json_generate.cbl.
 *
 * Key behaviors from COBOL:
 * - Field name mappings via NAME OF clause:
 *     ws-record-name  -> "name"
 *     ws-record-value -> "value"
 *     ws-record-flag  -> "enabled"
 *     ws-record-blank -> "ws-record-blank" (not renamed)
 * - ws-record-blank is always included (even if blank)
 * - enabled is serialized as a string "true"/"false" (COBOL PIC X(5))
 * - Output is wrapped in a "ws-record" root object
 * - Error handling via try/catch mirrors COBOL's ON EXCEPTION / JSON-CODE
 * - Character count tracked via String.length() (equivalent to COUNT IN ws-json-char-count)
 */
public class JsonSerializer {

    private final ObjectMapper objectMapper;

    public JsonSerializer() {
        this.objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(Record.class, new RecordJsonStdSerializer());
        this.objectMapper.registerModule(module);
    }

    /**
     * Serializes the given Record to JSON and prints the output and character count.
     *
     * @param record the Record to serialize
     */
    public void serialize(Record record) {
        try {
            String jsonOutput = objectMapper.writeValueAsString(record);

            int charCount = jsonOutput.length();

            System.out.println("JSON document successfully generated.");
            System.out.println("Generated JSON for record: " + record.getName()
                    + record.getValue()
                    + (record.getBlank() != null ? record.getBlank() : "")
                    + (record.isEnabled() ? "true" : "false"));
            System.out.println("----------------------------");
            System.out.println(jsonOutput);
            System.out.println("----------------------------");
            System.out.println("JSON output character count: " + charCount);
            System.out.println("Done.");
        } catch (JsonProcessingException e) {
            System.out.println("Error generating JSON error " + e.getMessage());
        }
    }

    /**
     * Custom Jackson serializer for Record that produces the exact JSON format
     * matching the COBOL JSON GENERATE output.
     */
    private static class RecordJsonStdSerializer extends StdSerializer<Record> {

        RecordJsonStdSerializer() {
            super(Record.class);
        }

        @Override
        public void serialize(Record record, JsonGenerator gen, SerializerProvider provider)
                throws IOException {
            gen.writeStartObject();
            gen.writeFieldName("ws-record");
            gen.writeStartObject();
            gen.writeStringField("name", record.getName());
            gen.writeStringField("value", record.getValue());
            gen.writeStringField("ws-record-blank",
                    record.getBlank() != null ? record.getBlank() : "");
            gen.writeStringField("enabled", record.isEnabled() ? "true" : "false");
            gen.writeEndObject();
            gen.writeEndObject();
        }
    }
}
