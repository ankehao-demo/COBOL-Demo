package com.coboldemo.xml;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.StringWriter;

/**
 * Port of {@code xml_generate/xml_generate.cbl} — generates an XML document
 * for a single record using JAXB. The COBOL program used {@code XML
 * GENERATE ... NAME OF ... TYPE OF ... IS ATTRIBUTE SUPPRESS WHEN SPACES}
 * which maps to {@link XmlElement}, {@link XmlAttribute} and leaving
 * empty fields null so JAXB omits them.
 */
public final class XmlGenerateExample {

    private XmlGenerateExample() {
    }

    @XmlRootElement(name = "ws-record")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static final class Record {

        @XmlElement(name = "name")
        private String name;

        @XmlElement(name = "value")
        private String value;

        @XmlElement(name = "blank")
        private String blank;

        /** Emitted as an attribute — matches COBOL {@code TYPE OF ... IS ATTRIBUTE}. */
        @XmlAttribute(name = "enabled")
        private String enabled;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getBlank() {
            return blank;
        }

        public void setBlank(String blank) {
            this.blank = blank;
        }

        public String getEnabled() {
            return enabled;
        }

        public void setEnabled(String enabled) {
            this.enabled = enabled;
        }
    }

    public static void main(String[] args) throws JAXBException {
        Record record = new Record();
        record.setName("Test Name");
        record.setValue("Test Value");
        // blank is intentionally null / empty — SUPPRESS WHEN SPACES.
        record.setEnabled("true");

        JAXBContext context = JAXBContext.newInstance(Record.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.FALSE);
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE);

        StringWriter writer = new StringWriter();
        marshaller.marshal(record, writer);
        String xml = writer.toString();
        System.out.println("XML document successfully generated.");
        System.out.println("Generated xml for record: " + record.getName() + "/" + record.getValue()
                + "/" + record.getEnabled());
        System.out.println("----------------------------");
        System.out.println(xml);
        System.out.println("----------------------------");
        System.out.println("XML output character count: " + xml.length());
        System.out.println("Done.");
    }
}
