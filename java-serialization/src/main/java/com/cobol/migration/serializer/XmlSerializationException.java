package com.cobol.migration.serializer;

/**
 * Exception thrown when XML serialization fails.
 * 
 * Mirrors COBOL's XML-CODE error handling mechanism.
 * The errorCode field corresponds to the XML-CODE special register
 * that COBOL sets when XML GENERATE encounters an error.
 * 
 * Phase 2 TODO:
 * - Define error code constants matching COBOL XML-CODE values
 * - Add detailed error messages for each code
 */
public class XmlSerializationException extends Exception {

    private final int errorCode;

    public XmlSerializationException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public XmlSerializationException(String message, int errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
