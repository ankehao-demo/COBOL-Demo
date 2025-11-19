package com.example.cobol.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ws-record")
public class Record {
    private String recordName;
    private String recordValue;
    private boolean enabled;

    public Record() {
    }

    public Record(String recordName, String recordValue, boolean enabled) {
        this.recordName = truncate(recordName, 10);
        this.recordValue = truncate(recordValue, 10);
        this.enabled = enabled;
    }

    private String truncate(String value, int maxLength) {
        if (value == null) {
            return "";
        }
        return value.length() > maxLength ? value.substring(0, maxLength) : value;
    }

    @XmlElement(name = "name")
    @JsonProperty("name")
    public String getRecordName() {
        return recordName;
    }

    public void setRecordName(String recordName) {
        this.recordName = truncate(recordName, 10);
    }

    @XmlElement(name = "value")
    @JsonProperty("value")
    public String getRecordValue() {
        return recordValue;
    }

    public void setRecordValue(String recordValue) {
        this.recordValue = truncate(recordValue, 10);
    }

    @XmlAttribute(name = "enabled")
    @JsonProperty("enabled")
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return String.format("Record[name=%s, value=%s, enabled=%s]", recordName, recordValue, enabled);
    }
}
