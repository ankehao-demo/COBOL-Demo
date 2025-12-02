package com.cobol.serialization.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Java equivalent of the COBOL ws-record structure from xml_generate.cbl and json_generate.cbl.
 * 
 * COBOL structure:
 * 01  ws-record.
 *     05  ws-record-name                  pic x(10).
 *     05  ws-record-value                 pic x(10).
 *     05  ws-record-blank                 pic x(10).
 *     05  ws-record-flag                  pic x(5) value "false".
 *         88  ws-record-flag-enabled      value "true".
 *         88  ws-record-flag-disabled     value "false".
 */
@XmlRootElement(name = "ws-record")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Record {

    private static final int NAME_LENGTH = 10;
    private static final int VALUE_LENGTH = 10;
    private static final int BLANK_LENGTH = 10;
    private static final int FLAG_LENGTH = 5;

    @XmlElement(name = "name")
    @JsonProperty("name")
    private String name;

    @XmlElement(name = "value")
    @JsonProperty("value")
    private String value;

    @XmlElement(name = "ws-record-blank")
    @JsonProperty("ws-record-blank")
    private String blank;

    @XmlAttribute(name = "enabled")
    @JsonProperty("enabled")
    private String enabled;

    public Record() {
        this.enabled = "false";
    }

    public Record(String name, String value, String blank, boolean enabled) {
        this.name = padOrTruncate(name, NAME_LENGTH);
        this.value = padOrTruncate(value, VALUE_LENGTH);
        this.blank = padOrTruncate(blank, BLANK_LENGTH);
        this.enabled = enabled ? "true" : "false";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = padOrTruncate(name, NAME_LENGTH);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = padOrTruncate(value, VALUE_LENGTH);
    }

    public String getBlank() {
        return blank;
    }

    public void setBlank(String blank) {
        this.blank = padOrTruncate(blank, BLANK_LENGTH);
    }

    public String getEnabled() {
        return enabled;
    }

    public void setEnabled(String enabled) {
        this.enabled = padOrTruncate(enabled, FLAG_LENGTH);
    }

    public boolean isEnabled() {
        return "true".equals(enabled != null ? enabled.trim() : null);
    }

    public void setEnabledFlag(boolean enabled) {
        this.enabled = enabled ? "true" : "false";
    }

    @JsonIgnore
    public boolean isBlankEmpty() {
        return blank == null || blank.trim().isEmpty();
    }

    private String padOrTruncate(String input, int length) {
        if (input == null) {
            return null;
        }
        if (input.length() > length) {
            return input.substring(0, length);
        }
        return input;
    }

    @Override
    public String toString() {
        return String.format("Record[name=%s, value=%s, blank=%s, enabled=%s]",
                name, value, blank, enabled);
    }
}
