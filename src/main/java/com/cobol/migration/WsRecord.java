package com.cobol.migration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * Java POJO equivalent of the COBOL record structure:
 *
 *     01  ws-record.
 *         05  ws-record-name                  pic x(10).
 *         05  ws-record-value                 pic x(10).
 *         05  ws-record-blank                 pic x(10).
 *         05  ws-record-flag                  pic x(5) value "false".
 *             88  ws-record-flag-enabled      value "true".
 *             88  ws-record-flag-disabled     value "false".
 *
 * This class uses both Jackson and JAXB annotations so it can be used
 * for both JSON and XML serialization.
 *
 * Field name mappings (from COBOL NAME OF clauses):
 *   ws-record-name  -> "name"
 *   ws-record-value -> "value"
 *   ws-record-flag  -> "enabled"
 *
 * Special behaviors:
 *   - ws-record-flag is an XML attribute (COBOL: TYPE OF ws-record-flag IS ATTRIBUTE)
 *   - ws-record-blank is suppressed in XML when blank (COBOL: SUPPRESS WHEN SPACES)
 *   - ws-record-blank is included in JSON even when blank (COBOL JSON has no suppression)
 */
@XmlRootElement(name = "ws-record")
@XmlAccessorType(XmlAccessType.FIELD)
public class WsRecord {

    /**
     * Equivalent to: 05 ws-record-name pic x(10).
     * Serialized as "name" in both JSON and XML.
     */
    @JsonProperty("name")
    @XmlElement(name = "name")
    private String wsRecordName;

    /**
     * Equivalent to: 05 ws-record-value pic x(10).
     * Serialized as "value" in both JSON and XML.
     */
    @JsonProperty("value")
    @XmlElement(name = "value")
    private String wsRecordValue;

    /**
     * Equivalent to: 05 ws-record-blank pic x(10).
     * In XML: suppressed when blank (SUPPRESS WHEN SPACES).
     * In JSON: included even when blank (no suppression in COBOL JSON GENERATE).
     */
    @XmlElement(name = "blank")
    @XmlJavaTypeAdapter(BlankSuppressingAdapter.class)
    private String wsRecordBlank;

    /**
     * Equivalent to: 05 ws-record-flag pic x(5) value "false".
     *     88 ws-record-flag-enabled  value "true".
     *     88 ws-record-flag-disabled value "false".
     *
     * Serialized as "enabled" in both JSON and XML.
     * In XML, this is rendered as an attribute (TYPE OF ws-record-flag IS ATTRIBUTE).
     */
    @JsonProperty("enabled")
    @XmlAttribute(name = "enabled")
    private String wsRecordFlag;

    /**
     * Default constructor. Initializes wsRecordFlag to "false"
     * matching the COBOL default: pic x(5) value "false".
     */
    public WsRecord() {
        this.wsRecordFlag = "false";
    }

    // --- Getters and Setters ---

    public String getWsRecordName() {
        return wsRecordName;
    }

    public void setWsRecordName(String wsRecordName) {
        this.wsRecordName = wsRecordName;
    }

    public String getWsRecordValue() {
        return wsRecordValue;
    }

    public void setWsRecordValue(String wsRecordValue) {
        this.wsRecordValue = wsRecordValue;
    }

    public String getWsRecordBlank() {
        return wsRecordBlank;
    }

    public void setWsRecordBlank(String wsRecordBlank) {
        this.wsRecordBlank = wsRecordBlank;
    }

    public String getWsRecordFlag() {
        return wsRecordFlag;
    }

    public void setWsRecordFlag(String wsRecordFlag) {
        this.wsRecordFlag = wsRecordFlag;
    }

    /**
     * Equivalent to: SET ws-record-flag-enabled TO TRUE
     * Sets the flag to "true".
     */
    public void setFlagEnabled() {
        this.wsRecordFlag = "true";
    }

    /**
     * Equivalent to: SET ws-record-flag-disabled TO TRUE
     * Sets the flag to "false".
     */
    public void setFlagDisabled() {
        this.wsRecordFlag = "false";
    }

    /**
     * Equivalent to: 88 ws-record-flag-enabled value "true"
     * Returns true if the flag is "true".
     */
    public boolean isFlagEnabled() {
        return "true".equals(wsRecordFlag);
    }

    /**
     * Equivalent to: 88 ws-record-flag-disabled value "false"
     * Returns true if the flag is "false".
     */
    public boolean isFlagDisabled() {
        return "false".equals(wsRecordFlag);
    }
}
