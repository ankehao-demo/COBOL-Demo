package com.cobol.serialization;

/**
 * Entry point that creates a sample Record and runs both JSON and XML serializers,
 * mirroring the COBOL display statements from json_generate.cbl and xml_generate.cbl.
 *
 * Sample data matches the COBOL programs:
 *   move "Test Name" to ws-record-name
 *   move "Test Value" to ws-record-value
 *   set ws-record-flag-enabled to true
 *   (ws-record-blank is left as spaces / blank)
 */
public class Main {

    public static void main(String[] args) {
        // Create a sample Record matching the COBOL test data
        // ws-record-blank is set to a single space to represent COBOL's blank/spaces field
        Record record = new Record("Test Name", "Test Value", " ", true);

        System.out.println("=== JSON Serialization ===");
        JsonSerializer jsonSerializer = new JsonSerializer();
        jsonSerializer.serialize(record);

        System.out.println();

        System.out.println("=== XML Serialization ===");
        XmlSerializer xmlSerializer = new XmlSerializer();
        xmlSerializer.serialize(record);
    }
}
