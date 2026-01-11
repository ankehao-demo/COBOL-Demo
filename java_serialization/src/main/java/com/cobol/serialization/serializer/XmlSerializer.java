package com.cobol.serialization.serializer;

import com.cobol.serialization.exception.XmlSerializationException;
import com.cobol.serialization.model.WsRecord;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.io.StringWriter;

/**
 * XML serializer that replicates COBOL's XML GENERATE functionality.
 * 
 * COBOL equivalent:
 * <pre>
 * xml generate ws-xml-output
 *     from ws-record
 *     count in ws-xml-char-count
 *     with xml-declaration
 *     name of
 *         ws-record-name is "name",
 *         ws-record-value is "value",
 *         ws-record-flag is "enabled"
 *     type of ws-record-flag is attribute
 *     suppress when spaces
 *     on exception
 *         display "Error generating xml error " XML-CODE
 *         stop run
 *     not on exception
 *         display "XML document successfully generated."
 * end-xml
 * </pre>
 * 
 * Features implemented:
 * - XML declaration (WITH XML-DECLARATION)
 * - Field name mapping (NAME OF clause)
 * - Attribute vs element distinction (TYPE OF ... ATTRIBUTE)
 * - Space suppression (SUPPRESS WHEN SPACES)
 * - Character count tracking (COUNT IN clause)
 * - Error handling with XML-CODE equivalent
 */
public class XmlSerializer {
    
    private static final String ROOT_ELEMENT_NAME = "ws-record";
    private static final String FIELD_NAME_NAME = "name";
    private static final String FIELD_NAME_VALUE = "value";
    private static final String FIELD_NAME_FLAG = "enabled";
    
    private final DocumentBuilderFactory documentBuilderFactory;
    private final TransformerFactory transformerFactory;
    private int lastCharCount;
    
    public XmlSerializer() {
        this.documentBuilderFactory = DocumentBuilderFactory.newInstance();
        this.transformerFactory = TransformerFactory.newInstance();
        this.lastCharCount = 0;
    }
    
    /**
     * Generates XML from a WsRecord, replicating COBOL's XML GENERATE behavior.
     * Includes XML declaration and suppresses blank fields.
     * The flag field is rendered as an attribute (TYPE OF ... ATTRIBUTE).
     * 
     * @param record The WsRecord to serialize
     * @return The generated XML string
     * @throws XmlSerializationException if serialization fails
     */
    public String generate(WsRecord record) throws XmlSerializationException {
        return generate(record, true, true);
    }
    
    /**
     * Generates XML from a WsRecord with configurable options.
     * 
     * @param record The WsRecord to serialize
     * @param includeXmlDeclaration If true, include XML declaration (WITH XML-DECLARATION)
     * @param suppressWhenSpaces If true, suppress fields that are empty or contain only spaces (SUPPRESS WHEN SPACES)
     * @return The generated XML string
     * @throws XmlSerializationException if serialization fails
     */
    public String generate(WsRecord record, boolean includeXmlDeclaration, boolean suppressWhenSpaces) 
            throws XmlSerializationException {
        if (record == null) {
            throw new XmlSerializationException(
                "Cannot generate XML from null record",
                XmlSerializationException.ERROR_INVALID_DATA
            );
        }
        
        try {
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();
            
            Element rootElement = document.createElement(ROOT_ELEMENT_NAME);
            document.appendChild(rootElement);
            
            rootElement.setAttribute(FIELD_NAME_FLAG, record.getFlag());
            
            Element nameElement = document.createElement(FIELD_NAME_NAME);
            nameElement.setTextContent(record.getName());
            rootElement.appendChild(nameElement);
            
            Element valueElement = document.createElement(FIELD_NAME_VALUE);
            valueElement.setTextContent(record.getValue());
            rootElement.appendChild(valueElement);
            
            if (!suppressWhenSpaces || !record.isBlankEmpty()) {
                Element blankElement = document.createElement("ws-record-blank");
                blankElement.setTextContent(record.getBlank());
                rootElement.appendChild(blankElement);
            }
            
            String xml = transformToString(document, includeXmlDeclaration);
            this.lastCharCount = xml.length();
            
            return xml;
        } catch (ParserConfigurationException e) {
            throw new XmlSerializationException(
                "Error configuring XML parser: " + e.getMessage(),
                XmlSerializationException.ERROR_INTERNAL,
                e
            );
        } catch (TransformerException e) {
            throw new XmlSerializationException(
                "Error transforming XML: " + e.getMessage(),
                XmlSerializationException.ERROR_INTERNAL,
                e
            );
        }
    }
    
    /**
     * Returns the character count of the last generated XML.
     * Equivalent to COBOL's COUNT IN clause.
     * 
     * @return The character count
     */
    public int getCharCount() {
        return lastCharCount;
    }
    
    /**
     * Transforms the DOM document to a string.
     * Matches COBOL's XML output format with no indentation.
     */
    private String transformToString(Document document, boolean includeXmlDeclaration) 
            throws TransformerException {
        Transformer transformer = transformerFactory.newTransformer();
        
        if (includeXmlDeclaration) {
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.setOutputProperty(OutputKeys.VERSION, "1.0");
        } else {
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        }
        
        transformer.setOutputProperty(OutputKeys.INDENT, "no");
        
        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(document), new StreamResult(writer));
        
        return writer.toString();
    }
}
