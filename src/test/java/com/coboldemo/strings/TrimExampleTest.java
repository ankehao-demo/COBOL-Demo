package com.coboldemo.strings;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TrimExampleTest {

    @Test
    void trimBothRemovesLeadingAndTrailingSpaces() {
        assertEquals("hello world", TrimExample.trimBoth("    hello world       "));
    }

    @Test
    void trimLeadingRemovesOnlyLeadingSpaces() {
        assertEquals("hello world       ", TrimExample.trimLeading("    hello world       "));
    }

    @Test
    void trimTrailingRemovesOnlyTrailingSpaces() {
        assertEquals("    hello world", TrimExample.trimTrailing("    hello world       "));
    }

    @Test
    void trimBothOnStringLiteral() {
        assertEquals("String literal", TrimExample.trimBoth("   String literal    "));
    }

    @Test
    void trimEmptyString() {
        assertEquals("", TrimExample.trimBoth("     "));
    }

    @Test
    void trimNoSpaces() {
        assertEquals("hello", TrimExample.trimBoth("hello"));
    }
}
