package com.cobol.serialization.exception;

/**
 * Exception class for JSON serialization errors.
 * Mirrors COBOL's JSON-CODE error handling mechanism.
 * 
 * COBOL equivalent:
 * <pre>
 * on exception
 *     display "Error generating JSON error " JSON-CODE
 *     stop run
 * </pre>
 * 
 * Common JSON-CODE values in GnuCOBOL:
 * - 1: Invalid data
 * - 2: Output buffer too small
 * - 3: Invalid name mapping
 * - 4: Internal error
 */
public class JsonSerializationException extends SerializationException {
    
    public static final int ERROR_INVALID_DATA = 1;
    public static final int ERROR_BUFFER_TOO_SMALL = 2;
    public static final int ERROR_INVALID_NAME_MAPPING = 3;
    public static final int ERROR_INTERNAL = 4;
    
    public JsonSerializationException(String message, int jsonCode) {
        super(message, jsonCode);
    }
    
    public JsonSerializationException(String message, int jsonCode, Throwable cause) {
        super(message, jsonCode, cause);
    }
    
    /**
     * Returns the JSON-CODE equivalent error code.
     */
    public int getJsonCode() {
        return getErrorCode();
    }
    
    @Override
    public String toString() {
        return "JsonSerializationException{" +
               "message='" + getMessage() + '\'' +
               ", JSON-CODE=" + getJsonCode() +
               '}';
    }
}
