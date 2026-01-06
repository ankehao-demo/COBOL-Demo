# Java Data Serialization

This directory contains Java implementations of the COBOL data serialization programs found in `json_generate/` and `xml_generate/`.

## Overview

The Java implementation replicates the functionality of the COBOL programs using Jackson for JSON and XML serialization.

### COBOL to Java Mapping

| COBOL Feature | Java Equivalent |
|---------------|-----------------|
| `JSON GENERATE` | `ObjectMapper.writeValueAsString()` |
| `XML GENERATE` | `XmlMapper.writeValueAsString()` |
| `NAME OF` clauses | `@JsonProperty` / `@JacksonXmlProperty` annotations |
| `COUNT IN` | `String.length()` |
| `WITH XML-DECLARATION` | `ToXmlGenerator.Feature.WRITE_XML_DECLARATION` |
| `TYPE OF ... IS ATTRIBUTE` | `@JacksonXmlProperty(isAttribute = true)` |
| `SUPPRESS WHEN SPACES` | `@JsonInclude(JsonInclude.Include.NON_EMPTY)` |
| `ON EXCEPTION` | `try-catch` blocks |

## Prerequisites

- Java 8 or higher
- Maven 3.x

## Building

```bash
cd java_serialization
mvn compile
```

## Running

### JSON Serialization Example

```bash
mvn exec:java -Dexec.mainClass="com.cobol.serialization.JsonGenerateExample"
```

### XML Serialization Example

```bash
mvn exec:java -Dexec.mainClass="com.cobol.serialization.XmlGenerateExample"
```

## Project Structure

```
java_serialization/
├── pom.xml                                    # Maven build configuration
├── README.md                                  # This file
└── src/main/java/com/cobol/serialization/
    ├── Record.java                            # Data class (POJO)
    ├── JsonGenerateExample.java               # JSON serialization demo
    └── XmlGenerateExample.java                # XML serialization demo
```

## Data Structure

The Java `Record` class maps to the COBOL `ws-record` structure:

```cobol
01  ws-record.
    05  ws-record-name                  pic x(10).
    05  ws-record-value                 pic x(10).
    05  ws-record-blank                 pic x(10).
    05  ws-record-flag                  pic x(5) value "false".
        88  ws-record-flag-enabled      value "true".
        88  ws-record-flag-disabled     value "false".
```

## Dependencies

- Jackson Databind (JSON serialization)
- Jackson Dataformat XML (XML serialization)
