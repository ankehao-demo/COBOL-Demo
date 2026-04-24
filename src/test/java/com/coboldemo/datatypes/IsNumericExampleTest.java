package com.coboldemo.datatypes;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class IsNumericExampleTest {

    @Test
    void allDigitsIsNumeric() {
        assertTrue(IsNumericExample.isAllDigits("12345"));
    }

    @Test
    void digitsWithSpacesIsNotNumericWithSpaces() {
        assertFalse(IsNumericExample.isNumericWithSpaces("123   "));
    }

    @Test
    void allDigitsPaddedFailsNumericWithSpaces() {
        assertFalse(IsNumericExample.isNumericWithSpaces("123       "));
    }

    @Test
    void pureDigitsPassesNumericWithSpaces() {
        assertTrue(IsNumericExample.isNumericWithSpaces("1234567890"));
    }

    @Test
    void emptyStringIsNotNumeric() {
        assertFalse(IsNumericExample.isAllDigits(""));
    }

    @Test
    void nullIsNotNumeric() {
        assertFalse(IsNumericExample.isAllDigits(null));
    }

    @Test
    void lettersAreNotNumeric() {
        assertFalse(IsNumericExample.isAllDigits("abc"));
    }

    @Test
    void mixedIsNotNumeric() {
        assertFalse(IsNumericExample.isAllDigits("12a34"));
    }
}
