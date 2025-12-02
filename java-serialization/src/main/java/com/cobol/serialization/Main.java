package com.cobol.serialization;

import com.cobol.serialization.json.JsonSerializer;
import com.cobol.serialization.model.Address;
import com.cobol.serialization.model.CorporateCustomer;
import com.cobol.serialization.model.Customer;
import com.cobol.serialization.model.PersonCustomer;
import com.cobol.serialization.model.Record;
import com.cobol.serialization.util.SerializationResult;
import com.cobol.serialization.xml.XmlSerializer;

/**
 * Main class demonstrating the Java migration of COBOL data serialization.
 * 
 * This replicates the functionality of:
 * - xml_generate/xml_generate.cbl
 * - json_generate/json_generate.cbl
 * - redifines/redefines.cbl (for data structures)
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("COBOL Data Serialization - Java Migration");
        System.out.println("==========================================");
        System.out.println();

        demonstrateXmlGeneration();
        System.out.println();
        demonstrateJsonGeneration();
        System.out.println();
        demonstrateCustomerSerialization();
    }

    private static void demonstrateXmlGeneration() {
        System.out.println("XML Generation (equivalent to xml_generate.cbl)");
        System.out.println("-----------------------------------------------");

        Record record = new Record();
        record.setName("Test Name");
        record.setValue("Test Value");
        record.setBlank("");
        record.setEnabledFlag(true);

        XmlSerializer xmlSerializer = new XmlSerializer()
                .withXmlDeclaration(true)
                .withSuppressWhenSpaces(true);

        SerializationResult result = xmlSerializer.serialize(record);

        if (result.isSuccess()) {
            System.out.println("XML document successfully generated.");
            System.out.println("Generated XML for record: " + record);
            System.out.println("----------------------------");
            System.out.println(result.getOutput());
            System.out.println("----------------------------");
            System.out.println("XML output character count: " + result.getCharacterCount());
        } else {
            System.out.println("Error generating XML error " + result.getErrorCode());
            System.out.println(result.getErrorMessage());
        }

        System.out.println("Done.");
    }

    private static void demonstrateJsonGeneration() {
        System.out.println("JSON Generation (equivalent to json_generate.cbl)");
        System.out.println("-------------------------------------------------");

        Record record = new Record();
        record.setName("Test Name");
        record.setValue("Test Value");
        record.setBlank("");
        record.setEnabledFlag(true);

        JsonSerializer jsonSerializer = new JsonSerializer();

        SerializationResult result = jsonSerializer.serialize(record);

        if (result.isSuccess()) {
            System.out.println("JSON document successfully generated.");
            System.out.println("Generated JSON for record: " + record);
            System.out.println("----------------------------");
            System.out.println(result.getOutput());
            System.out.println("----------------------------");
            System.out.println("JSON output character count: " + result.getCharacterCount());
        } else {
            System.out.println("Error generating JSON error " + result.getErrorCode());
            System.out.println(result.getErrorMessage());
        }

        System.out.println("Done.");
    }

    private static void demonstrateCustomerSerialization() {
        System.out.println("Customer Serialization (demonstrating REDEFINES handling)");
        System.out.println("---------------------------------------------------------");

        Address address1 = new Address("123 fake st", "NV", "12345");
        Customer person = Customer.createPerson("test-first", "test-last", address1);

        Address address2 = new Address("567 real st", "NY", "11795");
        Customer corp = Customer.createCorporation("no-name corp", address2);

        JsonSerializer jsonSerializer = new JsonSerializer().withPrettyPrint(true);

        System.out.println("Person Customer:");
        SerializationResult personResult = jsonSerializer.serializeObject(person);
        if (personResult.isSuccess()) {
            System.out.println(personResult.getOutput());
        }

        System.out.println();
        System.out.println("Corporate Customer:");
        SerializationResult corpResult = jsonSerializer.serializeObject(corp);
        if (corpResult.isSuccess()) {
            System.out.println(corpResult.getOutput());
        }

        XmlSerializer xmlSerializer = new XmlSerializer().withXmlDeclaration(true);

        System.out.println();
        System.out.println("Person Customer (XML):");
        SerializationResult personXmlResult = xmlSerializer.serializeObject(
                (PersonCustomer) person, PersonCustomer.class);
        if (personXmlResult.isSuccess()) {
            System.out.println(personXmlResult.getOutput());
        }

        System.out.println();
        System.out.println("Corporate Customer (XML):");
        SerializationResult corpXmlResult = xmlSerializer.serializeObject(
                (CorporateCustomer) corp, CorporateCustomer.class);
        if (corpXmlResult.isSuccess()) {
            System.out.println(corpXmlResult.getOutput());
        }

        System.out.println();
        System.out.println("Done.");
    }
}
