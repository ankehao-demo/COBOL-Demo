package com.coboldemo.serialization;

/**
 * Java POJO equivalent of the COBOL ws-record group item.
 *
 * COBOL definition:
 *   01  ws-record.
 *       05  ws-record-name                  pic x(10).
 *       05  ws-record-value                 pic x(10).
 *       05  ws-record-blank                 pic x(10).
 *       05  ws-record-flag                  pic x(5) value "false".
 *           88  ws-record-flag-enabled      value "true".
 *           88  ws-record-flag-disabled     value "false".
 *
 * The 88-level condition names (ws-record-flag-enabled, ws-record-flag-disabled)
 * are stored as the string "true"/"false" in COBOL, but mapped to a native
 * Java boolean here.
 *
 * Serialization-specific representations (JsonRecord, XmlRecord) handle
 * format-specific concerns like field renaming, attribute mapping, and
 * suppress-when-spaces behavior.
 */
public class Record {

    private String name;      // ws-record-name PIC X(10)
    private String value;     // ws-record-value PIC X(10)
    private String blank;     // ws-record-blank PIC X(10)
    private boolean enabled;  // ws-record-flag (88-level boolean condition)

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

    @Override
    public String toString() {
        return "Record{name='" + name + "', value='" + value
                + "', blank='" + blank + "', enabled=" + enabled + "}";
    }
}
