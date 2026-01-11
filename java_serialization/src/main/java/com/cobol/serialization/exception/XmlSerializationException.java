package com.cobol.serialization.exception;

/**
 * Exception class for XML serialization errors.
 * Mirrors COBOL's XML-CODE error handling mechanism.
 * 
 * COBOL equivalent:
 * <pre>
 * on exception
 *     display "Error generating xml error " XML-CODE
 *     stop run
 * </pre>
 * 
 * Common XML-CODE values in GnuCOBOL:
 * - 1: Invalid data
 * - 2: Output buffer too small
 * - 3: Invalid name mapping
 * - 4: Invalid attribute specification
 * - 5: Internal error
 */
public class XmlSerializationException extends SerializationException {
    
    public static final int ERROR_INVALID_DATA = 1;
    public static final int ERROR_BUFFER_TOO_SMALL = 2;
    public static final int ERROR_INVALID_NAME_MAPPING = 3;
    public static final int ERROR_INVALID_ATTRIBUTE = 4;
    public static final int ERROR_INTERNAL = 5;
    
    public XmlSerializationException(String message, int xmlCode) {
        super(message, xmlCode);
    }
    
    public XmlSerializationException(String message, int xmlCode, Throwable cause) {
        super(message, xmlCode, cause);
    }
    
    /**
     * Returns the XML-CODE equivalent error code.
     */
    public int getXmlCode() {
        return getErrorCode();
    }
    
    @Override
    public String toString() {
        return "XmlSerializationException{" +
               "message='" + getMessage() + '\'' +
               ", XML-CODE=" + getXmlCode() +
               '}';
    }
}
