package com.cobol.demo.xml;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import java.io.StringWriter;

public class XmlGenerateExample {
    public static void main(String[] args) {
        try {
            XmlRecord record = new XmlRecord();
            record.setName("Test Name");
            record.setValue("Test Value");
            record.setEnabled(true);

            JAXBContext jaxbContext = JAXBContext.newInstance(XmlRecord.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");

            StringWriter stringWriter = new StringWriter();
            marshaller.marshal(record, stringWriter);
            
            String xmlOutput = stringWriter.toString();
            int xmlCharCount = xmlOutput.length();

            System.out.println("XML document successfully generated.");
            System.out.println("Generated xml for record: " + record.getName() + " " + record.getValue());
            System.out.println("----------------------------");
            System.out.println(xmlOutput.trim());
            System.out.println("----------------------------");
            System.out.println("XML output character count: " + xmlCharCount);
            System.out.println("Done.");

        } catch (Exception e) {
            System.err.println("Error generating xml: " + e.getMessage());
            System.exit(1);
        }
    }
}
