package com.cobol.migration.serializer;

/**
 * Exception thrown when JSON serialization fails.
 * 
 * Mirrors COBOL's JSON-CODE error handling mechanism.
 * The errorCode field corresponds to the JSON-CODE special register
 * that COBOL sets when JSON GENERATE encounters an error.
 * 
 * Phase 2 TODO:
 * - Define error code constants matching COBOL JSON-CODE values
 * - Add detailed error messages for each code
 */
public class JsonSerializationException extends Exception {

    private final int errorCode;

    public JsonSerializationException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public JsonSerializationException(String message, int errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
