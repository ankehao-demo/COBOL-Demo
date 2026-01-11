package com.cobol.serialization.serializer;

import com.cobol.serialization.exception.JsonSerializationException;
import com.cobol.serialization.model.WsRecord;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * JSON serializer that replicates COBOL's JSON GENERATE functionality.
 * 
 * COBOL equivalent:
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
 * - Field name mapping (NAME OF clause)
 * - Character count tracking (COUNT IN clause)
 * - Error handling with JSON-CODE equivalent
 * - Root element wrapping with record name
 */
public class JsonSerializer {
    
    private static final String ROOT_ELEMENT_NAME = "ws-record";
    private static final String FIELD_NAME_NAME = "name";
    private static final String FIELD_NAME_VALUE = "value";
    private static final String FIELD_NAME_BLANK = "ws-record-blank";
    private static final String FIELD_NAME_FLAG = "enabled";
    
    private final ObjectMapper objectMapper;
    private int lastCharCount;
    
    public JsonSerializer() {
        this.objectMapper = new ObjectMapper();
        this.lastCharCount = 0;
    }
    
    /**
     * Generates JSON from a WsRecord, replicating COBOL's JSON GENERATE behavior.
     * 
     * @param record The WsRecord to serialize
     * @return The generated JSON string
     * @throws JsonSerializationException if serialization fails
     */
    public String generate(WsRecord record) throws JsonSerializationException {
        if (record == null) {
            throw new JsonSerializationException(
                "Cannot generate JSON from null record",
                JsonSerializationException.ERROR_INVALID_DATA
            );
        }
        
        try {
            ObjectNode rootNode = objectMapper.createObjectNode();
            ObjectNode recordNode = objectMapper.createObjectNode();
            
            recordNode.put(FIELD_NAME_NAME, record.getName());
            recordNode.put(FIELD_NAME_VALUE, record.getValue());
            recordNode.put(FIELD_NAME_BLANK, formatBlankField(record.getBlank()));
            recordNode.put(FIELD_NAME_FLAG, record.getFlag());
            
            rootNode.set(ROOT_ELEMENT_NAME, recordNode);
            
            String json = objectMapper.writeValueAsString(rootNode);
            this.lastCharCount = json.length();
            
            return json;
        } catch (JsonProcessingException e) {
            throw new JsonSerializationException(
                "Error generating JSON: " + e.getMessage(),
                JsonSerializationException.ERROR_INTERNAL,
                e
            );
        }
    }
    
    /**
     * Generates JSON with blank field suppression.
     * When suppressBlanks is true, fields that are empty or contain only spaces are omitted.
     * 
     * @param record The WsRecord to serialize
     * @param suppressBlanks If true, suppress blank fields
     * @return The generated JSON string
     * @throws JsonSerializationException if serialization fails
     */
    public String generate(WsRecord record, boolean suppressBlanks) throws JsonSerializationException {
        if (record == null) {
            throw new JsonSerializationException(
                "Cannot generate JSON from null record",
                JsonSerializationException.ERROR_INVALID_DATA
            );
        }
        
        try {
            ObjectNode rootNode = objectMapper.createObjectNode();
            ObjectNode recordNode = objectMapper.createObjectNode();
            
            recordNode.put(FIELD_NAME_NAME, record.getName());
            recordNode.put(FIELD_NAME_VALUE, record.getValue());
            
            if (!suppressBlanks || !record.isBlankEmpty()) {
                recordNode.put(FIELD_NAME_BLANK, formatBlankField(record.getBlank()));
            }
            
            recordNode.put(FIELD_NAME_FLAG, record.getFlag());
            
            rootNode.set(ROOT_ELEMENT_NAME, recordNode);
            
            String json = objectMapper.writeValueAsString(rootNode);
            this.lastCharCount = json.length();
            
            return json;
        } catch (JsonProcessingException e) {
            throw new JsonSerializationException(
                "Error generating JSON: " + e.getMessage(),
                JsonSerializationException.ERROR_INTERNAL,
                e
            );
        }
    }
    
    /**
     * Returns the character count of the last generated JSON.
     * Equivalent to COBOL's COUNT IN clause.
     * 
     * @return The character count
     */
    public int getCharCount() {
        return lastCharCount;
    }
    
    /**
     * Formats the blank field to match COBOL behavior.
     * COBOL PIC X(10) fields are space-padded, so an empty field becomes a single space.
     */
    private String formatBlankField(String blank) {
        if (blank == null || blank.isEmpty()) {
            return " ";
        }
        return blank;
    }
}
