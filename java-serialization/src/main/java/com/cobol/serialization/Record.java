package com.cobol.serialization;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "ws-record")
public class Record {
    
    @JsonProperty("name")
    @JacksonXmlProperty(localName = "name")
    private String recordName;
    
    @JsonProperty("value")
    @JacksonXmlProperty(localName = "value")
    private String recordValue;
    
    @JsonProperty("enabled")
    @JacksonXmlProperty(isAttribute = true, localName = "enabled")
    private String recordFlag;
    
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JacksonXmlProperty(localName = "recordBlank")
    private String recordBlank;
    
    public Record() {
    }
    
    public Record(String recordName, String recordValue, String recordFlag, String recordBlank) {
        this.recordName = recordName;
        this.recordValue = recordValue;
        this.recordFlag = recordFlag;
        this.recordBlank = recordBlank;
    }
    
    public String getRecordName() {
        return recordName;
    }
    
    public void setRecordName(String recordName) {
        this.recordName = recordName;
    }
    
    public String getRecordValue() {
        return recordValue;
    }
    
    public void setRecordValue(String recordValue) {
        this.recordValue = recordValue;
    }
    
    public String getRecordFlag() {
        return recordFlag;
    }
    
    public void setRecordFlag(String recordFlag) {
        this.recordFlag = recordFlag;
    }
    
    public String getRecordBlank() {
        if (recordBlank != null && recordBlank.trim().isEmpty()) {
            return null;
        }
        return recordBlank;
    }
    
    public void setRecordBlank(String recordBlank) {
        this.recordBlank = recordBlank;
    }
    
    @Override
    public String toString() {
        return "Record{" +
                "recordName='" + recordName + '\'' +
                ", recordValue='" + recordValue + '\'' +
                ", recordFlag='" + recordFlag + '\'' +
                ", recordBlank='" + recordBlank + '\'' +
                '}';
    }
}
