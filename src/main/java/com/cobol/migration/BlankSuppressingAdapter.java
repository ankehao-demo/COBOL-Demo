package com.cobol.migration;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * JAXB XmlAdapter that suppresses XML elements when their value is blank (empty or whitespace).
 *
 * This replicates the COBOL behavior of:
 *     SUPPRESS WHEN SPACES
 *
 * When marshalling, if the string value is null or contains only whitespace,
 * this adapter returns null, which causes JAXB to omit the element from the XML output.
 */
public class BlankSuppressingAdapter extends XmlAdapter<String, String> {

    @Override
    public String unmarshal(String v) {
        return v;
    }

    @Override
    public String marshal(String v) {
        if (v == null || v.trim().isEmpty()) {
            return null; // suppresses the element in XML output
        }
        return v;
    }
}
