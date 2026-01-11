package com.cobol.serialization.exception;

/**
 * Base exception class for serialization errors.
 * Mirrors COBOL's error handling mechanism for JSON GENERATE and XML GENERATE.
 */
public class SerializationException extends Exception {
    
    private final int errorCode;
    
    public SerializationException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
    
    public SerializationException(String message, int errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }
    
    public int getErrorCode() {
        return errorCode;
    }
}
