package com.coboldemo.serialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * Java equivalent of json_generate/json_generate.cbl.
 *
 * Generates JSON from a Record using Jackson, replicating the COBOL
 * JSON GENERATE behavior including field renaming via NAME OF clause.
 *
 * Expected output:
 *   {"ws-record":{"name":"Test Name","value":"Test Value","ws-record-blank":" ","enabled":"true"}}
 */
public class JsonGenerator {

    public static void main(String[] args) {
        // Initialize record with same values as COBOL program
        // COBOL: move "Test Name" to ws-record-name
        // COBOL: move "Test Value" to ws-record-value
        // COBOL: set ws-record-flag-enabled to true
        Record record = new Record("Test Name", "Test Value", " ", true);

        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, true);

            // Convert to JsonRecord to match COBOL string representation of enabled field
            JsonRecord jsonRecord = JsonRecord.fromRecord(record);
            String jsonOutput = mapper.writeValueAsString(jsonRecord);

            // COBOL: not on exception -> display "JSON document successfully generated."
            System.out.println("JSON document successfully generated.");

            // COBOL: display "Generated JSON for record: " ws-record
            System.out.println("Generated JSON for record: " + record);
            System.out.println("----------------------------");

            // COBOL: display function trim(ws-json-output)
            System.out.println(jsonOutput);
            System.out.println("----------------------------");

            // COBOL: display "JSON output character count: " ws-json-char-count
            int charCount = jsonOutput.length();
            System.out.println("JSON output character count: " + charCount);

            System.out.println("Done.");

        } catch (JsonProcessingException e) {
            // COBOL: on exception -> display "Error generating JSON error " JSON-CODE
            System.out.println("Error generating JSON error " + e.getMessage());
            System.exit(1);
        }
    }
}
