package com.cobol.serialization.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Java equivalent of the COBOL ws-customer structure when ws-customer-type-person is true.
 * Represents a person customer with first and last name fields.
 * 
 * COBOL structure (person view):
 * 05  ws-customer-name.
 *     10  ws-customer-first-name      pic x(10).
 *     10  ws-customer-last-name       pic x(20).
 */
@XmlRootElement(name = "ws-customer")
@XmlAccessorType(XmlAccessType.FIELD)
public class PersonCustomer extends Customer {

    private static final int FIRST_NAME_LENGTH = 10;
    private static final int LAST_NAME_LENGTH = 20;

    @XmlElement(name = "ws-customer-first-name")
    @JsonProperty("ws-customer-first-name")
    private String firstName;

    @XmlElement(name = "ws-customer-last-name")
    @JsonProperty("ws-customer-last-name")
    private String lastName;

    public PersonCustomer() {
        super();
        this.customerType = TYPE_PERSON;
    }

    public PersonCustomer(String firstName, String lastName, Address address) {
        super(TYPE_PERSON, address);
        this.firstName = padOrTruncate(firstName, FIRST_NAME_LENGTH);
        this.lastName = padOrTruncate(lastName, LAST_NAME_LENGTH);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = padOrTruncate(firstName, FIRST_NAME_LENGTH);
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = padOrTruncate(lastName, LAST_NAME_LENGTH);
    }

    @Override
    public String getDisplayName() {
        String first = firstName != null ? firstName.trim() : "";
        String last = lastName != null ? lastName.trim() : "";
        return (first + " " + last).trim();
    }

    public String getRawNameData() {
        String first = firstName != null ? String.format("%-" + FIRST_NAME_LENGTH + "s", firstName) : 
                       String.format("%" + FIRST_NAME_LENGTH + "s", "");
        String last = lastName != null ? String.format("%-" + LAST_NAME_LENGTH + "s", lastName) : 
                      String.format("%" + LAST_NAME_LENGTH + "s", "");
        return first + last;
    }

    private String padOrTruncate(String input, int length) {
        if (input == null) {
            return null;
        }
        if (input.length() > length) {
            return input.substring(0, length);
        }
        return input;
    }

    @Override
    public String toString() {
        return String.format("PersonCustomer[firstName=%s, lastName=%s, address=%s]",
                firstName, lastName, address);
    }
}
