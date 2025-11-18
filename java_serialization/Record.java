package com.example.serialization;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "ws-record")
@JsonPropertyOrder({"name", "value", "ws-record-blank", "enabled"})
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
    @JacksonXmlProperty(isAttribute = true, localName = "enabled")
    private String flag;
    
    public Record() {
        this.name = "";
        this.value = "";
        this.blank = "";
        this.flag = "false";
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = truncateAndPad(name, 10);
    }
    
    public String getValue() {
        return value;
    }
    
    public void setValue(String value) {
        this.value = truncateAndPad(value, 10);
    }
    
    public String getBlank() {
        return blank;
    }
    
    public void setBlank(String blank) {
        this.blank = truncateAndPad(blank, 10);
    }
    
    public String getFlag() {
        return flag;
    }
    
    public void setFlag(String flag) {
        if (flag.length() <= 5) {
            this.flag = flag;
        } else {
            this.flag = flag.substring(0, 5);
        }
    }
    
    public void setEnabled(boolean enabled) {
        this.flag = enabled ? "true" : "false";
    }
    
    public boolean isEnabled() {
        return "true".equals(this.flag);
    }
    
    private String truncateAndPad(String str, int length) {
        if (str == null) {
            str = "";
        }
        if (str.length() > length) {
            return str.substring(0, length);
        }
        return str;
    }
    
    @Override
    public String toString() {
        return String.format("%-10s%-10s%-10s%-5s", 
            name != null ? name : "",
            value != null ? value : "",
            blank != null ? blank : "",
            flag != null ? flag : "");
    }
}
