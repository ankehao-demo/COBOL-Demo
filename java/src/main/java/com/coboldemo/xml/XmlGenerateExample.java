package com.coboldemo.xml;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;

/**
 * Migrated from: xml_generate/xml_generate.cbl
 * Original author: Erik Eriksen (2022-06-15)
 * Purpose: Demonstrates XML GENERATE command equivalent using Jackson XML.
 *
 * Mapping:
 * - XML GENERATE -> XmlMapper.writeValueAsString()
 * - Field name mapping -> @JacksonXmlProperty annotations
 * - TYPE IS ATTRIBUTE -> @JacksonXmlProperty(isAttribute = true)
 * - SUPPRESS WHEN SPACES -> @JsonInclude(Include.NON_EMPTY)
 */
public class XmlGenerateExample {

    @JacksonXmlRootElement(localName = "Record")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    static class Record {
        @JacksonXmlProperty(localName = "Name")
        private String name;

        @JacksonXmlProperty(localName = "Value")
        private int value;

        @JacksonXmlProperty(localName = "Blank")
        private String blank;

        @JacksonXmlProperty(localName = "Enabled", isAttribute = true)
        private String enabled;

        public Record(String name, int value, String blank, String enabled) {
            this.name = name;
            this.value = value;
            this.blank = blank;
            this.enabled = enabled;
        }

        // Getters required for Jackson serialization
        public String getName() { return name; }
        public int getValue() { return value; }
        public String getBlank() { return blank; }
        public String getEnabled() { return enabled; }
    }

    public static void main(String[] args) throws JsonProcessingException {
        System.out.println("XML GENERATE Example");
        System.out.println("====================");
        System.out.println();

        // Create record matching COBOL data
        Record record = new Record("Test Record", 42, "", "Y");

        // Display record fields
        System.out.println("Record fields:");
        System.out.println("  Name:    '" + record.getName() + "'");
        System.out.println("  Value:   " + record.getValue());
        System.out.println("  Blank:   '" + record.getBlank() + "'");
        System.out.println("  Enabled: '" + record.getEnabled() + "'");
        System.out.println();

        // Generate XML
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.enable(com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT);
        xmlMapper.configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true);

        String xml = xmlMapper.writeValueAsString(record);
        System.out.println("Generated XML:");
        System.out.println(xml);
        System.out.println();

        // XML-CODE equivalent (0 = success)
        System.out.println("XML-CODE: 0 (success)");
        System.out.println("Length: " + xml.length());
    }
}
