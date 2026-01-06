package com.cobol.serialization;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 * Java POJO equivalent of the COBOL ws-record structure.
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
@JacksonXmlRootElement(localName = "ws-record")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Record {

    @JsonProperty("name")
    @JacksonXmlProperty(localName = "name")
    private String name;

    @JsonProperty("value")
    @JacksonXmlProperty(localName = "value")
    private String value;

    @JsonProperty("ws-record-blank")
    @JacksonXmlProperty(localName = "ws-record-blank")
    private String blank;

    @JsonProperty("enabled")
    @JacksonXmlProperty(localName = "enabled", isAttribute = true)
    private String enabled;

    public Record() {
        this.enabled = "false";
    }

    public Record(String name, String value, String blank, String enabled) {
        this.name = name;
        this.value = value;
        this.blank = blank;
        this.enabled = enabled;
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

    public void setEnabledFlag(boolean flag) {
        this.enabled = flag ? "true" : "false";
    }

    @JsonIgnore
    public boolean isEnabledFlag() {
        return "true".equals(this.enabled);
    }

    @Override
    public String toString() {
        return String.format("%s%s%s%s",
                padRight(name, 10),
                padRight(value, 10),
                padRight(blank, 10),
                padRight(enabled, 5));
    }

    private String padRight(String s, int length) {
        if (s == null) {
            s = "";
        }
        if (s.length() >= length) {
            return s.substring(0, length);
        }
        return String.format("%-" + length + "s", s);
    }
}
