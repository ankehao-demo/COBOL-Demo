package com.example.cobol.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

@XmlRootElement(name = "customer")
@XmlSeeAlso({PersonCustomer.class, CorpCustomer.class})
public abstract class Customer {
    protected String streetAddress;
    protected String state;
    protected int zipCode;
    protected int customerType;

    public Customer() {
    }

    public Customer(String streetAddress, String state, int zipCode, int customerType) {
        this.streetAddress = truncate(streetAddress, 20);
        this.state = truncate(state, 2);
        this.zipCode = zipCode;
        this.customerType = customerType;
    }

    protected String truncate(String value, int maxLength) {
        if (value == null) {
            return "";
        }
        return value.length() > maxLength ? value.substring(0, maxLength) : value;
    }

    @XmlElement(name = "street_address")
    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = truncate(streetAddress, 20);
    }

    @XmlElement
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = truncate(state, 2);
    }

    @XmlElement(name = "zip_code")
    public int getZipCode() {
        return zipCode;
    }

    public void setZipCode(int zipCode) {
        this.zipCode = zipCode;
    }

    @XmlElement(name = "customer_type")
    public int getCustomerType() {
        return customerType;
    }

    public void setCustomerType(int customerType) {
        this.customerType = customerType;
    }

    public boolean isPersonCustomer() {
        return customerType == 1;
    }

    public boolean isCorpCustomer() {
        return customerType == 2;
    }
}
