package com.cobol.serialization.model;

/**
 * Java POJO class mirroring the COBOL ws-record data structure.
 * 
 * COBOL equivalent:
 * <pre>
 * 01  ws-record.
 *     05  ws-record-name                  pic x(10).
 *     05  ws-record-value                 pic x(10).
 *     05  ws-record-blank                 pic x(10).
 *     05  ws-record-flag                  pic x(5) value "false".
 *         88  ws-record-flag-enabled      value "true".
 *         88  ws-record-flag-disabled     value "false".
 * </pre>
 * 
 * Field mappings:
 * - ws-record-name (PIC X(10)) -> String name (max 10 chars)
 * - ws-record-value (PIC X(10)) -> String value (max 10 chars)
 * - ws-record-blank (PIC X(10)) -> String blank (max 10 chars)
 * - ws-record-flag (PIC X(5)) -> String flag ("true" or "false")
 */
public class WsRecord {
    
    private static final int NAME_MAX_LENGTH = 10;
    private static final int VALUE_MAX_LENGTH = 10;
    private static final int BLANK_MAX_LENGTH = 10;
    private static final int FLAG_MAX_LENGTH = 5;
    
    private static final String FLAG_TRUE = "true";
    private static final String FLAG_FALSE = "false";
    
    private String name;
    private String value;
    private String blank;
    private String flag;
    
    public WsRecord() {
        this.name = "";
        this.value = "";
        this.blank = "";
        this.flag = FLAG_FALSE;
    }
    
    public WsRecord(String name, String value, String blank, boolean enabled) {
        setName(name);
        setValue(value);
        setBlank(blank);
        setFlagEnabled(enabled);
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = truncateToLength(name, NAME_MAX_LENGTH);
    }
    
    public String getValue() {
        return value;
    }
    
    public void setValue(String value) {
        this.value = truncateToLength(value, VALUE_MAX_LENGTH);
    }
    
    public String getBlank() {
        return blank;
    }
    
    public void setBlank(String blank) {
        this.blank = truncateToLength(blank, BLANK_MAX_LENGTH);
    }
    
    public String getFlag() {
        return flag;
    }
    
    public void setFlag(String flag) {
        if (FLAG_TRUE.equals(flag) || FLAG_FALSE.equals(flag)) {
            this.flag = flag;
        } else {
            this.flag = FLAG_FALSE;
        }
    }
    
    /**
     * Implements COBOL 88-level condition: ws-record-flag-enabled
     * Returns true if flag equals "true"
     */
    public boolean isFlagEnabled() {
        return FLAG_TRUE.equals(this.flag);
    }
    
    /**
     * Implements COBOL 88-level condition: ws-record-flag-disabled
     * Returns true if flag equals "false"
     */
    public boolean isFlagDisabled() {
        return FLAG_FALSE.equals(this.flag);
    }
    
    /**
     * Implements COBOL SET ws-record-flag-enabled TO TRUE
     */
    public void setFlagEnabled(boolean enabled) {
        this.flag = enabled ? FLAG_TRUE : FLAG_FALSE;
    }
    
    /**
     * Checks if the blank field contains only spaces or is empty.
     * Used for SUPPRESS WHEN SPACES functionality.
     */
    public boolean isBlankEmpty() {
        return blank == null || blank.trim().isEmpty();
    }
    
    /**
     * Returns the COBOL-style padded representation of the record.
     * Each field is padded to its maximum length with spaces.
     */
    public String toCobolString() {
        return padToLength(name, NAME_MAX_LENGTH) +
               padToLength(value, VALUE_MAX_LENGTH) +
               padToLength(blank, BLANK_MAX_LENGTH) +
               padToLength(flag, FLAG_MAX_LENGTH);
    }
    
    private String truncateToLength(String str, int maxLength) {
        if (str == null) {
            return "";
        }
        return str.length() > maxLength ? str.substring(0, maxLength) : str;
    }
    
    private String padToLength(String str, int length) {
        if (str == null) {
            str = "";
        }
        if (str.length() >= length) {
            return str.substring(0, length);
        }
        StringBuilder sb = new StringBuilder(str);
        while (sb.length() < length) {
            sb.append(' ');
        }
        return sb.toString();
    }
    
    @Override
    public String toString() {
        return "WsRecord{" +
               "name='" + name + '\'' +
               ", value='" + value + '\'' +
               ", blank='" + blank + '\'' +
               ", flag='" + flag + '\'' +
               '}';
    }
}
