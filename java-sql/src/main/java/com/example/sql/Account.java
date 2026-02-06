package com.example.sql;

import java.time.LocalDateTime;

public class Account {
    private final int id;
    private final String firstName;
    private final String lastName;
    private final String phone;
    private final String address;
    private final String isEnabled;
    private final LocalDateTime createDt;
    private final LocalDateTime modDt;

    public Account(int id, String firstName, String lastName, String phone,
                   String address, String isEnabled, LocalDateTime createDt, LocalDateTime modDt) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.address = address;
        this.isEnabled = isEnabled;
        this.createDt = createDt;
        this.modDt = modDt;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getIsEnabled() {
        return isEnabled;
    }

    public LocalDateTime getCreateDt() {
        return createDt;
    }

    public LocalDateTime getModDt() {
        return modDt;
    }

    public boolean isDisabled() {
        return "N".equals(isEnabled);
    }
}
