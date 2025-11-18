package com.example.cobol.migration.serializer;

public class SerializationException extends Exception {
    
    private final String errorCode;
    
    public SerializationException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
    
    public SerializationException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
}
