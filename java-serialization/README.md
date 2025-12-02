# COBOL Data Serialization - Java Migration

This Java project provides equivalent functionality to the COBOL `XML GENERATE` and `JSON GENERATE` commands found in the COBOL-Demo repository.

## Overview

This migration replaces the following COBOL functionality with Java implementations:

| COBOL Feature | Java Implementation |
|---------------|---------------------|
| `XML GENERATE` | JAXB (Jakarta XML Binding) |
| `JSON GENERATE` | Jackson |
| `REDEFINES` clause | Inheritance/Composition patterns |
| `NAME OF` clause | `@JsonProperty` / `@XmlElement` annotations |
| `TYPE OF ... IS ATTRIBUTE` | `@XmlAttribute` annotation |
| `SUPPRESS WHEN SPACES` | Custom serialization logic |
| `COUNT IN` | `SerializationResult.getCharacterCount()` |
| `XML-CODE` / `JSON-CODE` | `SerializationResult.getErrorCode()` |

## Source COBOL Files

This migration is based on the following COBOL programs:

- `xml_generate/xml_generate.cbl` - XML serialization using libxml2
- `json_generate/json_generate.cbl` - JSON serialization using libjson-c
- `redifines/redefines.cbl` - REDEFINES clause examples

## Project Structure

```
java-serialization/
├── pom.xml                                    # Maven build configuration
├── README.md                                  # This file
└── src/
    ├── main/java/com/cobol/serialization/
    │   ├── Main.java                          # Demo application
    │   ├── model/
    │   │   ├── Record.java                    # ws-record equivalent
    │   │   ├── Customer.java                  # ws-customer base class
    │   │   ├── PersonCustomer.java            # Person customer (REDEFINES)
    │   │   ├── CorporateCustomer.java         # Corporate customer (REDEFINES)
    │   │   └── Address.java                   # ws-customer-address equivalent
    │   ├── xml/
    │   │   └── XmlSerializer.java             # XML GENERATE equivalent
    │   ├── json/
    │   │   └── JsonSerializer.java            # JSON GENERATE equivalent
    │   └── util/
    │       └── SerializationResult.java       # Result with count and error code
    └── test/java/com/cobol/serialization/
        ├── XmlSerializerTest.java             # XML serialization tests
        ├── JsonSerializerTest.java            # JSON serialization tests
        └── CustomerSerializationTest.java     # Customer model tests
```

## Building

```bash
cd java-serialization
mvn clean compile
```

## Running Tests

```bash
mvn test
```

## Running the Demo

```bash
mvn exec:java -Dexec.mainClass="com.cobol.serialization.Main"
```

Or after building:

```bash
mvn package
java -jar target/java-serialization-1.0.0.jar
```

## Usage Examples

### XML Serialization

```java
import com.cobol.serialization.model.Record;
import com.cobol.serialization.xml.XmlSerializer;
import com.cobol.serialization.util.SerializationResult;

Record record = new Record();
record.setName("Test Name");
record.setValue("Test Value");
record.setEnabledFlag(true);

XmlSerializer xmlSerializer = new XmlSerializer()
    .withXmlDeclaration(true)      // WITH XML-DECLARATION
    .withSuppressWhenSpaces(true); // SUPPRESS WHEN SPACES

SerializationResult result = xmlSerializer.serialize(record);

if (result.isSuccess()) {
    System.out.println(result.getOutput());
    System.out.println("Character count: " + result.getCharacterCount());
} else {
    System.out.println("Error code: " + result.getErrorCode());
}
```

### JSON Serialization

```java
import com.cobol.serialization.model.Record;
import com.cobol.serialization.json.JsonSerializer;
import com.cobol.serialization.util.SerializationResult;

Record record = new Record();
record.setName("Test Name");
record.setValue("Test Value");
record.setEnabledFlag(true);

JsonSerializer jsonSerializer = new JsonSerializer();

SerializationResult result = jsonSerializer.serialize(record);

if (result.isSuccess()) {
    System.out.println(result.getOutput());
    System.out.println("Character count: " + result.getCharacterCount());
} else {
    System.out.println("Error code: " + result.getErrorCode());
}
```

### Handling REDEFINES (Polymorphic Data)

```java
import com.cobol.serialization.model.*;

// Create a person customer (uses first/last name fields)
Address address1 = new Address("123 Main St", "CA", "90210");
Customer person = Customer.createPerson("John", "Doe", address1);

// Create a corporate customer (uses single corp name field - REDEFINES)
Address address2 = new Address("456 Corp Ave", "NY", "10001");
Customer corp = Customer.createCorporation("Acme Corp", address2);

// Serialize both using the same serializer
JsonSerializer serializer = new JsonSerializer();
System.out.println(serializer.serializeObject(person).getOutput());
System.out.println(serializer.serializeObject(corp).getOutput());
```

## COBOL to Java Mapping

### Record Structure (ws-record)

| COBOL Field | PIC | Java Field | Type |
|-------------|-----|------------|------|
| ws-record-name | X(10) | name | String |
| ws-record-value | X(10) | value | String |
| ws-record-blank | X(10) | blank | String |
| ws-record-flag | X(5) | enabled | String |

### Customer Structure (ws-customer with REDEFINES)

The COBOL `REDEFINES` clause allows the same memory location to be interpreted differently:

```cobol
05  ws-customer-name.
    10  ws-customer-first-name      pic x(10).
    10  ws-customer-last-name       pic x(20).
05  ws-corp-name redefines ws-customer-name pic x(30).
```

In Java, this is handled using inheritance:
- `Customer` - Abstract base class
- `PersonCustomer` - Has firstName and lastName fields
- `CorporateCustomer` - Has corpName field

## Dependencies

- **Jackson** (2.15.3) - JSON serialization
- **JAXB** (Jakarta XML Binding 4.0.4) - XML serialization
- **JUnit 5** (5.10.1) - Testing

## Requirements

- Java 8 or higher
- Maven 3.6 or higher
