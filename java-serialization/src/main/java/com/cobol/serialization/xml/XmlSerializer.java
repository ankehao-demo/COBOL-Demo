package com.cobol.serialization.xml;

import com.cobol.serialization.model.Record;
import com.cobol.serialization.util.SerializationResult;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import java.io.StringWriter;

/**
 * XML serializer that replicates COBOL XML GENERATE functionality.
 * 
 * Supports the following COBOL features:
 * - XML declaration (WITH XML-DECLARATION)
 * - Field name mapping (NAME OF clause)
 * - Attribute generation (TYPE OF ... IS ATTRIBUTE)
 * - Suppress when spaces (SUPPRESS WHEN SPACES)
 * - Character count tracking (COUNT IN)
 * - Error handling (XML-CODE)
 */
public class XmlSerializer {

    public static final int ERROR_JAXB_CONTEXT = 1;
    public static final int ERROR_MARSHALLING = 2;
    public static final int ERROR_NULL_INPUT = 3;

    private boolean includeXmlDeclaration = true;
    private boolean suppressWhenSpaces = true;

    public XmlSerializer() {
    }

    public XmlSerializer withXmlDeclaration(boolean include) {
        this.includeXmlDeclaration = include;
        return this;
    }

    public XmlSerializer withSuppressWhenSpaces(boolean suppress) {
        this.suppressWhenSpaces = suppress;
        return this;
    }

    public SerializationResult serialize(Record record) {
        if (record == null) {
            return SerializationResult.error(ERROR_NULL_INPUT, "Input record is null");
        }

        try {
            Record recordToSerialize = prepareRecord(record);
            
            JAXBContext context = JAXBContext.newInstance(Record.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE);
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, !includeXmlDeclaration);

            StringWriter writer = new StringWriter();
            marshaller.marshal(recordToSerialize, writer);
            
            String output = writer.toString();
            return SerializationResult.success(output);

        } catch (JAXBException e) {
            return SerializationResult.error(ERROR_MARSHALLING, 
                    "Error generating XML: " + e.getMessage());
        }
    }

    public <T> SerializationResult serializeObject(T object, Class<T> clazz) {
        if (object == null) {
            return SerializationResult.error(ERROR_NULL_INPUT, "Input object is null");
        }

        try {
            JAXBContext context = JAXBContext.newInstance(clazz);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE);
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, !includeXmlDeclaration);

            StringWriter writer = new StringWriter();
            marshaller.marshal(object, writer);
            
            String output = writer.toString();
            return SerializationResult.success(output);

        } catch (JAXBException e) {
            return SerializationResult.error(ERROR_MARSHALLING, 
                    "Error generating XML: " + e.getMessage());
        }
    }

    private Record prepareRecord(Record original) {
        Record prepared = new Record();
        
        prepared.setName(original.getName());
        prepared.setValue(original.getValue());
        prepared.setEnabled(original.getEnabled());
        
        if (suppressWhenSpaces) {
            String blank = original.getBlank();
            if (blank == null || blank.trim().isEmpty()) {
                prepared.setBlank(null);
            } else {
                prepared.setBlank(blank);
            }
        } else {
            prepared.setBlank(original.getBlank());
        }
        
        return prepared;
    }

    public boolean isIncludeXmlDeclaration() {
        return includeXmlDeclaration;
    }

    public boolean isSuppressWhenSpaces() {
        return suppressWhenSpaces;
    }
}
