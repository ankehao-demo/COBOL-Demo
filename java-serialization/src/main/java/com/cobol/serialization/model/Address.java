package com.cobol.serialization.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * Java equivalent of the COBOL ws-customer-address structure from redefines.cbl.
 * 
 * COBOL structure:
 * 05  ws-customer-address.
 *     10  ws-street-address           pic x(20).
 *     10  ws-state                    pic xx.
 *     10  ws-zip-code                 pic 9(5).
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Address {

    private static final int STREET_LENGTH = 20;
    private static final int STATE_LENGTH = 2;
    private static final int ZIP_LENGTH = 5;

    @XmlElement(name = "ws-street-address")
    @JsonProperty("ws-street-address")
    private String streetAddress;

    @XmlElement(name = "ws-state")
    @JsonProperty("ws-state")
    private String state;

    @XmlElement(name = "ws-zip-code")
    @JsonProperty("ws-zip-code")
    private String zipCode;

    public Address() {
    }

    public Address(String streetAddress, String state, String zipCode) {
        this.streetAddress = padOrTruncate(streetAddress, STREET_LENGTH);
        this.state = padOrTruncate(state, STATE_LENGTH);
        this.zipCode = padOrTruncateNumeric(zipCode, ZIP_LENGTH);
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = padOrTruncate(streetAddress, STREET_LENGTH);
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = padOrTruncate(state, STATE_LENGTH);
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = padOrTruncateNumeric(zipCode, ZIP_LENGTH);
    }

    public void setZipCode(int zipCode) {
        this.zipCode = String.format("%05d", zipCode);
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

    private String padOrTruncateNumeric(String input, int length) {
        if (input == null) {
            return null;
        }
        String numericOnly = input.replaceAll("[^0-9]", "");
        if (numericOnly.length() > length) {
            return numericOnly.substring(0, length);
        }
        return String.format("%" + length + "s", numericOnly).replace(' ', '0');
    }

    @Override
    public String toString() {
        return String.format("Address[street=%s, state=%s, zip=%s]",
                streetAddress, state, zipCode);
    }
}
