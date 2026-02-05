package com.cobol.serialization;

/**
 * Data Serialization Demo - Java equivalent of COBOL xml_generate.cbl and json_generate.cbl
 * 
 * This class demonstrates the Java implementation of COBOL data serialization,
 * replicating the functionality of both COBOL programs:
 * 
 * 1. XML Generation (xml_generate/xml_generate.cbl):
 *    - Generates XML with declaration
 *    - Renames fields using NAME OF clause
 *    - Makes flag field an XML attribute
 *    - Suppresses fields when they contain only spaces
 *    - Tracks character count
 *    - Handles exceptions
 * 
 * 2. JSON Generation (json_generate/json_generate.cbl):
 *    - Generates JSON
 *    - Renames fields using NAME OF clause
 *    - Tracks character count
 *    - Handles exceptions
 * 
 * COBOL equivalent procedure:
 * <pre>
 *     move "Test Name" to ws-record-name
 *     move "Test Value" to ws-record-value
 *     set ws-record-flag-enabled to true
 * </pre>
 */
public class DataSerializationDemo {

    public static void main(String[] args) {
        Record record = new Record();
        record.setName("Test Name");
        record.setValue("Test Value");
        record.setBlank(" ");
        record.setEnabled(true);

        System.out.println(repeatChar('=', 60));
        System.out.println("COBOL to Java Data Serialization Demo");
        System.out.println(repeatChar('=', 60));
        System.out.println();

        demonstrateXmlGeneration(record);
        System.out.println();
        demonstrateJsonGeneration(record);

        System.out.println();
        System.out.println("Done.");
    }

    /**
     * Demonstrates XML generation equivalent to xml_generate.cbl
     */
    private static void demonstrateXmlGeneration(Record record) {
        System.out.println("--- XML Generation (equivalent to xml_generate.cbl) ---");
        System.out.println();

        XmlGenerator xmlGenerator = new XmlGenerator();
        XmlGenerator.XmlResult xmlResult = xmlGenerator.generate(record);

        System.out.println("Generated XML for record: " + record);
        System.out.println("----------------------------");
        System.out.println(xmlResult.getXmlOutput().trim());
        System.out.println("----------------------------");
        System.out.println("XML output character count: " + xmlResult.getCharCount());
    }

    /**
     * Demonstrates JSON generation equivalent to json_generate.cbl
     */
    private static void demonstrateJsonGeneration(Record record) {
        System.out.println("--- JSON Generation (equivalent to json_generate.cbl) ---");
        System.out.println();

        JsonGenerator jsonGenerator = new JsonGenerator();
        JsonGenerator.JsonResult jsonResult = jsonGenerator.generate(record);

        System.out.println("Generated JSON for record: " + record);
        System.out.println("----------------------------");
        System.out.println(jsonResult.getJsonOutput().trim());
        System.out.println("----------------------------");
        System.out.println("JSON output character count: " + jsonResult.getCharCount());
    }

    private static String repeatChar(char c, int count) {
        StringBuilder sb = new StringBuilder(count);
        for (int i = 0; i < count; i++) {
            sb.append(c);
        }
        return sb.toString();
    }
}
