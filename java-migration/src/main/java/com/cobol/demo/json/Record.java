package com.cobol.demo.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Record {
    @JsonProperty("name")
    private String name;

    @JsonProperty("value")
    private String value;

    private String blank;

    @JsonProperty("enabled")
    private String flag;

    public Record() {
        this.flag = "false";
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

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public boolean isEnabled() {
        return "true".equals(flag);
    }

    public void setEnabled(boolean enabled) {
        this.flag = enabled ? "true" : "false";
    }
}
