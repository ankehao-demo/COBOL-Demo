package com.coboldemo.serialization;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.StringWriter;

/**
 * Migrated from: xml_generate/xml_generate.cbl
 *
 * Demonstrates COBOL XML GENERATE command. Maps to JAXB marshalling:
 * - TYPE OF ... IS ATTRIBUTE -> @XmlAttribute
 * - SUPPRESS WHEN SPACES -> conditional null-check (empty fields excluded)
 * - WITH XML-DECLARATION -> Marshaller.JAXB_FRAGMENT = false
 */
public class XmlGenerateExample {

    @XmlRootElement(name = "ws-record")
    public static class Record {
        private String name;
        private String value;
        private String blank;
        private String enabled;

        public Record() {}

        public Record(String name, String value, String blank, String enabled) {
            this.name = name;
            this.value = value;
            this.blank = blank;
            this.enabled = enabled;
        }

        @XmlElement(name = "name")
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        @XmlElement(name = "value")
        public String getValue() { return value; }
        public void setValue(String value) { this.value = value; }

        // SUPPRESS WHEN SPACES: return null if blank so JAXB omits it
        @XmlElement(name = "ws-record-blank", nillable = false)
        public String getBlank() {
            if (blank == null || blank.isBlank()) return null;
            return blank;
        }
        public void setBlank(String blank) { this.blank = blank; }

        // TYPE OF ws-record-flag IS ATTRIBUTE
        @XmlAttribute(name = "enabled")
        public String getEnabled() { return enabled; }
        public void setEnabled(String enabled) { this.enabled = enabled; }
    }

    public static void main(String[] args) {
        Record record = new Record("Test Name", "Test Value", "", "true");

        try {
            JAXBContext context = JAXBContext.newInstance(Record.class);
            Marshaller marshaller = context.createMarshaller();

            // WITH XML-DECLARATION: include the XML declaration
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, false);
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);

            StringWriter writer = new StringWriter();
            marshaller.marshal(record, writer);
            String xmlOutput = writer.toString();
            int charCount = xmlOutput.length();

            System.out.println("XML document successfully generated.");
            System.out.println("Generated xml for record: " + record.getName()
                    + record.getValue() + (record.getBlank() != null ? record.getBlank() : "")
                    + record.getEnabled());
            System.out.println("----------------------------");
            System.out.println(xmlOutput);
            System.out.println("----------------------------");
            System.out.println("XML output character count: " + charCount);
            System.out.println("Done.");
        } catch (JAXBException e) {
            System.out.println("Error generating XML: " + e.getMessage());
        }
    }
}
