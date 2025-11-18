package com.example.cobol.migration.util;

public class CobolDataConverter {
    
    public static String trimCobolString(String cobolString) {
        if (cobolString == null) {
            return null;
        }
        return cobolString.stripTrailing();
    }
    
    public static String padCobolString(String javaString, int length) {
        if (javaString == null) {
            return " ".repeat(length);
        }
        if (javaString.length() >= length) {
            return javaString.substring(0, length);
        }
        return javaString + " ".repeat(length - javaString.length());
    }
    
    public static int cobolNumericToInt(String cobolNumeric) {
        if (cobolNumeric == null || cobolNumeric.trim().isEmpty()) {
            return 0;
        }
        return Integer.parseInt(cobolNumeric.trim());
    }
    
    public static String intToCobolNumeric(int value, int length) {
        return String.format("%0" + length + "d", value);
    }
    
    public static String normalizeForSerialization(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.stripTrailing();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
