package com.example.cobol.migration.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JacksonXmlRootElement(localName = "ws-record")
public class Record {
    
    @JsonProperty("name")
    @JacksonXmlProperty(localName = "name")
    private String name;
    
    @JsonProperty("value")
    @JacksonXmlProperty(localName = "value")
    private String value;
    
    @JsonProperty("blank")
    @JacksonXmlProperty(localName = "blank")
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
    
    @JsonIgnore
    public void setEnabledFlag(boolean flag) {
        this.enabled = flag ? "true" : "false";
    }
    
    @JsonIgnore
    public boolean isEnabledFlag() {
        return "true".equals(this.enabled);
    }
    
    @Override
    public String toString() {
        return "Record{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                ", blank='" + blank + '\'' +
                ", enabled='" + enabled + '\'' +
                '}';
    }
}
