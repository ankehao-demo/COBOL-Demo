package com.example.cobol.redefines;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;

class RedefinesTestTest {

    @Test
    void mainRunsToCompletion() {
        assertDoesNotThrow(() -> RedefinesTest.main(new String[0]));
    }
}
