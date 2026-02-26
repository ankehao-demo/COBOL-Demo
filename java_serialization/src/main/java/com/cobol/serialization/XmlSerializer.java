package com.cobol.serialization;

/**
 * Serializes a Record to XML, replicating the behavior of
 * xml_generate/xml_generate.cbl.
 *
 * Key behaviors from COBOL:
 * - XML declaration is included (with xml-declaration): {@code <?xml version="1.0"?>}
 * - Field name mappings via NAME OF clause:
 *     ws-record-name  -> "name"
 *     ws-record-value -> "value"
 *     ws-record-flag  -> "enabled"
 * - enabled is an XML attribute on the root element (type of ws-record-flag is attribute)
 * - ws-record-blank is suppressed when it contains only spaces (suppress when spaces)
 * - Error handling via try/catch mirrors COBOL's ON EXCEPTION / XML-CODE
 * - Character count tracked via String.length() (equivalent to COUNT IN ws-xml-char-count)
 */
public class XmlSerializer {

    /**
     * Serializes the given Record to XML and prints the output and character count.
     * If the blank field is null or contains only spaces, it is suppressed from the output.
     *
     * @param record the Record to serialize
     */
    public void serialize(Record record) {
        try {
            String xmlOutput = buildXml(record);

            int charCount = xmlOutput.length();

            System.out.println("XML document successfully generated.");
            System.out.println("Generated xml for record: " + record.getName()
                    + record.getValue()
                    + (record.getBlank() != null ? record.getBlank() : "")
                    + (record.isEnabled() ? "true" : "false"));
            System.out.println("----------------------------");
            System.out.println(xmlOutput);
            System.out.println("----------------------------");
            System.out.println("XML output character count: " + charCount);
            System.out.println("Done.");
        } catch (Exception e) {
            System.out.println("Error generating xml error " + e.getMessage());
        }
    }

    /**
     * Builds the XML string to exactly match the COBOL XML GENERATE output:
     * {@code <?xml version="1.0"?><ws-record enabled="true"><name>Test Name</name><value>Test Value</value></ws-record>}
     *
     * @param record the Record to convert to XML
     * @return the XML string
     */
    private String buildXml(Record record) {
        StringBuilder sb = new StringBuilder();

        // XML declaration (with xml-declaration)
        sb.append("<?xml version=\"1.0\"?>");

        // Root element with enabled as attribute (type of ws-record-flag is attribute)
        sb.append("<ws-record enabled=\"")
                .append(record.isEnabled() ? "true" : "false")
                .append("\">");

        // name element (ws-record-name renamed to "name")
        sb.append("<name>")
                .append(escapeXml(record.getName()))
                .append("</name>");

        // value element (ws-record-value renamed to "value")
        sb.append("<value>")
                .append(escapeXml(record.getValue()))
                .append("</value>");

        // ws-record-blank: suppress when spaces (only include if non-blank)
        String blank = record.getBlank();
        if (blank != null && !blank.trim().isEmpty()) {
            sb.append("<ws-record-blank>")
                    .append(escapeXml(blank))
                    .append("</ws-record-blank>");
        }

        sb.append("</ws-record>");

        return sb.toString();
    }

    /**
     * Escapes special XML characters in the given string.
     *
     * @param text the text to escape
     * @return the escaped text
     */
    private String escapeXml(String text) {
        if (text == null) {
            return "";
        }
        return text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }
}
