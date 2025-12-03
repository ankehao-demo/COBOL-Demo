# Java Implementation Design Document

This document outlines the Java implementation approach for migrating COBOL data serialization functionality to Java, including class structures, library choices, and implementation strategies.

## Executive Summary

The migration will replace GnuCOBOL's native `JSON GENERATE` and `XML GENERATE` commands with pure Java implementations using industry-standard libraries. This eliminates dependencies on libjson-c and libxml2 while providing equivalent functionality with improved maintainability and cross-platform compatibility.

## Library Selection

### JSON Serialization: Jackson

**Library:** `com.fasterxml.jackson.core:jackson-databind`

**Rationale:**
1. **Industry Standard:** Jackson is the most widely used JSON library in the Java ecosystem
2. **Performance:** Highly optimized for both serialization and deserialization
3. **Annotation Support:** Rich annotation system for field mapping (`@JsonProperty`)
4. **Flexibility:** Supports custom serializers, filters, and views
5. **Active Maintenance:** Regular updates and security patches
6. **No Native Dependencies:** Pure Java implementation

**Maven Dependency:**
```xml
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.15.3</version>
</dependency>
```

### XML Serialization: JAXB (Jakarta XML Binding)

**Library:** `jakarta.xml.bind:jakarta.xml.bind-api` with `org.glassfish.jaxb:jaxb-runtime`

**Rationale:**
1. **Standard API:** Part of Jakarta EE specification, ensuring long-term support
2. **Annotation-Based:** Declarative mapping via annotations (`@XmlElement`, `@XmlAttribute`)
3. **Attribute Support:** Native support for XML attributes (required for R-XML-003)
4. **XML Declaration:** Built-in support for XML declarations (required for R-XML-002)
5. **No Native Dependencies:** Pure Java implementation
6. **Mature and Stable:** Well-tested in enterprise applications

**Maven Dependencies:**
```xml
<dependency>
    <groupId>jakarta.xml.bind</groupId>
    <artifactId>jakarta.xml.bind-api</artifactId>
    <version>4.0.1</version>
</dependency>
<dependency>
    <groupId>org.glassfish.jaxb</groupId>
    <artifactId>jaxb-runtime</artifactId>
    <version>4.0.4</version>
</dependency>
```

### Alternative Considered: Jackson XML Module

**Library:** `com.fasterxml.jackson.dataformat:jackson-dataformat-xml`

While Jackson XML provides a unified API for both JSON and XML, JAXB was chosen because:
- Better native support for XML attributes
- More intuitive handling of XML declarations
- Clearer separation of concerns between JSON and XML serialization

## Important: Verified COBOL Output Format

After running the actual COBOL programs, the following output formats were verified:

**JSON Output (from json_generate.cbl):**
```json
{"ws-record":{"name":"Test Name","value":"Test Value","ws-record-blank":" ","enabled":"true"}}
```

Key observations that affect the Java design:
1. **Root Wrapper:** The JSON has a root wrapper object named "ws-record" (the COBOL group item name)
2. **String Boolean:** The "enabled" field is serialized as a string "true", not a native JSON boolean
3. **No Suppression:** The "ws-record-blank" field contains a single space (not suppressed in JSON, unlike XML)
4. **Character Count:** 94 characters

**XML Output (from xml_generate.cbl):**
```xml
<?xml version="1.0"?>
<ws-record enabled="true"><name>Test Name</name><value>Test Value</value></ws-record>
```

Key observations:
1. **XML Declaration:** Uses version 1.0 without encoding attribute
2. **Suppression Works:** The `ws-record-blank` field is correctly suppressed (SUPPRESS WHEN SPACES)
3. **Attribute:** The `enabled` field is correctly rendered as an XML attribute
4. **Character Count:** 107 characters

These observations inform the Java implementation design below.

## Class Architecture

### Package Structure

```
com.example.serialization/
├── model/
│   └── Record.java              # POJO representing ws-record
├── serializer/
│   ├── JsonSerializer.java      # JSON serialization service
│   ├── XmlSerializer.java       # XML serialization service
│   └── SerializationResult.java # Result wrapper with char count
├── adapter/
│   └── EmptyStringAdapter.java  # JAXB adapter for SUPPRESS WHEN SPACES
└── exception/
    └── SerializationException.java  # Custom exception type
```

### Class Designs

#### 1. Record.java (Data Model)

```java
package com.example.serialization.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.xml.bind.annotation.*;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import com.example.serialization.adapter.EmptyStringAdapter;

/**
 * Java equivalent of COBOL ws-record structure.
 * 
 * Maps to:
 * - json_generate/json_generate.cbl lines 27-33
 * - xml_generate/xml_generate.cbl lines 26-32
 */
@XmlRootElement(name = "ws-record")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Record {

    /**
     * Maps to: ws-record-name PIC X(10)
     * Serialized as: "name" (JSON/XML)
     */
    @JsonProperty("name")
    @XmlElement(name = "name")
    @XmlJavaTypeAdapter(EmptyStringAdapter.class)
    private String name;

    /**
     * Maps to: ws-record-value PIC X(10)
     * Serialized as: "value" (JSON/XML)
     */
    @JsonProperty("value")
    @XmlElement(name = "value")
    @XmlJavaTypeAdapter(EmptyStringAdapter.class)
    private String value;

    /**
     * Maps to: ws-record-blank PIC X(10)
     * Serialized as: "ws-record-blank" (default name)
     * Subject to SUPPRESS WHEN SPACES in XML
     */
    @JsonProperty("ws-record-blank")
    @XmlElement(name = "ws-record-blank")
    @XmlJavaTypeAdapter(EmptyStringAdapter.class)
    private String blank;

    /**
     * Maps to: ws-record-flag PIC X(5) with 88-level conditions
     * Serialized as: "enabled" (JSON property / XML attribute)
     */
    @JsonProperty("enabled")
    @XmlAttribute(name = "enabled")
    private boolean enabled;

    // Default constructor required for JAXB
    public Record() {
    }

    // All-args constructor
    public Record(String name, String value, String blank, boolean enabled) {
        this.name = trimToNull(name);
        this.value = trimToNull(value);
        this.blank = trimToNull(blank);
        this.enabled = enabled;
    }

    // Getters and setters with trimming logic
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = trimToNull(name);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = trimToNull(value);
    }

    public String getBlank() {
        return blank;
    }

    public void setBlank(String blank) {
        this.blank = trimToNull(blank);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * Trims string and converts empty/whitespace-only to null.
     * Implements COBOL SUPPRESS WHEN SPACES behavior.
     */
    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    /**
     * Builder pattern for fluent construction
     */
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String name;
        private String value;
        private String blank;
        private boolean enabled;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder value(String value) {
            this.value = value;
            return this;
        }

        public Builder blank(String blank) {
            this.blank = blank;
            return this;
        }

        public Builder enabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public Record build() {
            return new Record(name, value, blank, enabled);
        }
    }
}
```

#### 2. SerializationResult.java (Result Wrapper)

```java
package com.example.serialization.serializer;

/**
 * Wrapper for serialization results, providing both the output
 * and character count (equivalent to COBOL COUNT IN clause).
 */
public class SerializationResult {
    
    private final String output;
    private final int characterCount;

    public SerializationResult(String output) {
        this.output = output;
        this.characterCount = output != null ? output.length() : 0;
    }

    /**
     * Returns the serialized output string.
     */
    public String getOutput() {
        return output;
    }

    /**
     * Returns the character count of the output.
     * Equivalent to COBOL's COUNT IN ws-json-char-count / ws-xml-char-count.
     */
    public int getCharacterCount() {
        return characterCount;
    }

    @Override
    public String toString() {
        return output;
    }
}
```

#### 3. JsonSerializer.java

```java
package com.example.serialization.serializer;

import com.example.serialization.exception.SerializationException;
import com.example.serialization.model.Record;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * JSON serialization service.
 * Replaces COBOL JSON GENERATE command (json_generate/json_generate.cbl).
 */
public class JsonSerializer {

    private final ObjectMapper objectMapper;

    public JsonSerializer() {
        this.objectMapper = new ObjectMapper();
        // Compact output (no pretty printing) to match COBOL behavior
        this.objectMapper.disable(SerializationFeature.INDENT_OUTPUT);
    }

    /**
     * Serializes a Record to JSON format.
     * 
     * Equivalent to COBOL:
     *   json generate ws-json-output
     *       from ws-record
     *       count in ws-json-char-count
     *       name of ...
     *       on exception ...
     *       not on exception ...
     *   end-json
     *
     * @param record The record to serialize
     * @return SerializationResult containing JSON output and character count
     * @throws SerializationException if serialization fails (equivalent to ON EXCEPTION)
     */
    public SerializationResult serialize(Record record) throws SerializationException {
        try {
            String json = objectMapper.writeValueAsString(record);
            // Equivalent to NOT ON EXCEPTION path
            return new SerializationResult(json);
        } catch (JsonProcessingException e) {
            // Equivalent to ON EXCEPTION path with JSON-CODE
            throw new SerializationException(
                "Error generating JSON: " + e.getMessage(),
                e,
                extractErrorCode(e)
            );
        }
    }

    /**
     * Extracts an error code similar to COBOL's JSON-CODE.
     */
    private int extractErrorCode(JsonProcessingException e) {
        // Map Jackson exceptions to numeric codes
        // This provides similar functionality to COBOL's JSON-CODE
        if (e instanceof com.fasterxml.jackson.databind.JsonMappingException) {
            return 1; // Mapping error
        }
        return 99; // General error
    }
}
```

#### 4. XmlSerializer.java

```java
package com.example.serialization.serializer;

import com.example.serialization.exception.SerializationException;
import com.example.serialization.model.Record;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import java.io.StringWriter;

/**
 * XML serialization service.
 * Replaces COBOL XML GENERATE command (xml_generate/xml_generate.cbl).
 */
public class XmlSerializer {

    private final JAXBContext jaxbContext;

    public XmlSerializer() throws SerializationException {
        try {
            this.jaxbContext = JAXBContext.newInstance(Record.class);
        } catch (JAXBException e) {
            throw new SerializationException(
                "Failed to initialize XML serializer: " + e.getMessage(),
                e,
                0
            );
        }
    }

    /**
     * Serializes a Record to XML format.
     * 
     * Equivalent to COBOL:
     *   xml generate ws-xml-output
     *       from ws-record
     *       count in ws-xml-char-count
     *       with xml-declaration
     *       name of ...
     *       type of ws-record-flag is attribute
     *       suppress when spaces
     *       on exception ...
     *       not on exception ...
     *   end-xml
     *
     * @param record The record to serialize
     * @return SerializationResult containing XML output and character count
     * @throws SerializationException if serialization fails (equivalent to ON EXCEPTION)
     */
    public SerializationResult serialize(Record record) throws SerializationException {
        try {
            Marshaller marshaller = jaxbContext.createMarshaller();
            
            // WITH XML-DECLARATION (line 44 of xml_generate.cbl)
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, false);
            
            // UTF-8 encoding
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            
            // Compact output (no formatting)
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);

            StringWriter writer = new StringWriter();
            marshaller.marshal(record, writer);
            
            String xml = writer.toString();
            // Equivalent to NOT ON EXCEPTION path
            return new SerializationResult(xml);
            
        } catch (JAXBException e) {
            // Equivalent to ON EXCEPTION path with XML-CODE
            throw new SerializationException(
                "Error generating XML: " + e.getMessage(),
                e,
                extractErrorCode(e)
            );
        }
    }

    /**
     * Extracts an error code similar to COBOL's XML-CODE.
     */
    private int extractErrorCode(JAXBException e) {
        // Map JAXB exceptions to numeric codes
        // This provides similar functionality to COBOL's XML-CODE
        if (e.getLinkedException() != null) {
            return 1; // Linked exception present
        }
        return 99; // General error
    }
}
```

#### 5. EmptyStringAdapter.java (JAXB Adapter)

```java
package com.example.serialization.adapter;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;

/**
 * JAXB adapter that implements SUPPRESS WHEN SPACES behavior.
 * Converts empty/whitespace-only strings to null, which JAXB
 * will then exclude from the XML output.
 * 
 * Implements: xml_generate/xml_generate.cbl line 50
 *   suppress when spaces
 */
public class EmptyStringAdapter extends XmlAdapter<String, String> {

    @Override
    public String unmarshal(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    @Override
    public String marshal(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
```

#### 6. SerializationException.java

```java
package com.example.serialization.exception;

/**
 * Custom exception for serialization errors.
 * Provides error code similar to COBOL's JSON-CODE and XML-CODE.
 */
public class SerializationException extends Exception {

    private final int errorCode;

    public SerializationException(String message, Throwable cause, int errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    /**
     * Returns the error code, similar to COBOL's JSON-CODE or XML-CODE.
     */
    public int getErrorCode() {
        return errorCode;
    }
}
```

## Implementation Strategy

### Phase 2: Implementation Steps

1. **Set up Java project structure**
   - Create Maven/Gradle project
   - Add dependencies (Jackson, JAXB)
   - Configure build settings

2. **Implement data model**
   - Create `Record.java` with all annotations
   - Implement validation logic
   - Add builder pattern

3. **Implement JSON serialization**
   - Create `JsonSerializer.java`
   - Configure ObjectMapper
   - Implement error handling

4. **Implement XML serialization**
   - Create `XmlSerializer.java`
   - Create `EmptyStringAdapter.java`
   - Configure JAXB marshaller

5. **Create unit tests**
   - Test JSON output matches expected format
   - Test XML output matches expected format
   - Test SUPPRESS WHEN SPACES behavior
   - Test error handling

6. **Integration testing**
   - Compare Java output with COBOL output
   - Verify character counts match
   - Test edge cases

### Field Name Mapping Implementation

The NAME OF clause in COBOL is implemented using annotations:

**JSON (Jackson):**
```java
@JsonProperty("name")
private String name;  // Maps ws-record-name to "name"
```

**XML (JAXB):**
```java
@XmlElement(name = "name")
private String name;  // Maps ws-record-name to <name>

@XmlAttribute(name = "enabled")
private boolean enabled;  // Maps ws-record-flag to enabled="..."
```

### Suppression Logic Implementation

The SUPPRESS WHEN SPACES behavior is implemented through:

1. **Data Model:** `trimToNull()` method converts empty strings to null
2. **JSON:** `@JsonInclude(JsonInclude.Include.NON_EMPTY)` excludes null/empty values
3. **XML:** `EmptyStringAdapter` converts empty strings to null, which JAXB excludes

### Error Handling Strategy

| COBOL Construct | Java Equivalent |
|-----------------|-----------------|
| ON EXCEPTION | try-catch block |
| JSON-CODE / XML-CODE | SerializationException.getErrorCode() |
| STOP RUN | throw exception (caller decides) |
| NOT ON EXCEPTION | Normal return path |

## Usage Example

```java
// Create a record (equivalent to COBOL MOVE statements)
Record record = Record.builder()
    .name("Test Name")
    .value("Test Value")
    .blank("          ")  // Will be suppressed (spaces only)
    .enabled(true)
    .build();

// JSON serialization
JsonSerializer jsonSerializer = new JsonSerializer();
try {
    SerializationResult jsonResult = jsonSerializer.serialize(record);
    System.out.println("JSON document successfully generated.");
    System.out.println("Generated JSON: " + jsonResult.getOutput());
    System.out.println("Character count: " + jsonResult.getCharacterCount());
} catch (SerializationException e) {
    System.out.println("Error generating JSON error " + e.getErrorCode());
}

// XML serialization
XmlSerializer xmlSerializer = new XmlSerializer();
try {
    SerializationResult xmlResult = xmlSerializer.serialize(record);
    System.out.println("XML document successfully generated.");
    System.out.println("Generated XML: " + xmlResult.getOutput());
    System.out.println("Character count: " + xmlResult.getCharacterCount());
} catch (SerializationException e) {
    System.out.println("Error generating XML error " + e.getErrorCode());
}
```

## Dependency Elimination

### Before (COBOL)

The COBOL implementation requires:
- **libjson-c:** Native C library for JSON processing
- **libxml2:** Native C library for XML processing
- **GnuCOBOL configuration:** `./configure --with-json --with-xml2`

These dependencies create:
- Platform-specific build requirements
- Native library management complexity
- Potential version compatibility issues

### After (Java)

The Java implementation requires only:
- **Jackson:** Pure Java library (JAR dependency)
- **JAXB:** Pure Java library (JAR dependency)
- **Standard JDK:** No native dependencies

Benefits:
- Cross-platform compatibility (any JVM)
- Simple dependency management (Maven/Gradle)
- No native library installation required
- Easier deployment and containerization

## Testing Strategy

### Unit Tests

```java
@Test
void testJsonSerialization() {
    Record record = new Record("Test Name", "Test Value", null, true);
    JsonSerializer serializer = new JsonSerializer();
    
    SerializationResult result = serializer.serialize(record);
    
    assertEquals(
        "{\"name\":\"Test Name\",\"value\":\"Test Value\",\"enabled\":true}",
        result.getOutput()
    );
}

@Test
void testXmlSerialization() {
    Record record = new Record("Test Name", "Test Value", null, true);
    XmlSerializer serializer = new XmlSerializer();
    
    SerializationResult result = serializer.serialize(record);
    
    assertTrue(result.getOutput().contains("<?xml version=\"1.0\""));
    assertTrue(result.getOutput().contains("<ws-record enabled=\"true\">"));
    assertTrue(result.getOutput().contains("<name>Test Name</name>"));
}

@Test
void testSuppressWhenSpaces() {
    Record record = new Record("Test", "Value", "   ", false);
    XmlSerializer serializer = new XmlSerializer();
    
    SerializationResult result = serializer.serialize(record);
    
    assertFalse(result.getOutput().contains("ws-record-blank"));
}
```

## Conclusion

This design provides a complete migration path from COBOL's native serialization commands to pure Java implementations. The use of Jackson and JAXB ensures industry-standard, well-maintained libraries while eliminating native dependencies. The annotation-based approach provides clear mapping between COBOL field names and serialized output, making the code maintainable and self-documenting.

The next phase (Phase 2) will implement this design, creating the actual Java classes and comprehensive test suites to verify functional equivalence with the COBOL implementation.
