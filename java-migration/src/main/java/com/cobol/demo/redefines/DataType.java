package com.cobol.demo.redefines;

public class DataType {
    private char type;
    private String displayValue;
    private double compValue;

    public static final char TYPE_DISPLAY = 'D';
    public static final char TYPE_COMP = 'C';

    public DataType() {
    }

    public char getType() {
        return type;
    }

    public void setType(char type) {
        this.type = type;
    }

    public boolean isDisplayType() {
        return type == TYPE_DISPLAY;
    }

    public boolean isCompType() {
        return type == TYPE_COMP;
    }

    public String getDisplayValue() {
        return displayValue;
    }

    public void setDisplayValue(String displayValue) {
        this.displayValue = displayValue;
    }

    public double getCompValue() {
        return compValue;
    }

    public void setCompValue(double compValue) {
        this.compValue = compValue;
    }
}
