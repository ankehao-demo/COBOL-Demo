package com.coboldemo.xml;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 * Java migration of xml_generate/xml_generate.cbl
 *
 * Demonstrates XML serialization using Jackson XmlMapper,
 * mirroring the COBOL XML GENERATE statement behavior including:
 *   - WITH XML-DECLARATION
 *   - NAME OF mappings
 *   - TYPE OF ws-record-flag IS ATTRIBUTE
 *   - SUPPRESS WHEN SPACES
 */
public class XmlGenerateExample {

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
     * @JacksonXmlRootElement maps the root to "ws-record" (COBOL group name).
     * @JsonInclude(NON_EMPTY) mirrors SUPPRESS WHEN SPACES.
     * @JacksonXmlProperty(isAttribute = true) mirrors TYPE OF ws-record-flag IS ATTRIBUTE.
     */
    @JacksonXmlRootElement(localName = "ws-record")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    static class WsRecord {

        @JacksonXmlProperty(localName = "name")
        private String wsRecordName;

        @JacksonXmlProperty(localName = "value")
        private String wsRecordValue;

        @JacksonXmlProperty(localName = "blank")
        private String wsRecordBlank;

        /** TYPE OF ws-record-flag IS ATTRIBUTE → isAttribute = true */
        @JacksonXmlProperty(localName = "enabled", isAttribute = true)
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
            XmlMapper xmlMapper = new XmlMapper();

            // XML GENERATE ws-xml-output FROM ws-record
            String xmlBody = xmlMapper.writeValueAsString(wsRecord);

            // WITH XML-DECLARATION: prepend the XML declaration header
            String xmlDeclaration = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
            String xmlOutput = xmlDeclaration + xmlBody;
            int xmlCharCount = xmlOutput.length();

            // NOT ON EXCEPTION: display success
            System.out.println("XML document successfully generated.");
            System.out.println("Generated xml for record: " + wsRecord);
            System.out.println("----------------------------");
            System.out.println(xmlOutput);
            System.out.println("----------------------------");
            System.out.println("XML output character count: " + xmlCharCount);
            System.out.println("Done.");
        } catch (Exception e) {
            // ON EXCEPTION: display error
            System.out.println("Error generating xml error " + e.getMessage());
            System.exit(1);
        }
    }
}
