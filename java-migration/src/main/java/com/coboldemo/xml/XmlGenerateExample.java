package com.coboldemo.xml;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.StringWriter;

/**
 * Java port of xml_generate/xml_generate.cbl.
 *
 * Uses JAXB to marshal a POJO to XML. The original program's modifiers map
 * onto annotations / marshaller properties:
 * <ul>
 *   <li>{@code NAME OF ...} -> {@link XmlElement#name()}</li>
 *   <li>{@code TYPE OF ... IS ATTRIBUTE} -> {@link XmlAttribute}</li>
 *   <li>{@code SUPPRESS WHEN SPACES} -> assign {@code null} instead of an
 *       empty string; JAXB omits {@link XmlElement} fields whose value is
 *       {@code null} (the closest idiomatic Java equivalent).</li>
 *   <li>{@code WITH XML-DECLARATION} -> default marshaller behaviour.</li>
 * </ul>
 */
public final class XmlGenerateExample {

    private XmlGenerateExample() {
    }

    public static void main(String[] args) throws Exception {
        Record record = new Record();
        record.name = "Test Name";
        record.value = "Test Value";
        // SUPPRESS WHEN SPACES: null -> JAXB omits the element from output.
        record.blank = suppressBlank("");
        record.enabled = "true";

        JAXBContext context = JAXBContext.newInstance(Record.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, false);

        StringWriter writer = new StringWriter();
        marshaller.marshal(record, writer);
        String xml = writer.toString();

        System.out.println("XML document successfully generated.");
        System.out.println("Generated xml for record: " + record);
        System.out.println("----------------------------");
        System.out.println(xml);
        System.out.println("----------------------------");
        System.out.println("XML output character count: " + xml.length());
        System.out.println("Done.");
    }

    /**
     * Translates COBOL's {@code SUPPRESS WHEN SPACES}: an empty/whitespace
     * value becomes {@code null} so JAXB leaves the element out entirely.
     */
    private static String suppressBlank(String value) {
        return (value == null || value.isBlank()) ? null : value;
    }

    /** Mirrors ws-record in xml_generate.cbl. */
    @XmlRootElement(name = "ws-record")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static final class Record {

        @XmlAttribute(name = "enabled")
        private String enabled;

        @XmlElement(name = "name")
        private String name;

        @XmlElement(name = "value")
        private String value;

        @XmlElement(name = "ws-record-blank")
        private String blank;

        public String getEnabled() {
            return enabled;
        }

        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }

        public String getBlank() {
            return blank;
        }

        @Override
        public String toString() {
            return "Record{name='" + name + "', value='" + value
                    + "', blank='" + blank + "', enabled='" + enabled + "'}";
        }
    }
}
