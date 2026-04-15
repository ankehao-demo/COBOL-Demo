package com.coboldemo.xml;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.StringWriter;

/**
 * Java equivalent of xml_generate/xml_generate.cbl
 *
 * Demonstrates the COBOL XML GENERATE command using JAXB marshalling.
 *
 * COBOL Mapping:
 *   XML GENERATE ws-xml-output FROM ws-record     → JAXB Marshaller
 *   WITH XML-DECLARATION                          → JAXB_FRAGMENT = false
 *   NAME OF ws-record-name IS "name"              → @XmlElement(name = "name")
 *   TYPE OF ws-record-flag IS ATTRIBUTE            → @XmlAttribute
 *   SUPPRESS WHEN SPACES                          → null check (empty → null)
 *   ON EXCEPTION ... XML-CODE                     → try-catch(JAXBException)
 *   COUNT IN ws-xml-char-count                    → xmlString.length()
 */
public class XmlGenerateExample {

    /** Record POJO with JAXB annotations for XML element/attribute mapping. */
    @XmlRootElement(name = "ws-record")
    static class Record {
        @XmlElement(name = "name")
        String recordName;

        @XmlElement(name = "value")
        String recordValue;

        @XmlElement(name = "ws-record-blank", nillable = false)
        String recordBlank;  // SUPPRESS WHEN SPACES → set to null if blank

        @XmlAttribute(name = "enabled")
        String recordFlag;

        // Required no-arg constructor for JAXB
        Record() {}

        Record(String name, String value, String blank, String flag) {
            this.recordName = name;
            this.recordValue = value;
            // SUPPRESS WHEN SPACES: set to null if blank/empty
            this.recordBlank = (blank != null && !blank.isBlank()) ? blank : null;
            this.recordFlag = flag;
        }
    }

    public static void main(String[] args) {
        Record record = new Record("Test Name", "Test Value", "", "true");

        try {
            JAXBContext context = JAXBContext.newInstance(Record.class);
            Marshaller marshaller = context.createMarshaller();

            // WITH XML-DECLARATION → include <?xml version="1.0" ...?>
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, false);
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);

            StringWriter writer = new StringWriter();
            marshaller.marshal(record, writer);
            String xmlOutput = writer.toString();

            // NOT ON EXCEPTION
            System.out.println("XML document successfully generated.");

            // Display results
            String recordDisplay = pad(record.recordName, 10)
                    + pad(record.recordValue, 10)
                    + pad("", 10)
                    + pad(record.recordFlag, 5);
            System.out.println("Generated xml for record: " + recordDisplay);
            System.out.println("----------------------------");
            System.out.println(xmlOutput);
            System.out.println("----------------------------");

            // COUNT IN ws-xml-char-count
            System.out.println("XML output character count: " + xmlOutput.length());
            System.out.println("Done.");

        } catch (JAXBException e) {
            // ON EXCEPTION
            System.out.println("Error generating xml error " + e.getMessage());
        }
    }

    private static String pad(String s, int len) {
        if (s == null) s = "";
        return String.format("%-" + len + "s", s).substring(0, len);
    }
}
