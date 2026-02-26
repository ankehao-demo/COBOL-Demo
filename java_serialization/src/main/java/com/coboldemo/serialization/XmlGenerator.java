package com.coboldemo.serialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

/**
 * Java equivalent of xml_generate/xml_generate.cbl.
 *
 * Generates XML from a Record using Jackson XML, replicating the COBOL
 * XML GENERATE behavior including:
 * - with xml-declaration: includes {@code <?xml version="1.0"?>}
 * - name of: field renaming
 * - type of ws-record-flag is attribute: "enabled" as XML attribute
 * - suppress when spaces: omit ws-record-blank if it contains only spaces
 *
 * Expected output:
 *   {@code <?xml version="1.0"?><ws-record enabled="true"><name>Test Name</name><value>Test Value</value></ws-record>}
 */
public class XmlGenerator {

    public static void main(String[] args) {
        // Initialize record with same values as COBOL program
        // COBOL: move "Test Name" to ws-record-name
        // COBOL: move "Test Value" to ws-record-value
        // COBOL: set ws-record-flag-enabled to true
        Record record = new Record("Test Name", "Test Value", " ", true);

        try {
            XmlMapper xmlMapper = new XmlMapper();

            // Convert to XmlRecord (applies suppress when spaces logic)
            XmlRecord xmlRecord = XmlRecord.fromRecord(record);

            // Serialize to XML
            String xmlBody = xmlMapper.writeValueAsString(xmlRecord);

            // COBOL: with xml-declaration -> prepend XML declaration
            String xmlOutput = "<?xml version=\"1.0\"?>" + xmlBody;

            // COBOL: not on exception -> display "XML document successfully generated."
            System.out.println("XML document successfully generated.");

            // COBOL: display "Generated xml for record: " ws-record
            System.out.println("Generated xml for record: " + record);
            System.out.println("----------------------------");

            // COBOL: display function trim(ws-xml-output)
            System.out.println(xmlOutput);
            System.out.println("----------------------------");

            // COBOL: display "XML output character count: " ws-xml-char-count
            int charCount = xmlOutput.length();
            System.out.println("XML output character count: " + charCount);

            System.out.println("Done.");

        } catch (JsonProcessingException e) {
            // COBOL: on exception -> display "Error generating xml error " XML-CODE
            System.out.println("Error generating xml error " + e.getMessage());
            System.exit(1);
        }
    }
}
