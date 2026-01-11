# COBOL to Java Data Serialization Migration

This Java module provides equivalent functionality to COBOL's `XML GENERATE` and `JSON GENERATE` commands, enabling migration of COBOL data serialization logic to Java.

## Overview

The module replicates the following COBOL features:

### JSON Serialization (JsonSerializer)
- Field name mapping (equivalent to COBOL's `NAME OF` clause)
- Character count tracking (equivalent to `COUNT IN` clause)
- Blank field suppression
- Error handling with JSON-CODE equivalent error codes

### XML Serialization (XmlSerializer)
- XML declaration output (equivalent to `WITH XML-DECLARATION`)
- Field name mapping (equivalent to `NAME OF` clause)
- Attribute vs element distinction (equivalent to `TYPE OF ... ATTRIBUTE`)
- Space suppression (equivalent to `SUPPRESS WHEN SPACES`)
- Character count tracking (equivalent to `COUNT IN` clause)
- Error handling with XML-CODE equivalent error codes

## COBOL Data Structure

The Java `WsRecord` class mirrors the following COBOL record structure:

```cobol
01  ws-record.
    05  ws-record-name                  pic x(10).
    05  ws-record-value                 pic x(10).
    05  ws-record-blank                 pic x(10).
    05  ws-record-flag                  pic x(5) value "false".
        88  ws-record-flag-enabled      value "true".
        88  ws-record-flag-disabled     value "false".
```

## Usage

### JSON Generation

```java
import com.cobol.serialization.model.WsRecord;
import com.cobol.serialization.serializer.JsonSerializer;

WsRecord record = new WsRecord();
record.setName("Test Name");
record.setValue("Test Value");
record.setFlagEnabled(true);

JsonSerializer serializer = new JsonSerializer();
String json = serializer.generate(record);
int charCount = serializer.getCharCount();

// Output: {"ws-record":{"name":"Test Name","value":"Test Value","ws-record-blank":" ","enabled":"true"}}
```

### XML Generation

```java
import com.cobol.serialization.model.WsRecord;
import com.cobol.serialization.serializer.XmlSerializer;

WsRecord record = new WsRecord();
record.setName("Test Name");
record.setValue("Test Value");
record.setFlagEnabled(true);

XmlSerializer serializer = new XmlSerializer();
String xml = serializer.generate(record);
int charCount = serializer.getCharCount();

// Output: <?xml version="1.0"?><ws-record enabled="true"><name>Test Name</name><value>Test Value</value></ws-record>
```

## Building

```bash
cd java_serialization
mvn clean compile
```

## Testing

```bash
mvn test
```

## Dependencies

- Jackson (2.15.2) for JSON serialization
- JUnit 5 (5.10.0) for testing
- JAXB (included in Java 8) for XML serialization

## Project Structure

```
java_serialization/
├── pom.xml
├── README.md
└── src/
    ├── main/java/com/cobol/serialization/
    │   ├── model/
    │   │   └── WsRecord.java
    │   ├── serializer/
    │   │   ├── JsonSerializer.java
    │   │   └── XmlSerializer.java
    │   └── exception/
    │       ├── SerializationException.java
    │       ├── JsonSerializationException.java
    │       └── XmlSerializationException.java
    └── test/java/com/cobol/serialization/
        ├── WsRecordTest.java
        ├── JsonSerializerTest.java
        └── XmlSerializerTest.java
```

## Error Handling

The serializers throw custom exceptions that mirror COBOL's error codes:

### JsonSerializationException
- `ERROR_INVALID_DATA (1)`: Invalid input data
- `ERROR_BUFFER_TOO_SMALL (2)`: Output buffer too small
- `ERROR_INVALID_NAME_MAPPING (3)`: Invalid name mapping
- `ERROR_INTERNAL (4)`: Internal error

### XmlSerializationException
- `ERROR_INVALID_DATA (1)`: Invalid input data
- `ERROR_BUFFER_TOO_SMALL (2)`: Output buffer too small
- `ERROR_INVALID_NAME_MAPPING (3)`: Invalid name mapping
- `ERROR_INVALID_ATTRIBUTE (4)`: Invalid attribute specification
- `ERROR_INTERNAL (5)`: Internal error
