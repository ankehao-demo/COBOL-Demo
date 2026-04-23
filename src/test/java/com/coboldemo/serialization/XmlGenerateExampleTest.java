package com.coboldemo.serialization;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import org.junit.jupiter.api.Test;

import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.*;

class XmlGenerateExampleTest {

    @Test
    void recordSerializesToXml() throws Exception {
        XmlGenerateExample.Record record = new XmlGenerateExample.Record(
                "Test Name", "Test Value", "", "true");

        JAXBContext context = JAXBContext.newInstance(XmlGenerateExample.Record.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, false);

        StringWriter writer = new StringWriter();
        marshaller.marshal(record, writer);
        String xml = writer.toString();

        assertTrue(xml.contains("<?xml"));
        assertTrue(xml.contains("ws-record"));
        assertTrue(xml.contains("enabled=\"true\""));
        assertTrue(xml.contains("<name>Test Name</name>"));
        assertTrue(xml.contains("<value>Test Value</value>"));
        // Blank field should be suppressed (returns null when blank)
        assertFalse(xml.contains("ws-record-blank"));
    }

    @Test
    void nonBlankFieldIsIncluded() throws Exception {
        XmlGenerateExample.Record record = new XmlGenerateExample.Record(
                "N", "V", "Not Blank", "Y");

        JAXBContext context = JAXBContext.newInstance(XmlGenerateExample.Record.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);

        StringWriter writer = new StringWriter();
        marshaller.marshal(record, writer);
        String xml = writer.toString();

        assertTrue(xml.contains("Not Blank"));
    }
}
