package com.cobol.serialization.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * Java equivalent of the COBOL ws-customer structure from redefines.cbl.
 * Uses inheritance to handle the REDEFINES clause that creates polymorphic data views.
 * 
 * COBOL structure:
 * 01  ws-customer                       occurs 0 to 99 times
 *                                       depending on ws-num-records
 *                                       indexed by ws-customer-idx.
 *     05  ws-customer-type                pic 9.
 *         88  ws-customer-type-person     value 1.
 *         88  ws-customer-type-corp       value 2.
 *     05  ws-customer-name.
 *         10  ws-customer-first-name      pic x(10).
 *         10  ws-customer-last-name       pic x(20).
 *     05  ws-corp-name redefines ws-customer-name pic x(30).
 *     05  ws-customer-address.
 *         10  ws-street-address           pic x(20).
 *         10  ws-state                    pic xx.
 *         10  ws-zip-code                 pic 9(5).
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({PersonCustomer.class, CorporateCustomer.class})
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "ws-customer-type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = PersonCustomer.class, name = "1"),
    @JsonSubTypes.Type(value = CorporateCustomer.class, name = "2")
})
public abstract class Customer {

    public static final int TYPE_PERSON = 1;
    public static final int TYPE_CORP = 2;

    @XmlElement(name = "ws-customer-type")
    @JsonProperty("ws-customer-type")
    protected int customerType;

    @XmlElement(name = "ws-customer-address")
    @JsonProperty("ws-customer-address")
    protected Address address;

    protected Customer() {
    }

    protected Customer(int customerType, Address address) {
        this.customerType = customerType;
        this.address = address;
    }

    public int getCustomerType() {
        return customerType;
    }

    public void setCustomerType(int customerType) {
        this.customerType = customerType;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public boolean isPerson() {
        return customerType == TYPE_PERSON;
    }

    public boolean isCorporation() {
        return customerType == TYPE_CORP;
    }

    public abstract String getDisplayName();

    public static Customer createPerson(String firstName, String lastName, Address address) {
        return new PersonCustomer(firstName, lastName, address);
    }

    public static Customer createCorporation(String corpName, Address address) {
        return new CorporateCustomer(corpName, address);
    }
}
