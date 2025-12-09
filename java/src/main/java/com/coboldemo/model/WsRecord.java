package com.coboldemo.model;

import java.util.Objects;

/**
 * Java POJO equivalent of the COBOL ws-record data structure.
 * 
 * This class maps the following COBOL structure:
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
 * The COBOL 88-level condition names (ws-record-flag-enabled, ws-record-flag-disabled)
 * are implemented as boolean getter methods isEnabled() and isDisabled().
 */
public class WsRecord {

    private static final int NAME_MAX_LENGTH = 10;
    private static final int VALUE_MAX_LENGTH = 10;
    private static final int BLANK_MAX_LENGTH = 10;
    private static final String FLAG_TRUE = "true";
    private static final String FLAG_FALSE = "false";

    private String name;
    private String value;
    private String blank;
    private String enabled;

    /**
     * No-arg constructor. Initializes all fields to empty strings
     * and enabled flag to "false" (matching COBOL default).
     */
    public WsRecord() {
        this.name = "";
        this.value = "";
        this.blank = "";
        this.enabled = FLAG_FALSE;
    }

    /**
     * Constructor with all fields.
     * 
     * @param name   the record name (maps from ws-record-name, max 10 chars)
     * @param value  the record value (maps from ws-record-value, max 10 chars)
     * @param blank  the blank field (maps from ws-record-blank, max 10 chars)
     * @param enabled the flag value as string "true" or "false" (maps from ws-record-flag)
     * @throws IllegalArgumentException if enabled is not "true" or "false"
     */
    public WsRecord(String name, String value, String blank, String enabled) {
        setName(name);
        setValue(value);
        setBlank(blank);
        setEnabled(enabled);
    }

    /**
     * Gets the record name.
     * 
     * @return the name field value
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the record name. If the value exceeds 10 characters (COBOL PIC X(10)),
     * it will be truncated to match COBOL fixed-length behavior.
     * 
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = truncateToLength(name, NAME_MAX_LENGTH);
    }

    /**
     * Gets the record value.
     * 
     * @return the value field
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the record value. If the value exceeds 10 characters (COBOL PIC X(10)),
     * it will be truncated to match COBOL fixed-length behavior.
     * 
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = truncateToLength(value, VALUE_MAX_LENGTH);
    }

    /**
     * Gets the blank field.
     * 
     * @return the blank field value
     */
    public String getBlank() {
        return blank;
    }

    /**
     * Sets the blank field. If the value exceeds 10 characters (COBOL PIC X(10)),
     * it will be truncated to match COBOL fixed-length behavior.
     * 
     * @param blank the blank value to set
     */
    public void setBlank(String blank) {
        this.blank = truncateToLength(blank, BLANK_MAX_LENGTH);
    }

    /**
     * Gets the enabled flag as a string ("true" or "false").
     * 
     * @return the enabled flag value
     */
    public String getEnabled() {
        return enabled;
    }

    /**
     * Sets the enabled flag. Must be "true" or "false" to match COBOL 88-level conditions.
     * 
     * @param enabled the flag value ("true" or "false")
     * @throws IllegalArgumentException if enabled is not "true" or "false"
     */
    public void setEnabled(String enabled) {
        validateFlag(enabled);
        this.enabled = enabled;
    }

    /**
     * Checks if the record is enabled.
     * This method implements the COBOL 88-level condition ws-record-flag-enabled.
     * 
     * @return true if enabled flag is "true", false otherwise
     */
    public boolean isEnabled() {
        return FLAG_TRUE.equals(enabled);
    }

    /**
     * Checks if the record is disabled.
     * This method implements the COBOL 88-level condition ws-record-flag-disabled.
     * 
     * @return true if enabled flag is "false", false otherwise
     */
    public boolean isDisabled() {
        return FLAG_FALSE.equals(enabled);
    }

    /**
     * Sets the enabled flag using a boolean value.
     * Convenience method that converts boolean to the string representation.
     * 
     * @param enabled true to set flag to "true", false to set flag to "false"
     */
    public void setEnabledBoolean(boolean enabled) {
        this.enabled = enabled ? FLAG_TRUE : FLAG_FALSE;
    }

    /**
     * Validates that the flag value is either "true" or "false".
     * 
     * @param flag the flag value to validate
     * @throws IllegalArgumentException if flag is not "true" or "false"
     */
    private void validateFlag(String flag) {
        if (flag == null || (!FLAG_TRUE.equals(flag) && !FLAG_FALSE.equals(flag))) {
            throw new IllegalArgumentException(
                "Flag must be \"true\" or \"false\", but was: " + flag);
        }
    }

    /**
     * Truncates a string to the specified maximum length to match COBOL fixed-length behavior.
     * Returns empty string if input is null.
     * 
     * @param value     the string to truncate
     * @param maxLength the maximum length
     * @return the truncated string, or empty string if input was null
     */
    private String truncateToLength(String value, int maxLength) {
        if (value == null) {
            return "";
        }
        if (value.length() > maxLength) {
            return value.substring(0, maxLength);
        }
        return value;
    }

    @Override
    public String toString() {
        return "WsRecord{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                ", blank='" + blank + '\'' +
                ", enabled='" + enabled + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WsRecord wsRecord = (WsRecord) o;
        return Objects.equals(name, wsRecord.name) &&
                Objects.equals(value, wsRecord.value) &&
                Objects.equals(blank, wsRecord.blank) &&
                Objects.equals(enabled, wsRecord.enabled);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, value, blank, enabled);
    }
}
