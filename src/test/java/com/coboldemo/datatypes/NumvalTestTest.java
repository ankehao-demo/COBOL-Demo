package com.coboldemo.datatypes;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class NumvalTestTest {

    @Test
    void numvalParsesInteger() {
        assertEquals(42.0, NumvalTest.numval("42"), 0.001);
    }

    @Test
    void numvalParsesDecimal() {
        assertEquals(3.14, NumvalTest.numval("3.14"), 0.001);
    }

    @Test
    void numvalParsesNegative() {
        assertEquals(-100.0, NumvalTest.numval("-100"), 0.001);
    }

    @Test
    void numvalTrimsWhitespace() {
        assertEquals(5.0, NumvalTest.numval("   5   "), 0.001);
    }

    @Test
    void numvalEmptyReturnsZero() {
        assertEquals(0.0, NumvalTest.numval(""), 0.001);
    }

    @Test
    void numvalNullReturnsZero() {
        assertEquals(0.0, NumvalTest.numval(null), 0.001);
    }

    @Test
    void numvalParsesLargeNumber() {
        assertEquals(123456.78, NumvalTest.numval("123456.78"), 0.001);
    }
}
