package com.example.cobol.xmlgenerate;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 * Java port of {@code xml_generate/xml_generate.cbl}.
 *
 * <p>Generates an XML document from a record using Jackson's
 * {@link XmlMapper}. The COBOL {@code WITH XML-DECLARATION} clause maps to
 * prefixing the output with the standard XML 1.0 declaration; the
 * {@code TYPE OF foo IS ATTRIBUTE} clause maps to
 * {@link JacksonXmlProperty#isAttribute()}.
 */
public final class XmlGenerateExample {

    @JacksonXmlRootElement(localName = "wsRecord")
    @JsonPropertyOrder({"name", "value", "wsRecordBlank"})
    public static final class Record {
        @JsonProperty("name")
        public String name;

        @JsonProperty("value")
        public String value;

        @JsonProperty("wsRecordBlank")
        public String wsRecordBlank = "";

        @JacksonXmlProperty(isAttribute = true, localName = "enabled")
        public String enabled;
    }

    private XmlGenerateExample() {}

    public static void main(String[] args) throws Exception {
        Record r = new Record();
        r.name = "Test Name";
        r.value = "Test Value";
        r.enabled = "true";

        XmlMapper mapper = new XmlMapper();
        String body = mapper.writeValueAsString(r);
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + body;

        System.out.println("Generated xml for record: name=" + r.name
                + ", value=" + r.value + ", enabled=" + r.enabled);
        System.out.println("----------------------------");
        System.out.println(xml);
        System.out.println("----------------------------");
        System.out.println("XML output character count: " + xml.length());
        System.out.println("Done.");
    }
}
