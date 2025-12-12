package com.cobol.migration.model;

/**
 * Wrapper class to hold serialization output along with character count.
 * 
 * Mirrors COBOL's COUNT IN clause functionality which tracks the actual
 * character count of generated JSON/XML output.
 * 
 * Phase 2 TODO:
 * - Add validation for output
 * - Consider adding metadata about serialization process
 */
public class SerializationResult {

    private final String output;
    private final int characterCount;

    public SerializationResult(String output, int characterCount) {
        this.output = output;
        this.characterCount = characterCount;
    }

    public String getOutput() {
        return output;
    }

    public int getCharacterCount() {
        return characterCount;
    }
}
