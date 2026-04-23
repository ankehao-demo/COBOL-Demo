package com.coboldemo.xml;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import jakarta.xml.bind.annotation.adapters.XmlAdapter;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.StringWriter;

/**
 * Java port of xml_generate/xml_generate.cbl.
 *
 * Uses JAXB to marshal a POJO to XML. The original program's modifiers map
 * onto annotations / marshaller properties:
 * <ul>
 *   <li>{@code NAME OF ...} -> {@link XmlElement#name()}</li>
 *   <li>{@code TYPE OF ... IS ATTRIBUTE} -> {@link XmlAttribute}</li>
 *   <li>{@code SUPPRESS WHEN SPACES} -> a {@link XmlAdapter} that returns
 *       {@code null} for blank values so JAXB omits the element.</li>
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
        record.blank = ""; // suppressed on output
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
        @XmlJavaTypeAdapter(SuppressBlankAdapter.class)
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

        @XmlTransient
        @Override
        public String toString() {
            return "Record{name='" + name + "', value='" + value
                    + "', blank='" + blank + "', enabled='" + enabled + "'}";
        }
    }

    /** Maps empty strings to {@code null} so JAXB suppresses the element. */
    public static final class SuppressBlankAdapter
            extends XmlAdapter<String, String> {

        @Override
        public String unmarshal(String v) {
            return v;
        }

        @Override
        public String marshal(String v) {
            if (v == null || v.isEmpty()) {
                return null;
            }
            return v;
        }
    }
}
