package com.example.cobol.mergesort;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class MergeSortExampleTest {

    @Test
    void roundTripsThroughFixedWidth() {
        MergeSortExample.Customer c = new MergeSortExample.Customer(
                42, "Smith", "Jane", 1234, "hello world");
        String fixed = c.toFixedWidth();
        assertEquals(135, fixed.length());

        MergeSortExample.Customer parsed = MergeSortExample.Customer.parse(fixed);
        assertEquals(c.id, parsed.id);
        assertEquals(c.contractId, parsed.contractId);
        assertEquals("Smith".length(), parsed.lastName.strip().length());
        assertEquals("Jane", parsed.firstName.strip());
    }
}
