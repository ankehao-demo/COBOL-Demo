# Java Data Serialization

Java implementation of COBOL data serialization programs (`xml_generate.cbl` and `json_generate.cbl`).

## Overview

This module provides Java equivalents of the COBOL XML and JSON generation programs, replicating their functionality using Jackson libraries.

## COBOL to Java Mapping

### Data Structure

| COBOL Field | COBOL Type | Java Field | Java Type |
|-------------|------------|------------|-----------|
| `ws-record-name` | `pic x(10)` | `name` | `String` |
| `ws-record-value` | `pic x(10)` | `value` | `String` |
| `ws-record-blank` | `pic x(10)` | `blank` | `String` |
| `ws-record-flag` | `pic x(5)` | `enabled` | `String` |

### Field Renaming (COBOL `NAME OF` clause)

- `ws-record-name` → `"name"`
- `ws-record-value` → `"value"`
- `ws-record-flag` → `"enabled"`

## Features

### XML Generation (`XmlGenerator.java`)

Equivalent to `xml_generate/xml_generate.cbl`:

- **XML Declaration**: Includes `<?xml version="1.0" encoding="UTF-8"?>`
- **Field Renaming**: Uses Jackson `@JacksonXmlProperty` annotations
- **Attribute Support**: The `enabled` field becomes an XML attribute
- **Suppress When Spaces**: Fields containing only spaces are omitted
- **Character Count**: Returns the length of generated XML
- **Error Handling**: Catches and reports serialization errors

### JSON Generation (`JsonGenerator.java`)

Equivalent to `json_generate/json_generate.cbl`:

- **Field Renaming**: Uses Jackson `@JsonProperty` annotations
- **Character Count**: Returns the length of generated JSON
- **Error Handling**: Catches and reports serialization errors

## Building

```bash
cd java_serialization
mvn clean compile
```

## Running

```bash
mvn exec:java
```

Or build and run the JAR:

```bash
mvn clean package
java -jar target/serialization-1.0.0.jar
```

## Example Output

```
============================================================
COBOL to Java Data Serialization Demo
============================================================

--- XML Generation (equivalent to xml_generate.cbl) ---

XML document successfully generated.
Generated XML for record: Record{name='Test Name', value='Test Value', blank='null', enabled='true'}
----------------------------
<?xml version='1.0' encoding='UTF-8'?>
<ws-record enabled="true">
  <name>Test Name</name>
  <value>Test Value</value>
</ws-record>
----------------------------
XML output character count: 123

--- JSON Generation (equivalent to json_generate.cbl) ---

JSON document successfully generated.
Generated JSON for record: Record{name='Test Name', value='Test Value', blank='null', enabled='true'}
----------------------------
{
  "name" : "Test Name",
  "value" : "Test Value",
  "ws-record-blank" : null,
  "enabled" : "true"
}
----------------------------
JSON output character count: 89

Done.
```

## Dependencies

- Jackson Databind (JSON serialization)
- Jackson Dataformat XML (XML serialization)
- Woodstox Core (XML declaration support)

## Requirements

- Java 11 or higher
- Maven 3.6 or higher
