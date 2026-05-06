package com.example.cobol.trim;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class TrimFunctionTestTest {

    @Test
    void stripBehavesLikeCobolTrim() {
        String s = "    hello world       ";
        assertEquals("hello world", s.strip());
        assertEquals("hello world       ", s.stripLeading());
        assertEquals("    hello world", s.stripTrailing());
    }
}
