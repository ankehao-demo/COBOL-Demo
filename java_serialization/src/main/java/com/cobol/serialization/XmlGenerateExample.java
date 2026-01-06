package com.cobol.serialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;

/**
 * Java equivalent of xml_generate/xml_generate.cbl
 * 
 * This class demonstrates XML serialization using Jackson XML,
 * replicating the COBOL XML GENERATE command functionality.
 * 
 * COBOL features replicated:
 * - XML GENERATE command -> XmlMapper.writeValueAsString()
 * - WITH XML-DECLARATION -> ToXmlGenerator.Feature.WRITE_XML_DECLARATION
 * - NAME OF clauses -> @JacksonXmlProperty annotations on Record class
 * - TYPE OF ... IS ATTRIBUTE -> @JacksonXmlProperty(isAttribute = true)
 * - SUPPRESS WHEN SPACES -> @JsonInclude(JsonInclude.Include.NON_EMPTY)
 * - COUNT IN -> String.length() to track character count
 * - ON EXCEPTION / NOT ON EXCEPTION -> try-catch blocks
 */
public class XmlGenerateExample {

    public static void main(String[] args) {
        Record record = new Record();
        record.setName("Test Name");
        record.setValue("Test Value");
        record.setEnabledFlag(true);

        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true);

        try {
            String xmlOutput = xmlMapper.writeValueAsString(record);
            int xmlCharCount = xmlOutput.length();

            System.out.println("XML document successfully generated.");
            System.out.println("Generated xml for record: " + record);
            System.out.println("----------------------------");
            System.out.println(xmlOutput.trim());
            System.out.println("----------------------------");
            System.out.println("XML output character count: " + xmlCharCount);
            System.out.println("Done.");

        } catch (JsonProcessingException e) {
            System.out.println("Error generating xml error " + e.getMessage());
            System.exit(1);
        }
    }
}
