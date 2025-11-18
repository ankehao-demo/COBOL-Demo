package com.example.mergesort;

import org.junit.Test;
import static org.junit.Assert.*;

public class CustomerRecordTest {

    @Test
    public void testConstructorAndGetters() {
        CustomerRecord record = new CustomerRecord(1, "Smith", "John", 5423, "Test comment");
        
        assertEquals(1, record.getCustomerId());
        assertEquals("Smith", record.getLastName());
        assertEquals("John", record.getFirstName());
        assertEquals(5423, record.getContractId());
        assertEquals("Test comment", record.getComment());
    }

    @Test
    public void testSetters() {
        CustomerRecord record = new CustomerRecord();
        
        record.setCustomerId(10);
        record.setLastName("Doe");
        record.setFirstName("Jane");
        record.setContractId(1234);
        record.setComment("New comment");
        
        assertEquals(10, record.getCustomerId());
        assertEquals("Doe", record.getLastName());
        assertEquals("Jane", record.getFirstName());
        assertEquals(1234, record.getContractId());
        assertEquals("New comment", record.getComment());
    }

    @Test
    public void testCompareTo() {
        CustomerRecord record1 = new CustomerRecord(1, "Smith", "John", 5423, "Comment1");
        CustomerRecord record2 = new CustomerRecord(5, "Doe", "Jane", 1234, "Comment2");
        CustomerRecord record3 = new CustomerRecord(1, "Jones", "Bob", 9999, "Comment3");
        
        assertTrue(record1.compareTo(record2) < 0);
        assertTrue(record2.compareTo(record1) > 0);
        assertEquals(0, record1.compareTo(record3));
    }

    @Test
    public void testToString() {
        CustomerRecord record = new CustomerRecord(1, "Smith", "John", 5423, "Test");
        String result = record.toString();
        
        assertNotNull(result);
        assertTrue(result.startsWith("00001"));
        assertTrue(result.contains("Smith"));
        assertTrue(result.contains("John"));
        assertTrue(result.contains("05423"));
        assertTrue(result.contains("Test"));
        assertEquals(135, result.length());
    }

    @Test
    public void testFromString() {
        String line = "00001Smith                                            John                                              05423Test                     ";
        CustomerRecord record = CustomerRecord.fromString(line);
        
        assertEquals(1, record.getCustomerId());
        assertEquals("Smith", record.getLastName());
        assertEquals("John", record.getFirstName());
        assertEquals(5423, record.getContractId());
        assertEquals("Test", record.getComment());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromStringInvalidFormat() {
        CustomerRecord.fromString("short");
    }

    @Test
    public void testToStringAndFromStringRoundTrip() {
        CustomerRecord original = new CustomerRecord(999, "LastName", "FirstName", 12345, "Comment");
        String serialized = original.toString();
        CustomerRecord deserialized = CustomerRecord.fromString(serialized);
        
        assertEquals(original.getCustomerId(), deserialized.getCustomerId());
        assertEquals(original.getLastName(), deserialized.getLastName());
        assertEquals(original.getFirstName(), deserialized.getFirstName());
        assertEquals(original.getContractId(), deserialized.getContractId());
        assertEquals(original.getComment(), deserialized.getComment());
    }
}
