# Java Serialization - COBOL Migration

## Overview

This directory contains Java implementations of the XML and JSON generation capabilities originally written in COBOL. These implementations provide equivalent functionality to the COBOL programs in `xml_generate/` and `json_generate/` directories, using modern Java serialization libraries.

## Purpose

This migration demonstrates how to convert COBOL data serialization functionality to Java, maintaining compatibility with the original COBOL output format while leveraging Java's standard libraries and ecosystem.

## Source COBOL Programs

The following COBOL programs have been migrated to Java:

1. **`../xml_generate/xml_generate.cbl`** - XML generation using GnuCOBOL's `XML GENERATE` command
2. **`../json_generate/json_generate.cbl`** - JSON generation using GnuCOBOL's `JSON GENERATE` command

## Java Implementation

### Architecture

The Java implementation consists of three main components:

1. **`Record.java`** - POJO (Plain Old Java Object) representing the COBOL record structure
2. **`XmlGenerateExample.java`** - XML serialization demonstration
3. **`JsonGenerateExample.java`** - JSON serialization demonstration

### COBOL to Java Data Structure Mapping

The COBOL record structure:

```cobol
01  ws-record.
    05  ws-record-name                  pic x(10).
    05  ws-record-value                 pic x(10).
    05  ws-record-blank                 pic x(10).
    05  ws-record-flag                  pic x(5) value "false".
        88  ws-record-flag-enabled      value "true".
        88  ws-record-flag-disabled     value "false".
```

Maps to the Java `Record` class:

```java
public class Record {
    private String name;      // PIC X(10) -> String (max 10 chars)
    private String value;     // PIC X(10) -> String (max 10 chars)
    private String blank;     // PIC X(10) -> String (max 10 chars)
    private String flag;      // PIC X(5)  -> String (max 5 chars)
    
    // 88-level conditions implemented as methods
    public void setEnabled(boolean enabled);
    public boolean isEnabled();
}
```

### Key Features Replicated from COBOL

#### XML Serialization Features

The Java implementation replicates these COBOL XML GENERATE features:

- **Custom field naming** (`NAME OF` clause): Uses Jackson `@JsonProperty` annotations
- **Attribute handling** (`TYPE OF ... IS ATTRIBUTE`): The `enabled` field is serialized as an XML attribute using `@JacksonXmlProperty(isAttribute = true)`
- **Suppress empty fields** (`SUPPRESS WHEN SPACES`): Configured via `JsonInclude.Include.NON_EMPTY`
- **XML declaration** (`WITH XML-DECLARATION`): Enabled via `ToXmlGenerator.Feature.WRITE_XML_DECLARATION`
- **Character counting** (`COUNT IN`): Tracks the length of generated XML string
- **Error handling** (`ON EXCEPTION`): Java try-catch blocks for serialization errors

#### JSON Serialization Features

The Java implementation replicates these COBOL JSON GENERATE features:

- **Custom field naming** (`NAME OF` clause): Uses Jackson `@JsonProperty` annotations
- **Field inclusion**: Unlike XML, JSON includes the blank field (matching COBOL behavior)
- **Character counting** (`COUNT IN`): Tracks the length of generated JSON string
- **Error handling** (`ON EXCEPTION`): Java try-catch blocks for serialization errors

### Library Selection

This implementation uses **Jackson** for both XML and JSON serialization:

- **Jackson Core** (`jackson-databind`): Core JSON serialization
- **Jackson XML** (`jackson-dataformat-xml`): XML serialization support
- **Jackson Annotations** (`jackson-annotations`): Serialization configuration

Jackson was chosen because:
- It provides both JSON and XML support with a consistent API
- It's the de facto standard for Java serialization
- It offers fine-grained control over output format
- It's actively maintained and well-documented

## Prerequisites

- **Java 11 or higher**
- **Maven 3.6 or higher**

## Building and Running

### Option 1: Using Maven (Recommended)

1. **Compile the project:**
   ```bash
   cd java_serialization
   mvn clean compile
   ```

2. **Run the XML generation example:**
   ```bash
   mvn exec:java -Dexec.mainClass="com.example.serialization.XmlGenerateExample"
   ```

3. **Run the JSON generation example:**
   ```bash
   mvn exec:java -Dexec.mainClass="com.example.serialization.JsonGenerateExample"
   ```

### Option 2: Manual Compilation

1. **Download Jackson dependencies** (or use Maven to download them first):
   ```bash
   mvn dependency:copy-dependencies -DoutputDirectory=lib
   ```

2. **Compile the Java files:**
   ```bash
   javac -cp "lib/*" Record.java XmlGenerateExample.java JsonGenerateExample.java
   ```

3. **Run the examples:**
   ```bash
   java -cp ".:lib/*" com.example.serialization.XmlGenerateExample
   java -cp ".:lib/*" com.example.serialization.JsonGenerateExample
   ```

## Expected Output

### XML Generation Example

```
XML document successfully generated.
Generated xml for record: Test Name Test Value          true 
----------------------------
<?xml version="1.0"?>
<ws-record enabled="true"><name>Test Name</name><value>Test Value</value></ws-record>
----------------------------
XML output character count: 0107
Done.
```

This matches the COBOL output from `xml_generate.cbl`.

### JSON Generation Example

```
JSON document successfully generated.
Generated JSON for record: Test Name Test Value          true 
----------------------------
{"ws-record":{"name":"Test Name","value":"Test Value","ws-record-blank":" ","enabled":"true"}}
----------------------------
JSON output character count: 0094
Done.
```

This matches the COBOL output from `json_generate.cbl`.

## Implementation Details

### Field Name Mapping

The COBOL `NAME OF` clause is implemented using Jackson annotations:

| COBOL Field Name | Serialized Name | Implementation |
|-----------------|-----------------|----------------|
| `ws-record-name` | `name` | `@JsonProperty("name")` |
| `ws-record-value` | `value` | `@JsonProperty("value")` |
| `ws-record-blank` | `ws-record-blank` | `@JsonProperty("ws-record-blank")` |
| `ws-record-flag` | `enabled` | `@JsonProperty("enabled")` |

### XML Attribute Handling

In COBOL, the `TYPE OF ws-record-flag IS ATTRIBUTE` clause makes the flag field an XML attribute. In Java, this is achieved with:

```java
@JacksonXmlProperty(isAttribute = true, localName = "enabled")
private String flag;
```

This produces: `<ws-record enabled="true">...</ws-record>`

### Field Suppression

COBOL's `SUPPRESS WHEN SPACES` is implemented in Java using:

```java
xmlMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
```

This ensures empty or whitespace-only fields are omitted from XML output (but not JSON, matching COBOL behavior).

### Character Count Tracking

Both COBOL programs track the character count of generated output using the `COUNT IN` clause. The Java implementations replicate this by using `String.length()` on the generated output.

### 88-Level Condition Implementation

COBOL's 88-level conditions (`ws-record-flag-enabled`, `ws-record-flag-disabled`) are implemented as Java methods:

```java
public void setEnabled(boolean enabled) {
    this.flag = enabled ? "true" : "false";
}

public boolean isEnabled() {
    return "true".equals(this.flag);
}
```

## Validation and Testing

To verify the Java implementation produces identical output to the COBOL version:

1. Run the COBOL programs (requires GnuCOBOL with XML/JSON support):
   ```bash
   cd ../xml_generate && cobc -x xml_generate.cbl && ./xml_generate
   cd ../json_generate && cobc -x json_generate.cbl && ./json_generate
   ```

2. Run the Java programs:
   ```bash
   mvn exec:java -Dexec.mainClass="com.example.serialization.XmlGenerateExample"
   mvn exec:java -Dexec.mainClass="com.example.serialization.JsonGenerateExample"
   ```

3. Compare the outputs - they should match exactly, including character counts.

## Migration Benefits

Migrating from COBOL to Java provides several advantages:

1. **No special build configuration**: Unlike COBOL (which requires `--with-xml2` and `--with-json` flags), Java's libraries work out of the box
2. **Rich ecosystem**: Access to Maven Central's extensive library collection
3. **Modern tooling**: IDEs, debuggers, profilers, and testing frameworks
4. **Cross-platform**: Java runs on any platform with a JVM
5. **Maintainability**: Larger pool of developers familiar with Java
6. **Integration**: Easier integration with modern web services and APIs

## Deployment Strategy

For production migration, consider this phased approach:

1. **Phase 1 - Parallel Deployment**: Run both COBOL and Java versions side-by-side
2. **Phase 2 - Validation**: Compare outputs to ensure identical behavior
3. **Phase 3 - Gradual Cutover**: Route increasing percentages of traffic to Java
4. **Phase 4 - Monitoring**: Monitor performance and error rates
5. **Phase 5 - Full Migration**: Complete cutover with COBOL as fallback
6. **Phase 6 - Decommission**: Remove COBOL version after stability period

## Troubleshooting

### Maven Build Issues

If Maven cannot find dependencies:
```bash
mvn clean install -U
```

### Java Version Issues

Verify Java version:
```bash
java -version
```

Ensure Java 11 or higher is installed.

### ClassNotFoundException

Ensure all Jackson dependencies are in the classpath when running manually.

## Further Enhancements

Potential improvements for production use:

1. **Input validation**: Add validation for field length constraints
2. **Configuration**: Externalize field mappings and serialization settings
3. **Logging**: Add structured logging for debugging and monitoring
4. **Unit tests**: Add JUnit tests to verify serialization behavior
5. **Performance**: Benchmark and optimize for high-throughput scenarios
6. **Error handling**: More granular exception handling and recovery
7. **Schema validation**: Validate XML/JSON against schemas
8. **Batch processing**: Support for processing multiple records efficiently

## References

- [Jackson Documentation](https://github.com/FasterXML/jackson)
- [Jackson XML Module](https://github.com/FasterXML/jackson-dataformat-xml)
- [GnuCOBOL XML GENERATE](https://gnucobol.sourceforge.io/)
- [GnuCOBOL JSON GENERATE](https://gnucobol.sourceforge.io/)

## License

This implementation follows the same MIT License as the parent COBOL-Demo repository.
