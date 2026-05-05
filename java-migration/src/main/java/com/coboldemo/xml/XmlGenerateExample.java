package com.coboldemo.xml;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 * Java port of {@code xml_generate/xml_generate.cbl}.
 *
 * Builds the same record as the JSON example and serialises it to XML
 * with an XML declaration. Empty fields are suppressed (analogue of
 * {@code SUPPRESS WHEN SPACES}) and the {@code enabled} flag is rendered
 * as an XML attribute, mirroring {@code TYPE OF ws-record-flag IS ATTRIBUTE}.
 */
public class XmlGenerateExample {

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JacksonXmlRootElement(localName = "ws-record")
    public static final class Record {
        @JacksonXmlProperty(localName = "name")
        public String name;

        @JacksonXmlProperty(localName = "value")
        public String value;

        @JacksonXmlProperty(localName = "blank")
        public String blank;

        @JacksonXmlProperty(isAttribute = true, localName = "enabled")
        public String enabled;
    }

    public static void main(String[] args) {
        Record record = new Record();
        record.name = "Test Name";
        record.value = "Test Value";
        record.enabled = "true";
        // blank is intentionally null so it is suppressed.

        XmlMapper mapper = new XmlMapper();
        String body;
        try {
            body = mapper.writeValueAsString(record);
        } catch (Exception e) {
            System.out.println("Error generating xml error " + e.getMessage());
            return;
        }
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + body;

        System.out.println("XML document successfully generated.");
        System.out.println("Generated xml for record: "
                + safe(record.name) + safe(record.value) + safe(record.blank) + safe(record.enabled));
        System.out.println("----------------------------");
        System.out.println(xml);
        System.out.println("----------------------------");
        System.out.println("XML output character count: " + xml.length());
        System.out.println("Done.");
    }

    private static String safe(String s) {
        return s == null ? "" : s;
    }
}
