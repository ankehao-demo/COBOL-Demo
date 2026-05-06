package com.example.cobol.xmlgenerate;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.junit.jupiter.api.Test;

class XmlGenerateExampleTest {

    @Test
    void generatesXmlWithAttribute() throws Exception {
        XmlGenerateExample.Record r = new XmlGenerateExample.Record();
        r.name = "Test Name";
        r.value = "Test Value";
        r.enabled = "true";

        String body = new XmlMapper().writeValueAsString(r);
        assertTrue(body.contains("<name>Test Name</name>"), body);
        assertTrue(body.contains("<value>Test Value</value>"), body);
        assertTrue(body.contains("enabled=\"true\""), body);
    }
}
