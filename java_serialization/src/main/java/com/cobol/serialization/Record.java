package com.cobol.serialization;

/**
 * Java POJO representing the COBOL ws-record group item.
 *
 * COBOL definition:
 *   01  ws-record.
 *       05  ws-record-name       pic x(10).
 *       05  ws-record-value      pic x(10).
 *       05  ws-record-blank      pic x(10).
 *       05  ws-record-flag       pic x(5) value "false".
 *           88  ws-record-flag-enabled   value "true".
 *           88  ws-record-flag-disabled  value "false".
 *
 * JSON and XML serialization are handled by dedicated serializer classes
 * (JsonSerializer and XmlSerializer) because the two formats have different
 * field-mapping and output rules (e.g., blank suppression in XML, enabled as
 * attribute in XML vs. string value in JSON).
 */
public class Record {

    private String name;
    private String value;
    private String blank;
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
