package com.cobol.demo.xml;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "record")
public class XmlRecord {
    private String name;
    private String value;
    private String blank;
    private String flag;

    public XmlRecord() {
        this.flag = "false";
    }

    @XmlElement(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElement(name = "value")
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

    @XmlAttribute(name = "enabled")
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
