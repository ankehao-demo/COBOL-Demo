package com.example.cobol.unstring;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;

class UnstringExampleTest {

    @Test
    void mainRunsToCompletion() {
        assertDoesNotThrow(() -> UnstringExample.main(new String[0]));
    }
}
