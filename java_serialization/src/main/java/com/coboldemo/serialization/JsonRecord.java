package com.coboldemo.serialization;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * JSON-specific representation of the COBOL ws-record.
 *
 * In COBOL, ws-record-flag is a PIC X(5) string containing "true" or "false",
 * so the JSON output serializes it as a string (e.g., "enabled":"true"),
 * not as a JSON boolean.
 *
 * Expected JSON output:
 *   {"ws-record":{"name":"Test Name","value":"Test Value","ws-record-blank":" ","enabled":"true"}}
 */
@JsonRootName("ws-record")
@JsonPropertyOrder({"name", "value", "ws-record-blank", "enabled"})
public class JsonRecord {

    @JsonProperty("name")
    private String name;

    @JsonProperty("value")
    private String value;

    @JsonProperty("ws-record-blank")
    private String blank;

    @JsonProperty("enabled")
    private String enabled;

    public JsonRecord() {
    }

    /**
     * Creates a JsonRecord from a Record, converting the boolean enabled
     * field to its COBOL string representation ("true"/"false").
     *
     * @param record the source Record
     * @return a JsonRecord with enabled as a string
     */
    public static JsonRecord fromRecord(Record record) {
        JsonRecord jsonRecord = new JsonRecord();
        jsonRecord.setName(record.getName());
        jsonRecord.setValue(record.getValue());
        jsonRecord.setBlank(record.getBlank());
        // COBOL stores ws-record-flag as PIC X(5) string "true"/"false"
        jsonRecord.setEnabled(String.valueOf(record.isEnabled()));
        return jsonRecord;
    }

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
