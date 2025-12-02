package com.cobol.serialization.util;

/**
 * Result object for serialization operations.
 * Equivalent to COBOL's COUNT IN clause and error handling with XML-CODE/JSON-CODE.
 */
public class SerializationResult {

    private final String output;
    private final int characterCount;
    private final int errorCode;
    private final String errorMessage;
    private final boolean success;

    private SerializationResult(String output, int characterCount, int errorCode, 
                                String errorMessage, boolean success) {
        this.output = output;
        this.characterCount = characterCount;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.success = success;
    }

    public static SerializationResult success(String output) {
        return new SerializationResult(output, output.length(), 0, null, true);
    }

    public static SerializationResult error(int errorCode, String errorMessage) {
        return new SerializationResult(null, 0, errorCode, errorMessage, false);
    }

    public String getOutput() {
        return output;
    }

    public int getCharacterCount() {
        return characterCount;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public boolean isSuccess() {
        return success;
    }

    @Override
    public String toString() {
        if (success) {
            return String.format("SerializationResult[success=true, characterCount=%d]", characterCount);
        } else {
            return String.format("SerializationResult[success=false, errorCode=%d, errorMessage=%s]", 
                    errorCode, errorMessage);
        }
    }
}
