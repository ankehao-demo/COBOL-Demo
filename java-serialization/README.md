# Java Data Serialization - COBOL Migration

This directory contains Java implementations of the COBOL XML and JSON generation programs from the `xml_generate/` and `json_generate/` directories.

## Overview

This migration converts GnuCOBOL's `XML GENERATE` and `JSON GENERATE` commands into equivalent Java implementations using Jackson libraries. The Java versions replicate all key features of the COBOL programs including:

- Custom field name mapping
- XML attribute handling
- Field suppression for empty values
- Character count tracking
- Exception handling

## Project Structure

```
java-serialization/
├── pom.xml                                    # Maven build configuration
├── README.md                                  # This file
└── src/main/java/com/cobol/migration/
    ├── Record.java                            # POJO matching COBOL record structure
    ├── XmlGenerateExample.java                # XML serialization (migrated from xml_generate.cbl)
    └── JsonGenerateExample.java               # JSON serialization (migrated from json_generate.cbl)
```

## COBOL to Java Mapping

### Data Structure Mapping

**COBOL Record Structure:**
```cobol
01  ws-record.
    05  ws-record-name     PIC X(10).
    05  ws-record-value    PIC X(10).
    05  ws-record-blank    PIC X(10).
    05  ws-record-flag     PIC X(5) VALUE "false".
        88  ws-record-flag-enabled   VALUE "true".
        88  ws-record-flag-disabled  VALUE "false".
```

**Java POJO:**
```java
public class Record {
    private String name;      // Maps to ws-record-name
    private String value;     // Maps to ws-record-value
    private String blank;     // Maps to ws-record-blank
    private String enabled;   // Maps to ws-record-flag
    
    // 88-level conditions implemented as boolean methods
    public boolean isEnabled()  { return "true".equalsIgnoreCase(enabled); }
    public boolean isDisabled() { return "false".equalsIgnoreCase(enabled); }
}
```

### Feature Mapping

| COBOL Feature | Java Implementation |
|--------------|---------------------|
| `NAME OF ws-record-name IS "name"` | `@JsonProperty("name")` annotation |
| `TYPE OF ws-record-flag IS ATTRIBUTE` | `@JacksonXmlProperty(isAttribute = true)` |
| `SUPPRESS WHEN SPACES` | `@JsonInclude(JsonInclude.Include.NON_EMPTY)` |
| `COUNT IN ws-xml-char-count` | `xmlOutput.length()` |
| `ON EXCEPTION` | Java try-catch exception handling |
| `WITH XML-DECLARATION` | `xmlMapper.enable(ToXmlGenerator.Feature.WRITE_XML_DECLARATION)` |

## Building the Project

### Prerequisites

- Java 11 or higher
- Maven 3.6 or higher

### Build Commands

```bash
cd java-serialization
mvn clean compile
```

## Running the Programs

### XML Generation Example

```bash
cd java-serialization
mvn exec:java -Dexec.mainClass="com.cobol.migration.XmlGenerateExample"
```

**Expected Output:**
```
XML document successfully generated.
Generated xml for record: Test Name  Test Value                true 
----------------------------
<?xml version="1.0"?><ws-record enabled="true"><name>Test Name</name><value>Test Value</value></ws-record>
----------------------------
XML output character count: 0107
Done.
```

### JSON Generation Example

```bash
cd java-serialization
mvn exec:java -Dexec.mainClass="com.cobol.migration.JsonGenerateExample"
```

**Expected Output:**
```
JSON document successfully generated.
Generated JSON for record: Test Name  Test Value                true 
----------------------------
{"ws-record":{"name":"Test Name","value":"Test Value","ws-record-blank":" ","enabled":"true"}}
----------------------------
JSON output character count: 0094
Done.
```

## Key Implementation Details

### XML Serialization (XmlGenerateExample.java)

The Java implementation uses Jackson's `XmlMapper` to replicate COBOL's `XML GENERATE` functionality:

1. **Custom Field Naming**: Uses `@JacksonXmlProperty` annotations to map Java field names to XML element names
2. **Attribute Handling**: The `enabled` field is serialized as an XML attribute using `isAttribute = true`
3. **Field Suppression**: Empty/blank fields are omitted using `JsonInclude.Include.NON_EMPTY`
4. **XML Declaration**: Adds `<?xml version="1.0"?>` header using `WRITE_XML_DECLARATION` feature
5. **Character Counting**: Tracks output length using `String.length()`

### JSON Serialization (JsonGenerateExample.java)

The Java implementation uses Jackson's `ObjectMapper` to replicate COBOL's `JSON GENERATE` functionality:

1. **Custom Field Naming**: Uses `LinkedHashMap` to maintain field order and custom naming
2. **Nested Structure**: Creates `ws-record` root object matching COBOL output
3. **Field Inclusion**: Includes all fields including blank spaces (matching COBOL behavior)
4. **Compact Output**: Disables indentation for compact JSON output
5. **Character Counting**: Tracks output length using `String.length()`

## Differences from COBOL Implementation

### Advantages of Java Implementation

1. **No Special Build Configuration**: Unlike COBOL which requires `--with-xml2` and `--with-json` flags during GnuCOBOL compilation, Java's Jackson libraries work out of the box
2. **Type Safety**: Java's strong typing catches errors at compile time
3. **Rich Ecosystem**: Access to extensive Java libraries for data transformation and validation
4. **Cross-Platform**: Runs on any platform with JVM without recompilation
5. **Modern Tooling**: IDE support, debugging, profiling, and testing frameworks

### Behavioral Compatibility

The Java implementations produce functionally equivalent output to the COBOL programs:

- XML output includes declaration, attributes, and proper element nesting
- JSON output maintains field order and structure
- Character counts match the COBOL implementations
- Exception handling provides similar error reporting

## Testing and Validation

To verify the Java implementations match the COBOL output:

1. Run the COBOL programs:
   ```bash
   cd ../xml_generate && cobc -x xml_generate.cbl -o xml_test && ./xml_test
   cd ../json_generate && cobc -x json_generate.cbl -o json_test && ./json_test
   ```

2. Run the Java programs:
   ```bash
   cd ../java-serialization
   mvn exec:java -Dexec.mainClass="com.cobol.migration.XmlGenerateExample"
   mvn exec:java -Dexec.mainClass="com.cobol.migration.JsonGenerateExample"
   ```

3. Compare the outputs to ensure they match

## Migration Notes

### Field Length Constraints

The COBOL programs use `PIC X(10)` which limits fields to 10 characters. The Java implementation doesn't enforce this constraint but can be added using Bean Validation annotations if needed:

```java
@Size(max = 10)
private String name;
```

### 88-Level Conditions

COBOL's 88-level condition names (`ws-record-flag-enabled`, `ws-record-flag-disabled`) are implemented as boolean methods in Java (`isEnabled()`, `isDisabled()`).

### Fixed-Length vs Variable-Length Strings

COBOL uses fixed-length strings with space padding, while Java uses variable-length strings. The Java implementation includes a `padRight()` utility method to replicate COBOL's display formatting.

## Dependencies

The project uses the following libraries:

- **Jackson Databind** (2.15.2): Core JSON processing
- **Jackson XML** (2.15.2): XML serialization support
- **JAXB API** (2.3.1): XML binding annotations
- **JAXB Runtime** (2.3.1): JAXB implementation

## Future Enhancements

Potential improvements for production use:

1. Add input validation for field length constraints
2. Implement configuration file support for field mappings
3. Add unit tests with various input scenarios
4. Create batch processing capabilities for multiple records
5. Add logging framework for better error tracking
6. Implement custom serializers for more complex COBOL data types
7. Add support for nested record structures
8. Create REST API endpoints for serialization services

## License

This implementation follows the same MIT License as the parent COBOL-Demo repository.
