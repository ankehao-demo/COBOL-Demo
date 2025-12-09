# Data Serialization Migration Design Document
## COBOL to Java Migration - Phase 1: Analysis and Design

**Document Version:** 1.0  
**Date:** December 9, 2025  
**Author:** Devin AI  
**Status:** Phase 1 Complete

---

## 1. Executive Summary

This document provides a comprehensive analysis of the existing COBOL data serialization functionality and defines the Java equivalents for migration. The COBOL implementation consists of two programs that generate XML and JSON documents from COBOL record structures using GnuCOBOL's built-in serialization commands.

---

## 2. Current COBOL Implementation Analysis

### 2.1 XML Generation (`xml_generate/xml_generate.cbl`)

#### 2.1.1 Program Overview

The XML generation program (`xml-generate-example`) converts COBOL record structures into well-formed XML documents using the `XML GENERATE` statement. This is a GnuCOBOL-specific feature that requires the libxml2 library.

**File Location:** `xml_generate/xml_generate.cbl`  
**Program ID:** `xml-generate-example`  
**Author:** Erik Eriksen  
**Original Date:** 2022-04-11

#### 2.1.2 Dependencies and Configuration

The program requires libxml2 to be installed on the system. GnuCOBOL must be configured with XML support during compilation:

```bash
./configure --with-xml2 --without-db
```

The `--without-db` flag is optional and only needed if Berkeley DB libraries are not available.

#### 2.1.3 Data Structures

**Output Buffer (Line 22):**
```cobol
01  ws-xml-output                       pic x(256).
```
A 256-byte fixed-length character field that holds the generated XML document.

**Character Count (Line 24):**
```cobol
01  ws-xml-char-count                   pic 9(4).
```
A 4-digit numeric field that tracks the actual length of the generated XML content.

**Record Structure (Lines 26-32):**
```cobol
01  ws-record.
    05  ws-record-name                  pic x(10).
    05  ws-record-value                 pic x(10).
    05  ws-record-blank                 pic x(10).
    05  ws-record-flag                  pic x(5) value "false".
        88  ws-record-flag-enabled      value "true".
        88  ws-record-flag-disabled     value "false".
```

The record structure contains four fields:
- `ws-record-name`: 10-character field for the name
- `ws-record-value`: 10-character field for the value
- `ws-record-blank`: 10-character field (intentionally left blank for testing space suppression)
- `ws-record-flag`: 5-character field with a default value of "false", using 88-level condition names for boolean-like behavior

#### 2.1.4 XML Generation Features (Lines 41-56)

```cobol
xml generate ws-xml-output
    from ws-record
    count in ws-xml-char-count
    with xml-declaration
    name of
        ws-record-name is "name",
        ws-record-value is "value",
        ws-record-flag is "enabled"
    type of ws-record-flag is attribute
    suppress when spaces
    on exception
        display "Error generating xml error " XML-CODE
        stop run
    not on exception
        display "XML document successfully generated."
end-xml
```

**Key Features:**

1. **XML Declaration (`with xml-declaration`):** Includes the standard XML declaration (`<?xml version="1.0"?>`) at the beginning of the output.

2. **Field Name Mapping (`name of` clause):** Maps COBOL field names to XML element/attribute names:
   - `ws-record-name` → `name`
   - `ws-record-value` → `value`
   - `ws-record-flag` → `enabled`

3. **Attribute Serialization (`type of ... is attribute`):** The `ws-record-flag` field is serialized as an XML attribute rather than a child element.

4. **Space Suppression (`suppress when spaces`):** Fields containing only spaces are omitted from the XML output. This is critical for handling COBOL's fixed-length fields.

5. **Character Count Tracking (`count in`):** The actual number of characters written to the output buffer is stored in `ws-xml-char-count`.

#### 2.1.5 Error Handling

The program uses COBOL's structured exception handling:

- **`ON EXCEPTION`:** Triggered when XML generation fails. Displays the `XML-CODE` special register (containing the error code) and terminates the program.
- **`NOT ON EXCEPTION`:** Executed on successful generation, displays a success message.

#### 2.1.6 Test Data (Lines 37-39)

```cobol
move "Test Name" to ws-record-name
move "Test Value" to ws-record-value
set ws-record-flag-enabled to true
```

The program initializes test data with:
- Name: "Test Name"
- Value: "Test Value"
- Blank: (left empty to test space suppression)
- Flag: "true" (set via 88-level condition)

#### 2.1.7 Expected XML Output

Based on the configuration, the expected output would be:
```xml
<?xml version="1.0"?><ws-record enabled="true"><name>Test Name</name><value>Test Value</value></ws-record>
```

Note: The `ws-record-blank` field is suppressed because it contains only spaces.

---

### 2.2 JSON Generation (`json_generate/json_generate.cbl`)

#### 2.2.1 Program Overview

The JSON generation program (`json-generate-example`) converts COBOL record structures into JSON documents using the `JSON GENERATE` statement. This requires the libjson-c library.

**File Location:** `json_generate/json_generate.cbl`  
**Program ID:** `json-generate-example`  
**Author:** Erik Eriksen  
**Original Date:** 2022-04-12

#### 2.2.2 Dependencies and Configuration

The program requires libjson-c to be installed. GnuCOBOL must be configured with JSON support:

```bash
./configure --with-json --without-db
```

#### 2.2.3 Data Structures

**Output Buffer (Line 23):**
```cobol
01  ws-json-output                       pic x(256).
```
A 256-byte fixed-length character field for the generated JSON document.

**Character Count (Line 25):**
```cobol
01  ws-json-char-count                   pic 9(4).
```
A 4-digit numeric field tracking the actual JSON content length.

**Record Structure (Lines 27-33):**
```cobol
01  ws-record.
    05  ws-record-name                  pic x(10).
    05  ws-record-value                 pic x(10).
    05  ws-record-blank                 pic x(10).
    05  ws-record-flag                  pic x(5) value "false".
        88  ws-record-flag-enabled      value "true".
        88  ws-record-flag-disabled     value "false".
```

This is identical to the XML program's record structure.

#### 2.2.4 JSON Generation Features (Lines 42-54)

```cobol
json generate ws-json-output
    from ws-record
    count in ws-json-char-count
    name of
        ws-record-name is "name",
        ws-record-value is "value",
        ws-record-flag is "enabled"
    on exception
        display "Error generating JSON error " JSON-CODE
        stop run
    not on exception
        display "JSON document successfully generated."
end-json
```

**Key Features:**

1. **Field Name Mapping (`name of` clause):** Maps COBOL field names to JSON property names:
   - `ws-record-name` → `name`
   - `ws-record-value` → `value`
   - `ws-record-flag` → `enabled`

2. **Character Count Tracking (`count in`):** Stores the actual JSON content length in `ws-json-char-count`.

**Notable Differences from XML Generation:**
- No declaration option (JSON has no equivalent to XML declaration)
- No attribute type option (JSON doesn't have attributes)
- No explicit space suppression clause (JSON generation handles this differently)

#### 2.2.5 Error Handling

Similar to XML generation:
- **`ON EXCEPTION`:** Displays `JSON-CODE` error register and terminates
- **`NOT ON EXCEPTION`:** Displays success message

#### 2.2.6 Test Data (Lines 38-40)

```cobol
move "Test Name" to ws-record-name
move "Test Value" to ws-record-value
set ws-record-flag-enabled to true
```

Identical test data to the XML program.

#### 2.2.7 Expected JSON Output

```json
{"ws-record":{"name":"Test Name","value":"Test Value","ws-record-blank":"","enabled":"true"}}
```

Note: Unlike XML, JSON generation may include empty string values for blank fields.

---

### 2.3 Common Data Structure Summary

Both programs share the same record structure (`ws-record`) with the following fields:

| COBOL Field | PIC Clause | Size | Default Value | Purpose |
|-------------|------------|------|---------------|---------|
| `ws-record-name` | X(10) | 10 chars | None | Name field |
| `ws-record-value` | X(10) | 10 chars | None | Value field |
| `ws-record-blank` | X(10) | 10 chars | None | Blank field (for testing) |
| `ws-record-flag` | X(5) | 5 chars | "false" | Boolean-like flag |

The 88-level conditions (`ws-record-flag-enabled` and `ws-record-flag-disabled`) provide boolean-like semantics for the flag field.

---

## 3. Java Equivalent Design

### 3.1 Data Structure Mapping

#### 3.1.1 Java Record Class (Recommended for Java 16+)

```java
package com.example.serialization.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 * Java equivalent of COBOL ws-record structure.
 * Maps to the data structure used in xml_generate.cbl and json_generate.cbl
 */
@JacksonXmlRootElement(localName = "ws-record")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record DataRecord(
    @JsonProperty("name")
    @JacksonXmlProperty(localName = "name")
    String name,
    
    @JsonProperty("value")
    @JacksonXmlProperty(localName = "value")
    String value,
    
    @JsonProperty("ws-record-blank")
    @JacksonXmlProperty(localName = "ws-record-blank")
    String blank,
    
    @JsonProperty("enabled")
    @JacksonXmlProperty(localName = "enabled", isAttribute = true)
    String flag
) {
    /**
     * Default constructor with flag defaulting to "false"
     */
    public DataRecord(String name, String value, String blank) {
        this(name, value, blank, "false");
    }
    
    /**
     * Constructor matching COBOL test data initialization
     */
    public static DataRecord createTestRecord() {
        return new DataRecord("Test Name", "Test Value", null, "true");
    }
    
    /**
     * Check if flag is enabled (equivalent to 88-level ws-record-flag-enabled)
     */
    public boolean isFlagEnabled() {
        return "true".equals(flag);
    }
    
    /**
     * Check if flag is disabled (equivalent to 88-level ws-record-flag-disabled)
     */
    public boolean isFlagDisabled() {
        return "false".equals(flag);
    }
}
```

#### 3.1.2 Alternative: Traditional POJO Class (Java 8+)

```java
package com.example.serialization.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.Objects;

/**
 * Java POJO equivalent of COBOL ws-record structure.
 * Compatible with Java 8 and later versions.
 */
@JacksonXmlRootElement(localName = "ws-record")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class DataRecord {
    
    @JsonProperty("name")
    @JacksonXmlProperty(localName = "name")
    private String name;
    
    @JsonProperty("value")
    @JacksonXmlProperty(localName = "value")
    private String value;
    
    @JsonProperty("ws-record-blank")
    @JacksonXmlProperty(localName = "ws-record-blank")
    private String blank;
    
    @JsonProperty("enabled")
    @JacksonXmlProperty(localName = "enabled", isAttribute = true)
    private String flag = "false";  // Default value matching COBOL
    
    // Default constructor
    public DataRecord() {}
    
    // Full constructor
    public DataRecord(String name, String value, String blank, String flag) {
        this.name = name;
        this.value = value;
        this.blank = blank;
        this.flag = flag != null ? flag : "false";
    }
    
    // Constructor without flag (uses default)
    public DataRecord(String name, String value, String blank) {
        this(name, value, blank, "false");
    }
    
    // Factory method for test data
    public static DataRecord createTestRecord() {
        return new DataRecord("Test Name", "Test Value", null, "true");
    }
    
    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }
    
    public String getBlank() { return blank; }
    public void setBlank(String blank) { this.blank = blank; }
    
    public String getFlag() { return flag; }
    public void setFlag(String flag) { this.flag = flag; }
    
    // 88-level equivalent methods
    public boolean isFlagEnabled() { return "true".equals(flag); }
    public boolean isFlagDisabled() { return "false".equals(flag); }
    
    public void setFlagEnabled() { this.flag = "true"; }
    public void setFlagDisabled() { this.flag = "false"; }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataRecord that = (DataRecord) o;
        return Objects.equals(name, that.name) &&
               Objects.equals(value, that.value) &&
               Objects.equals(blank, that.blank) &&
               Objects.equals(flag, that.flag);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(name, value, blank, flag);
    }
    
    @Override
    public String toString() {
        return "DataRecord{" +
               "name='" + name + '\'' +
               ", value='" + value + '\'' +
               ", blank='" + blank + '\'' +
               ", flag='" + flag + '\'' +
               '}';
    }
}
```

### 3.2 Field Mapping Summary

| COBOL Field | Java Field | Type | Annotations | Notes |
|-------------|------------|------|-------------|-------|
| `ws-record-name` | `name` | String | `@JsonProperty("name")` | Direct mapping |
| `ws-record-value` | `value` | String | `@JsonProperty("value")` | Direct mapping |
| `ws-record-blank` | `blank` | String | `@JsonProperty("ws-record-blank")` | Nullable for suppression |
| `ws-record-flag` | `flag` | String | `@JsonProperty("enabled")`, `isAttribute=true` | Default "false" |

---

## 4. Java Library Recommendations

### 4.1 Primary Recommendation: Jackson

Jackson is recommended as the primary library for both XML and JSON serialization due to its unified API, extensive feature set, and widespread adoption.

#### 4.1.1 Required Dependencies (Maven)

```xml
<dependencies>
    <!-- Jackson Core -->
    <dependency>
        <groupId>com.fasterxml.jackson.core</groupId>
        <artifactId>jackson-databind</artifactId>
        <version>2.15.3</version>
    </dependency>
    
    <!-- Jackson XML Support -->
    <dependency>
        <groupId>com.fasterxml.jackson.dataformat</groupId>
        <artifactId>jackson-dataformat-xml</artifactId>
        <version>2.15.3</version>
    </dependency>
</dependencies>
```

#### 4.1.2 Required Dependencies (Gradle)

```groovy
dependencies {
    implementation 'com.fasterxml.jackson.core:jackson-databind:2.15.3'
    implementation 'com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.15.3'
}
```

### 4.2 Feature Comparison

| COBOL Feature | Jackson XML | Jackson JSON | JAXB | Gson |
|---------------|-------------|--------------|------|------|
| Field name mapping | `@JacksonXmlProperty` | `@JsonProperty` | `@XmlElement` | `@SerializedName` |
| XML attributes | `isAttribute = true` | N/A | `@XmlAttribute` | N/A |
| XML declaration | `ToXmlGenerator.Feature` | N/A | `Marshaller.JAXB_FRAGMENT` | N/A |
| Space/null suppression | `@JsonInclude(NON_EMPTY)` | `@JsonInclude(NON_EMPTY)` | `@XmlElement(nillable)` | Custom serializer |
| Character count | Manual via `String.length()` | Manual via `String.length()` | Manual | Manual |

### 4.3 Alternative Libraries

#### 4.3.1 JAXB (Java Architecture for XML Binding)

JAXB is a standard Java API for XML binding, suitable if only XML serialization is needed.

**Pros:**
- Standard Java API (included in Java SE until Java 10)
- Mature and well-documented
- Strong schema validation support

**Cons:**
- Removed from Java SE in Java 11+ (requires separate dependency)
- XML-only (separate library needed for JSON)
- More verbose configuration

**Dependencies (Java 11+):**
```xml
<dependency>
    <groupId>jakarta.xml.bind</groupId>
    <artifactId>jakarta.xml.bind-api</artifactId>
    <version>4.0.0</version>
</dependency>
<dependency>
    <groupId>org.glassfish.jaxb</groupId>
    <artifactId>jaxb-runtime</artifactId>
    <version>4.0.3</version>
</dependency>
```

#### 4.3.2 Gson (Google JSON)

Gson is a lightweight JSON library from Google.

**Pros:**
- Simple API
- Lightweight
- Good for JSON-only use cases

**Cons:**
- JSON-only (no XML support)
- Less feature-rich than Jackson
- No built-in XML attribute support

---

## 5. Error Handling Strategy

### 5.1 COBOL Error Handling Analysis

The COBOL programs use structured exception handling with `ON EXCEPTION` and `NOT ON EXCEPTION` clauses:

```cobol
on exception
    display "Error generating xml error " XML-CODE
    stop run
not on exception
    display "XML document successfully generated."
```

Key characteristics:
- `XML-CODE` and `JSON-CODE` special registers contain error codes
- `ON EXCEPTION` triggers on any generation failure
- Program terminates on error (`stop run`)

### 5.2 Java Error Handling Design

#### 5.2.1 Custom Exception Classes

```java
package com.example.serialization.exception;

/**
 * Base exception for serialization errors.
 * Equivalent to COBOL's ON EXCEPTION handling.
 */
public class SerializationException extends Exception {
    private final int errorCode;
    
    public SerializationException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
    
    public SerializationException(String message, int errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }
    
    /**
     * Returns the error code (equivalent to XML-CODE or JSON-CODE)
     */
    public int getErrorCode() {
        return errorCode;
    }
}

/**
 * XML-specific serialization exception.
 */
public class XmlSerializationException extends SerializationException {
    public XmlSerializationException(String message, int xmlCode) {
        super(message, xmlCode);
    }
    
    public XmlSerializationException(String message, int xmlCode, Throwable cause) {
        super(message, xmlCode, cause);
    }
}

/**
 * JSON-specific serialization exception.
 */
public class JsonSerializationException extends SerializationException {
    public JsonSerializationException(String message, int jsonCode) {
        super(message, jsonCode);
    }
    
    public JsonSerializationException(String message, int jsonCode, Throwable cause) {
        super(message, jsonCode, cause);
    }
}
```

#### 5.2.2 Error Code Mapping

| COBOL Error | Java Equivalent | Description |
|-------------|-----------------|-------------|
| XML-CODE non-zero | `XmlSerializationException` | XML generation failed |
| JSON-CODE non-zero | `JsonSerializationException` | JSON generation failed |
| Buffer overflow | `SerializationException(1)` | Output exceeds buffer size |
| Invalid data | `SerializationException(2)` | Input data validation failed |

#### 5.2.3 Serialization Service with Error Handling

```java
package com.example.serialization.service;

import com.example.serialization.exception.*;
import com.example.serialization.model.DataRecord;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;

/**
 * Service class for XML and JSON serialization.
 * Replicates COBOL XML GENERATE and JSON GENERATE functionality.
 */
public class DataSerializationService {
    
    private static final int MAX_OUTPUT_LENGTH = 256;  // Matches COBOL buffer size
    
    private final XmlMapper xmlMapper;
    private final ObjectMapper jsonMapper;
    
    public DataSerializationService() {
        // Configure XML mapper with declaration support
        this.xmlMapper = new XmlMapper();
        this.xmlMapper.configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true);
        this.xmlMapper.configure(SerializationFeature.INDENT_OUTPUT, false);
        
        // Configure JSON mapper
        this.jsonMapper = new ObjectMapper();
        this.jsonMapper.configure(SerializationFeature.INDENT_OUTPUT, false);
    }
    
    /**
     * Generates XML from a DataRecord.
     * Equivalent to COBOL: XML GENERATE ws-xml-output FROM ws-record
     * 
     * @param record The data record to serialize
     * @return SerializationResult containing XML and character count
     * @throws XmlSerializationException if generation fails
     */
    public SerializationResult generateXml(DataRecord record) throws XmlSerializationException {
        try {
            String xml = xmlMapper.writeValueAsString(record);
            int charCount = xml.length();
            
            // Check buffer size (equivalent to COBOL buffer overflow)
            if (charCount > MAX_OUTPUT_LENGTH) {
                throw new XmlSerializationException(
                    "XML output exceeds maximum buffer size of " + MAX_OUTPUT_LENGTH,
                    1  // Error code for buffer overflow
                );
            }
            
            System.out.println("XML document successfully generated.");
            return new SerializationResult(xml, charCount);
            
        } catch (JsonProcessingException e) {
            throw new XmlSerializationException(
                "Error generating XML: " + e.getMessage(),
                2,  // Error code for processing error
                e
            );
        }
    }
    
    /**
     * Generates JSON from a DataRecord.
     * Equivalent to COBOL: JSON GENERATE ws-json-output FROM ws-record
     * 
     * @param record The data record to serialize
     * @return SerializationResult containing JSON and character count
     * @throws JsonSerializationException if generation fails
     */
    public SerializationResult generateJson(DataRecord record) throws JsonSerializationException {
        try {
            String json = jsonMapper.writeValueAsString(record);
            int charCount = json.length();
            
            // Check buffer size
            if (charCount > MAX_OUTPUT_LENGTH) {
                throw new JsonSerializationException(
                    "JSON output exceeds maximum buffer size of " + MAX_OUTPUT_LENGTH,
                    1
                );
            }
            
            System.out.println("JSON document successfully generated.");
            return new SerializationResult(json, charCount);
            
        } catch (JsonProcessingException e) {
            throw new JsonSerializationException(
                "Error generating JSON: " + e.getMessage(),
                2,
                e
            );
        }
    }
    
    /**
     * Result class containing serialized output and character count.
     * Equivalent to COBOL's ws-xml-output/ws-json-output and ws-xml-char-count/ws-json-char-count
     */
    public record SerializationResult(String output, int charCount) {}
}
```

---

## 6. Special COBOL Features Requiring Custom Logic

### 6.1 XML Attribute Serialization

**COBOL Feature:**
```cobol
type of ws-record-flag is attribute
```

**Java Implementation:**
Use `@JacksonXmlProperty(isAttribute = true)` annotation:
```java
@JacksonXmlProperty(localName = "enabled", isAttribute = true)
private String flag;
```

### 6.2 Space Suppression

**COBOL Feature:**
```cobol
suppress when spaces
```

**Java Implementation:**
Use `@JsonInclude(JsonInclude.Include.NON_EMPTY)` at class or field level:
```java
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class DataRecord {
    // Fields with null or empty values will be omitted
}
```

For more granular control:
```java
@JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = BlankStringFilter.class)
private String blank;

public class BlankStringFilter {
    @Override
    public boolean equals(Object obj) {
        if (obj == null) return true;
        if (obj instanceof String) {
            return ((String) obj).trim().isEmpty();
        }
        return false;
    }
}
```

### 6.3 XML Declaration

**COBOL Feature:**
```cobol
with xml-declaration
```

**Java Implementation:**
Configure the XmlMapper:
```java
XmlMapper xmlMapper = new XmlMapper();
xmlMapper.configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true);
```

### 6.4 Character Count Tracking

**COBOL Feature:**
```cobol
count in ws-xml-char-count
```

**Java Implementation:**
Calculate after serialization:
```java
String output = mapper.writeValueAsString(record);
int charCount = output.length();
```

### 6.5 88-Level Condition Names

**COBOL Feature:**
```cobol
05  ws-record-flag                  pic x(5) value "false".
    88  ws-record-flag-enabled      value "true".
    88  ws-record-flag-disabled     value "false".
```

**Java Implementation:**
Provide helper methods:
```java
public boolean isFlagEnabled() {
    return "true".equals(flag);
}

public boolean isFlagDisabled() {
    return "false".equals(flag);
}

public void setFlagEnabled() {
    this.flag = "true";
}

public void setFlagDisabled() {
    this.flag = "false";
}
```

---

## 7. Implementation Recommendations

### 7.1 Project Structure

```
src/main/java/com/example/serialization/
├── model/
│   └── DataRecord.java
├── service/
│   └── DataSerializationService.java
├── exception/
│   ├── SerializationException.java
│   ├── XmlSerializationException.java
│   └── JsonSerializationException.java
└── Main.java
```

### 7.2 Testing Strategy

1. **Unit Tests:** Test serialization output matches expected COBOL output
2. **Integration Tests:** Verify round-trip serialization/deserialization
3. **Comparison Tests:** Run both COBOL and Java programs with same input, compare outputs

### 7.3 Migration Phases

| Phase | Description | Deliverables |
|-------|-------------|--------------|
| Phase 1 | Analysis and Design | This document |
| Phase 2 | Core Implementation | DataRecord class, serialization service |
| Phase 3 | Testing | Unit tests, integration tests |
| Phase 4 | Validation | Output comparison with COBOL programs |

---

## 8. Appendix

### 8.1 Complete COBOL Source Reference

#### XML Generation (xml_generate/xml_generate.cbl)
- Lines 22-24: Output buffer and character count definitions
- Lines 26-32: Record structure definition
- Lines 37-39: Test data initialization
- Lines 41-56: XML GENERATE statement with all options

#### JSON Generation (json_generate/json_generate.cbl)
- Lines 23-25: Output buffer and character count definitions
- Lines 27-33: Record structure definition
- Lines 38-40: Test data initialization
- Lines 42-54: JSON GENERATE statement with all options

### 8.2 Actual Output Samples (Validated)

**XML Output (107 characters):**
```xml
<?xml version="1.0"?>
<ws-record enabled="true"><name>Test Name</name><value>Test Value</value></ws-record>
```

Note: The `ws-record-blank` field is suppressed because it contains only spaces (due to `suppress when spaces`).

**JSON Output (94 characters):**
```json
{"ws-record":{"name":"Test Name","value":"Test Value","ws-record-blank":" ","enabled":"true"}}
```

Note: Unlike XML, JSON generation includes the blank field with a single space value (COBOL's fixed-length fields are space-padded).

### 8.3 Glossary

| Term | Definition |
|------|------------|
| `XML GENERATE` | COBOL statement for converting records to XML |
| `JSON GENERATE` | COBOL statement for converting records to JSON |
| `XML-CODE` | COBOL special register containing XML operation status |
| `JSON-CODE` | COBOL special register containing JSON operation status |
| `88-level` | COBOL condition name providing boolean-like semantics |
| `PIC X(n)` | COBOL picture clause for alphanumeric fields of n characters |

---

## 9. Document History

| Version | Date | Author | Changes |
|---------|------|--------|---------|
| 1.0 | 2025-12-09 | Devin AI | Initial document - Phase 1 complete |
