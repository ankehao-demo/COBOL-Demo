package com.cobol.migration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * Java equivalent of the COBOL JSON GENERATE program (json_generate/json_generate.cbl).
 *
 * Replicates the following COBOL logic:
 *
 *     move "Test Name" to ws-record-name
 *     move "Test Value" to ws-record-value
 *     set ws-record-flag-enabled to true
 *
 *     json generate ws-json-output
 *         from ws-record
 *         count in ws-json-char-count
 *         name of
 *             ws-record-name is "name",
 *             ws-record-value is "value",
 *             ws-record-flag is "enabled"
 *         on exception
 *             display "Error generating JSON error " JSON-CODE
 *             stop run
 *         not on exception
 *             display "JSON document successfully generated."
 *     end-json
 */
public class JsonSerializer {

    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper();
        // Enable root wrapping to produce {"ws-record":{...}} matching COBOL JSON GENERATE output
        mapper.enable(SerializationFeature.WRAP_ROOT_VALUE);

        // Populate the record (equivalent to COBOL MOVE statements)
        WsRecord record = new WsRecord();
        record.setWsRecordName("Test Name");   // move "Test Name" to ws-record-name
        record.setWsRecordValue("Test Value");  // move "Test Value" to ws-record-value
        record.setFlagEnabled();                // set ws-record-flag-enabled to true
        // In COBOL, PIC X(10) fields default to spaces. JSON GENERATE includes blank fields.
        record.setWsRecordBlank("          ");  // 10 spaces, matching COBOL PIC X(10) default

        try {
            // json generate ws-json-output from ws-record
            String jsonOutput = mapper.writeValueAsString(record);

            // count in ws-json-char-count
            int charCount = jsonOutput.length();

            // not on exception: display "JSON document successfully generated."
            System.out.println("JSON document successfully generated.");
            System.out.println(jsonOutput);
            System.out.println("Character count: " + charCount);
        } catch (JsonProcessingException e) {
            // on exception: display "Error generating JSON error " JSON-CODE
            System.err.println("Error generating JSON: " + e.getMessage());
        }
    }
}
