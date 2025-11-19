package com.example.cobol.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "person_customer")
public class PersonCustomer extends Customer {
    private String firstName;
    private String lastName;

    public PersonCustomer() {
        super();
        this.customerType = 1;
    }

    public PersonCustomer(String firstName, String lastName, String streetAddress, String state, int zipCode) {
        super(streetAddress, state, zipCode, 1);
        this.firstName = truncate(firstName, 10);
        this.lastName = truncate(lastName, 20);
    }

    @XmlElement(name = "first_name")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = truncate(firstName, 10);
    }

    @XmlElement(name = "last_name")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = truncate(lastName, 20);
    }

    @Override
    public String toString() {
        return String.format("Customer Type: PERSON\nFirst Name: %s\nLast Name: %s\nAddress:\n%s\n%s, %05d",
                firstName, lastName, streetAddress, state, zipCode);
    }
}
