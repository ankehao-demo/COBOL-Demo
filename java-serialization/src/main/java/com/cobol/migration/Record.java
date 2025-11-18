package com.cobol.migration;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

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
    @JacksonXmlProperty(isAttribute = true, localName = "enabled")
    private String enabled;
    
    public Record() {
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
    public boolean isEnabled() {
        return "true".equalsIgnoreCase(enabled);
    }
    
    @JsonIgnore
    public boolean isDisabled() {
        return "false".equalsIgnoreCase(enabled);
    }
}
