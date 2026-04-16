package com.coboldemo.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Java migration of json_generate/json_generate.cbl
 *
 * Demonstrates JSON serialization using Jackson ObjectMapper,
 * mirroring the COBOL JSON GENERATE statement behavior.
 */
public class JsonGenerateExample {

    /**
     * POJO representing the COBOL ws-record structure.
     *
     * COBOL fields:
     *   05  ws-record-name    pic x(10)
     *   05  ws-record-value   pic x(10)
     *   05  ws-record-blank   pic x(10)
     *   05  ws-record-flag    pic x(5) value "false"
     *       88  ws-record-flag-enabled   value "true"
     *       88  ws-record-flag-disabled  value "false"
     *
     * The @JsonInclude(NON_EMPTY) annotation suppresses blank/empty fields,
     * mirroring the COBOL behavior where ws-record-blank (spaces) is omitted
     * from the generated JSON output.
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    static class WsRecord {

        @JsonProperty("name")
        private String wsRecordName;

        @JsonProperty("value")
        private String wsRecordValue;

        @JsonProperty("blank")
        private String wsRecordBlank;

        @JsonProperty("enabled")
        private String wsRecordFlag;

        public WsRecord() {
            // Default: flag is "false" (ws-record-flag-disabled)
            this.wsRecordFlag = "false";
            this.wsRecordBlank = "";
        }

        public String getWsRecordName() {
            return wsRecordName;
        }

        public void setWsRecordName(String wsRecordName) {
            this.wsRecordName = wsRecordName;
        }

        public String getWsRecordValue() {
            return wsRecordValue;
        }

        public void setWsRecordValue(String wsRecordValue) {
            this.wsRecordValue = wsRecordValue;
        }

        public String getWsRecordBlank() {
            return wsRecordBlank;
        }

        public void setWsRecordBlank(String wsRecordBlank) {
            this.wsRecordBlank = wsRecordBlank;
        }

        public String getWsRecordFlag() {
            return wsRecordFlag;
        }

        public void setWsRecordFlag(String wsRecordFlag) {
            this.wsRecordFlag = wsRecordFlag;
        }

        /** Mirrors: SET ws-record-flag-enabled TO TRUE */
        public void setEnabled(boolean enabled) {
            this.wsRecordFlag = enabled ? "true" : "false";
        }

        @Override
        public String toString() {
            return String.format("%-10s%-10s%-10s%-5s",
                    wsRecordName != null ? wsRecordName : "",
                    wsRecordValue != null ? wsRecordValue : "",
                    wsRecordBlank != null ? wsRecordBlank : "",
                    wsRecordFlag != null ? wsRecordFlag : "");
        }
    }

    public static void main(String[] args) {
        // Mirrors: MOVE "Test Name" TO ws-record-name
        //          MOVE "Test Value" TO ws-record-value
        //          SET ws-record-flag-enabled TO TRUE
        WsRecord wsRecord = new WsRecord();
        wsRecord.setWsRecordName("Test Name");
        wsRecord.setWsRecordValue("Test Value");
        // ws-record-blank is left empty (spaces in COBOL)
        wsRecord.setEnabled(true);

        try {
            ObjectMapper mapper = new ObjectMapper();

            // JSON GENERATE ws-json-output FROM ws-record
            String jsonOutput = mapper.writeValueAsString(wsRecord);
            int jsonCharCount = jsonOutput.length();

            // NOT ON EXCEPTION: display success
            System.out.println("JSON document successfully generated.");
            System.out.println("Generated JSON for record: " + wsRecord);
            System.out.println("----------------------------");
            System.out.println(jsonOutput);
            System.out.println("----------------------------");
            System.out.println("JSON output character count: " + jsonCharCount);
            System.out.println("Done.");
        } catch (Exception e) {
            // ON EXCEPTION: display error
            System.out.println("Error generating JSON error " + e.getMessage());
            System.exit(1);
        }
    }
}
