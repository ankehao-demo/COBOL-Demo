package com.example.cobol.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.time.LocalDateTime;

@XmlRootElement(name = "account")
public class Account {
    private int id;
    private String firstName;
    private String lastName;
    private String phone;
    private String address;
    private String isEnabled;
    private LocalDateTime createDt;
    private LocalDateTime modDt;

    public Account() {
    }

    public Account(int id, String firstName, String lastName, String phone, 
                   String address, String isEnabled, LocalDateTime createDt, LocalDateTime modDt) {
        this.id = id;
        this.firstName = truncate(firstName, 8);
        this.lastName = truncate(lastName, 8);
        this.phone = truncate(phone, 10);
        this.address = truncate(address, 22);
        this.isEnabled = isEnabled;
        this.createDt = createDt;
        this.modDt = modDt;
    }

    private String truncate(String value, int maxLength) {
        if (value == null) {
            return "";
        }
        return value.length() > maxLength ? value.substring(0, maxLength) : value;
    }

    @XmlElement
    @JsonProperty("id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @XmlElement(name = "first_name")
    @JsonProperty("first_name")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = truncate(firstName, 8);
    }

    @XmlElement(name = "last_name")
    @JsonProperty("last_name")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = truncate(lastName, 8);
    }

    @XmlElement
    @JsonProperty("phone")
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = truncate(phone, 10);
    }

    @XmlElement
    @JsonProperty("address")
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = truncate(address, 22);
    }

    @XmlElement(name = "is_enabled")
    @JsonProperty("is_enabled")
    public String getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(String isEnabled) {
        this.isEnabled = isEnabled;
    }

    public boolean isEnabled() {
        return "Y".equals(isEnabled);
    }

    public boolean isDisabled() {
        return "N".equals(isEnabled);
    }

    @XmlElement(name = "create_dt")
    @JsonProperty("create_dt")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public LocalDateTime getCreateDt() {
        return createDt;
    }

    public void setCreateDt(LocalDateTime createDt) {
        this.createDt = createDt;
    }

    @XmlElement(name = "mod_dt")
    @JsonProperty("mod_dt")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public LocalDateTime getModDt() {
        return modDt;
    }

    public void setModDt(LocalDateTime modDt) {
        this.modDt = modDt;
    }

    @Override
    public String toString() {
        return String.format("%5d | %-8s | %-8s | %-10s | %-22s | %s",
                id,
                firstName != null ? firstName : "",
                lastName != null ? lastName : "",
                phone != null ? phone : "",
                address != null ? address : "",
                isEnabled != null ? isEnabled : "");
    }
}
