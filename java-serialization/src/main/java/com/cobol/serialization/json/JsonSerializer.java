package com.cobol.serialization.json;

import com.cobol.serialization.model.Record;
import com.cobol.serialization.util.SerializationResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * JSON serializer that replicates COBOL JSON GENERATE functionality.
 * 
 * Supports the following COBOL features:
 * - Field name mapping (NAME OF clause) via @JsonProperty annotations
 * - Character count tracking (COUNT IN)
 * - Error handling (JSON-CODE)
 */
public class JsonSerializer {

    public static final int ERROR_SERIALIZATION = 1;
    public static final int ERROR_NULL_INPUT = 2;

    private final ObjectMapper objectMapper;
    private boolean prettyPrint = false;

    public JsonSerializer() {
        this.objectMapper = new ObjectMapper();
    }

    public JsonSerializer withPrettyPrint(boolean prettyPrint) {
        this.prettyPrint = prettyPrint;
        if (prettyPrint) {
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        } else {
            objectMapper.disable(SerializationFeature.INDENT_OUTPUT);
        }
        return this;
    }

    public SerializationResult serialize(Record record) {
        if (record == null) {
            return SerializationResult.error(ERROR_NULL_INPUT, "Input record is null");
        }

        try {
            String output = objectMapper.writeValueAsString(record);
            return SerializationResult.success(output);

        } catch (JsonProcessingException e) {
            return SerializationResult.error(ERROR_SERIALIZATION, 
                    "Error generating JSON: " + e.getMessage());
        }
    }

    public <T> SerializationResult serializeObject(T object) {
        if (object == null) {
            return SerializationResult.error(ERROR_NULL_INPUT, "Input object is null");
        }

        try {
            String output = objectMapper.writeValueAsString(object);
            return SerializationResult.success(output);

        } catch (JsonProcessingException e) {
            return SerializationResult.error(ERROR_SERIALIZATION, 
                    "Error generating JSON: " + e.getMessage());
        }
    }

    public boolean isPrettyPrint() {
        return prettyPrint;
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }
}
