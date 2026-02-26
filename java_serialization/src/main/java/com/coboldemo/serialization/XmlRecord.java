package com.coboldemo.serialization;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 * XML-specific representation of the COBOL ws-record.
 *
 * Key differences from JSON serialization:
 * - "enabled" is an XML attribute (COBOL: type of ws-record-flag is attribute)
 * - "ws-record-blank" is suppressed when it contains only spaces
 *   (COBOL: suppress when spaces)
 * - Root element is "ws-record"
 *
 * Expected XML output:
 *   {@code <?xml version="1.0"?><ws-record enabled="true"><name>Test Name</name><value>Test Value</value></ws-record>}
 */
@JacksonXmlRootElement(localName = "ws-record")
@JsonPropertyOrder({"name", "value", "ws-record-blank"})
public class XmlRecord {

    @JacksonXmlProperty(isAttribute = true, localName = "enabled")
    private boolean enabled;

    @JacksonXmlProperty(localName = "name")
    private String name;

    @JacksonXmlProperty(localName = "value")
    private String value;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JacksonXmlProperty(localName = "ws-record-blank")
    private String blank;

    public XmlRecord() {
    }

    /**
     * Creates an XmlRecord from a Record, applying the COBOL
     * "suppress when spaces" rule to the blank field.
     *
     * @param record the source Record
     * @return an XmlRecord with blank suppressed if it contains only spaces
     */
    public static XmlRecord fromRecord(Record record) {
        XmlRecord xmlRecord = new XmlRecord();
        xmlRecord.setName(record.getName());
        xmlRecord.setValue(record.getValue());
        xmlRecord.setEnabled(record.isEnabled());

        // COBOL "suppress when spaces": omit blank field if it contains only spaces
        String blank = record.getBlank();
        if (blank != null && blank.trim().isEmpty()) {
            xmlRecord.setBlank(null);
        } else {
            xmlRecord.setBlank(blank);
        }

        return xmlRecord;
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
