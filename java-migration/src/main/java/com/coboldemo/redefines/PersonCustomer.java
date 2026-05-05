package com.coboldemo.redefines;

/**
 * Person-shaped {@link Customer}: holds first and last name.
 */
public class PersonCustomer extends Customer {

    private String firstName;
    private String lastName;

    public PersonCustomer() {
        super(Type.PERSON);
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
}
