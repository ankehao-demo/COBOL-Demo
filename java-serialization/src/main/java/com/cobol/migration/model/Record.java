package com.cobol.migration.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Java POJO representing the COBOL ws-record structure.
 * 
 * Maps to COBOL structure:
 * <pre>
 * 01  ws-record.
 *     05  ws-record-name                  pic x(10).
 *     05  ws-record-value                 pic x(10).
 *     05  ws-record-blank                 pic x(10).
 *     05  ws-record-flag                  pic x(5) value "false".
 *         88  ws-record-flag-enabled      value "true".
 *         88  ws-record-flag-disabled     value "false".
 * </pre>
 * 
 * Phase 2 TODO:
 * - Implement field validation
 * - Add builder pattern
 * - Implement equals/hashCode
 */
@JsonRootName("ws-record")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@XmlRootElement(name = "ws-record")
@XmlAccessorType(XmlAccessType.FIELD)
public class Record {

    @JsonProperty("name")
    @XmlElement(name = "name")
    private String name;

    @JsonProperty("value")
    @XmlElement(name = "value")
    private String value;

    @JsonProperty("ws-record-blank")
    @XmlElement(name = "ws-record-blank")
    private String blank;

    @JsonProperty("enabled")
    @XmlAttribute(name = "enabled")
    private boolean enabled;

    public Record() {
    }

    public Record(String name, String value, String blank, boolean enabled) {
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

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
