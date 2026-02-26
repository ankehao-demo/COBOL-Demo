package com.cobol.migration;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;

/**
 * Java equivalent of the COBOL XML GENERATE program (xml_generate/xml_generate.cbl).
 *
 * Replicates the following COBOL logic:
 *
 *     move "Test Name" to ws-record-name
 *     move "Test Value" to ws-record-value
 *     set ws-record-flag-enabled to true
 *
 *     xml generate ws-xml-output
 *         from ws-record
 *         count in ws-xml-char-count
 *         with xml-declaration
 *         name of
 *             ws-record-name is "name",
 *             ws-record-value is "value",
 *             ws-record-flag is "enabled"
 *         type of ws-record-flag is attribute
 *         suppress when spaces
 *         on exception
 *             display "Error generating xml error " XML-CODE
 *             stop run
 *         not on exception
 *             display "XML document successfully generated."
 *     end-xml
 */
public class XmlSerializer {

    public static void main(String[] args) {
        try {
            JAXBContext context = JAXBContext.newInstance(WsRecord.class);
            Marshaller marshaller = context.createMarshaller();

            // JAXB_FORMATTED_OUTPUT for readable XML
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            // JAXB_FRAGMENT = false (default) includes the XML declaration
            // This is equivalent to COBOL's WITH XML-DECLARATION

            // Populate the record (equivalent to COBOL MOVE statements)
            WsRecord record = new WsRecord();
            record.setWsRecordName("Test Name");   // move "Test Name" to ws-record-name
            record.setWsRecordValue("Test Value");  // move "Test Value" to ws-record-value
            record.setFlagEnabled();                // set ws-record-flag-enabled to true
            // ws-record-blank is left unset (null) — equivalent to spaces in COBOL
            // BlankSuppressingAdapter will suppress this element in XML output

            // xml generate ws-xml-output from ws-record
            StringWriter writer = new StringWriter();
            marshaller.marshal(record, writer);
            String xmlOutput = writer.toString();

            // count in ws-xml-char-count
            int charCount = xmlOutput.length();

            // not on exception: display "XML document successfully generated."
            System.out.println("XML document successfully generated.");
            System.out.println(xmlOutput);
            System.out.println("Character count: " + charCount);
        } catch (JAXBException e) {
            // on exception: display "Error generating xml error " XML-CODE
            System.err.println("Error generating XML: " + e.getMessage());
        }
    }
}
