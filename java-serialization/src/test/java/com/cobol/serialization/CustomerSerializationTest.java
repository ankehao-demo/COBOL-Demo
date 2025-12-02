package com.cobol.serialization;

import com.cobol.serialization.json.JsonSerializer;
import com.cobol.serialization.model.Address;
import com.cobol.serialization.model.CorporateCustomer;
import com.cobol.serialization.model.Customer;
import com.cobol.serialization.model.PersonCustomer;
import com.cobol.serialization.util.SerializationResult;
import com.cobol.serialization.xml.XmlSerializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Customer serialization.
 * Tests verify handling of COBOL REDEFINES pattern using inheritance.
 */
class CustomerSerializationTest {

    private JsonSerializer jsonSerializer;
    private XmlSerializer xmlSerializer;

    @BeforeEach
    void setUp() {
        jsonSerializer = new JsonSerializer();
        xmlSerializer = new XmlSerializer();
    }

    @Test
    void testPersonCustomerCreation() {
        Address address = new Address("123 fake st", "NV", "12345");
        Customer person = Customer.createPerson("test-first", "test-last", address);

        assertTrue(person.isPerson());
        assertFalse(person.isCorporation());
        assertEquals(Customer.TYPE_PERSON, person.getCustomerType());
        assertTrue(person instanceof PersonCustomer);
    }

    @Test
    void testCorporateCustomerCreation() {
        Address address = new Address("567 real st", "NY", "11795");
        Customer corp = Customer.createCorporation("no-name corp", address);

        assertFalse(corp.isPerson());
        assertTrue(corp.isCorporation());
        assertEquals(Customer.TYPE_CORP, corp.getCustomerType());
        assertTrue(corp instanceof CorporateCustomer);
    }

    @Test
    void testPersonCustomerJsonSerialization() {
        Address address = new Address("123 fake st", "NV", "12345");
        PersonCustomer person = new PersonCustomer("test-first", "test-last", address);

        SerializationResult result = jsonSerializer.serializeObject(person);

        assertTrue(result.isSuccess());
        assertTrue(result.getOutput().contains("ws-customer-first-name"));
        assertTrue(result.getOutput().contains("ws-customer-last-name"));
        assertTrue(result.getOutput().contains("test-first"));
        assertTrue(result.getOutput().contains("test-last"));
    }

    @Test
    void testCorporateCustomerJsonSerialization() {
        Address address = new Address("567 real st", "NY", "11795");
        CorporateCustomer corp = new CorporateCustomer("no-name corp", address);

        SerializationResult result = jsonSerializer.serializeObject(corp);

        assertTrue(result.isSuccess());
        assertTrue(result.getOutput().contains("ws-corp-name"));
        assertTrue(result.getOutput().contains("no-name corp"));
    }

    @Test
    void testPersonCustomerXmlSerialization() {
        Address address = new Address("123 fake st", "NV", "12345");
        PersonCustomer person = new PersonCustomer("test-first", "test-last", address);

        SerializationResult result = xmlSerializer.serializeObject(person, PersonCustomer.class);

        assertTrue(result.isSuccess());
        assertTrue(result.getOutput().contains("ws-customer-first-name"));
        assertTrue(result.getOutput().contains("ws-customer-last-name"));
    }

    @Test
    void testCorporateCustomerXmlSerialization() {
        Address address = new Address("567 real st", "NY", "11795");
        CorporateCustomer corp = new CorporateCustomer("no-name corp", address);

        SerializationResult result = xmlSerializer.serializeObject(corp, CorporateCustomer.class);

        assertTrue(result.isSuccess());
        assertTrue(result.getOutput().contains("ws-corp-name"));
    }

    @Test
    void testAddressSerialization() {
        Address address = new Address("123 Main St", "CA", "90210");

        assertEquals("123 Main St", address.getStreetAddress());
        assertEquals("CA", address.getState());
        assertEquals("90210", address.getZipCode());
    }

    @Test
    void testAddressZipCodePadding() {
        Address address = new Address();
        address.setZipCode(123);

        assertEquals("00123", address.getZipCode());
    }

    @Test
    void testPersonCustomerDisplayName() {
        PersonCustomer person = new PersonCustomer("John", "Doe", null);

        assertEquals("John Doe", person.getDisplayName());
    }

    @Test
    void testCorporateCustomerDisplayName() {
        CorporateCustomer corp = new CorporateCustomer("Acme Corp", null);

        assertEquals("Acme Corp", corp.getDisplayName());
    }

    @Test
    void testFieldLengthTruncation() {
        PersonCustomer person = new PersonCustomer(
                "VeryLongFirstNameThatExceedsLimit",
                "VeryLongLastNameThatExceedsTheLimit",
                null);

        assertEquals(10, person.getFirstName().length());
        assertEquals(20, person.getLastName().length());
    }

    @Test
    void testCorporateNameLengthTruncation() {
        CorporateCustomer corp = new CorporateCustomer(
                "This Is A Very Long Corporate Name That Exceeds The Limit",
                null);

        assertEquals(30, corp.getCorpName().length());
    }
}
