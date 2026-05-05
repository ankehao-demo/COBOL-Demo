package com.coboldemo.redefines;

/**
 * Common base class for {@link PersonCustomer} and {@link CorpCustomer}.
 *
 * Maps the COBOL group {@code ws-customer} which used a REDEFINES clause to
 * overlay either a person name (first/last) or a corporate name on the same
 * memory. Java has no REDEFINES, so we model the distinction with a class
 * hierarchy and use {@code instanceof} at the call site.
 */
public abstract class Customer {

    public enum Type {
        PERSON, CORP
    }

    private final Type type;
    private String streetAddress;
    private String state;
    private int zipCode;

    protected Customer(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
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
