package com.cobol.serialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.util.Collections;
import java.util.Map;

/**
 * JSON Generator - Java equivalent of COBOL json_generate.cbl
 * 
 * Replicates the COBOL JSON GENERATE statement functionality:
 * <pre>
 * json generate ws-json-output
 *     from ws-record
 *     count in ws-json-char-count
 *     name of
 *         ws-record-name is "name",
 *         ws-record-value is "value",
 *         ws-record-flag is "enabled"
 *     on exception
 *         display "Error generating JSON error " JSON-CODE
 *         stop run
 *     not on exception
 *         display "JSON document successfully generated."
 * end-json
 * </pre>
 * 
 * Features implemented:
 * - Field renaming (name of clause) - handled via @JsonProperty annotations on Record class
 * - Character count tracking (count in)
 * - Exception handling (on exception / not on exception)
 */
public class JsonGenerator {

    private final ObjectMapper objectMapper;

    public JsonGenerator() {
        this.objectMapper = createObjectMapper();
    }

    private ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        return mapper;
    }

    /**
     * Result class to hold both the JSON output and character count
     * (equivalent to COBOL's ws-json-output and ws-json-char-count)
     */
    public static class JsonResult {
        private final String jsonOutput;
        private final int charCount;

        public JsonResult(String jsonOutput, int charCount) {
            this.jsonOutput = jsonOutput;
            this.charCount = charCount;
        }

        public String getJsonOutput() {
            return jsonOutput;
        }

        public int getCharCount() {
            return charCount;
        }
    }

    /**
     * Generates JSON from a Record object.
     * 
     * Equivalent to COBOL: JSON GENERATE ws-json-output FROM ws-record
     * 
     * @param record The record to serialize
     * @return JsonResult containing the JSON string and character count
     */
    public JsonResult generate(Record record) {
        try {
            Map<String, Record> wrapper = Collections.singletonMap("ws-record", record);
            String jsonOutput = objectMapper.writeValueAsString(wrapper);
            int charCount = jsonOutput.length();
            
            System.out.println("JSON document successfully generated.");
            
            return new JsonResult(jsonOutput, charCount);
            
        } catch (JsonProcessingException e) {
            System.out.println("Error generating JSON error " + e.getMessage());
            throw new RuntimeException("JSON generation failed", e);
        }
    }
}
