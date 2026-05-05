package com.coboldemo.xml;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 * Migrated from xml_generate/xml_generate.cbl
 * Demonstrates XML GENERATE using Jackson XmlMapper.
 */
public class XmlGenerateExample {

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JacksonXmlRootElement(localName = "ws-record")
    static class Record {
        @JacksonXmlProperty(localName = "name")
        private String recordName;

        @JacksonXmlProperty(localName = "value")
        private String recordValue;

        @JacksonXmlProperty(localName = "blank")
        private String recordBlank;

        // COBOL: TYPE OF ws-record-flag IS ATTRIBUTE
        @JacksonXmlProperty(isAttribute = true, localName = "enabled")
        private String recordFlag;

        public Record() {}

        public String getRecordName() { return recordName; }
        public void setRecordName(String recordName) { this.recordName = recordName; }

        public String getRecordValue() { return recordValue; }
        public void setRecordValue(String recordValue) { this.recordValue = recordValue; }

        public String getRecordBlank() { return recordBlank; }
        public void setRecordBlank(String recordBlank) { this.recordBlank = recordBlank; }

        public String getRecordFlag() { return recordFlag; }
        public void setRecordFlag(String recordFlag) { this.recordFlag = recordFlag; }

        @Override
        public String toString() {
            return recordName + recordValue + recordBlank + recordFlag;
        }
    }

    public static void main(String[] args) {
        Record record = new Record();
        record.setRecordName("Test Name");
        record.setRecordValue("Test Value");
        record.setRecordBlank(""); // empty - suppressed by NON_EMPTY (SUPPRESS WHEN SPACES)
        record.setRecordFlag("true");

        XmlMapper xmlMapper = new XmlMapper();

        try {
            String xmlBody = xmlMapper.writeValueAsString(record);

            // Prepend XML declaration (WITH XML-DECLARATION)
            String xmlOutput = "<?xml version=\"1.0\"?>" + xmlBody;

            System.out.println("XML document successfully generated.");
            System.out.println("Generated xml for record: " + record);
            System.out.println("----------------------------");
            System.out.println(xmlOutput);
            System.out.println("----------------------------");
            System.out.println("XML output character count: " + xmlOutput.length());
            System.out.println("Done.");

        } catch (Exception e) {
            System.err.println("Error generating XML: " + e.getMessage());
            System.exit(1);
        }
    }
}
