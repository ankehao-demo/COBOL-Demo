package com.cobol.demo.redefines;

public class Customer {
    private int customerType;
    private String firstName;
    private String lastName;
    private String streetAddress;
    private String state;
    private int zipCode;

    public static final int TYPE_PERSON = 1;
    public static final int TYPE_CORP = 2;

    public Customer() {
    }

    public int getCustomerType() {
        return customerType;
    }

    public void setCustomerType(int customerType) {
        this.customerType = customerType;
    }

    public boolean isPersonType() {
        return customerType == TYPE_PERSON;
    }

    public boolean isCorpType() {
        return customerType == TYPE_CORP;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCorpName() {
        if (firstName != null && lastName != null) {
            return (firstName + lastName).substring(0, Math.min(30, (firstName + lastName).length()));
        }
        return "";
    }

    public void setCorpName(String corpName) {
        if (corpName.length() <= 10) {
            this.firstName = corpName;
            this.lastName = "";
        } else {
            this.firstName = corpName.substring(0, 10);
            this.lastName = corpName.substring(10, Math.min(30, corpName.length()));
        }
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getZipCode() {
        return zipCode;
    }

    public void setZipCode(int zipCode) {
        this.zipCode = zipCode;
    }
}
