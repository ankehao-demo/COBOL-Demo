# COBOL to Java Data Serialization Migration Analysis

## Phase 1: Analysis and Design

### COBOL Record Structures Analysis

#### XML Generation Program (xml_generate/xml_generate.cbl)

**Data Structure (lines 26-32):**
```cobol
01  ws-record.
    05  ws-record-name                  pic x(10).
    05  ws-record-value                 pic x(10).
    05  ws-record-blank                 pic x(10).
    05  ws-record-flag                  pic x(5) value "false".
        88  ws-record-flag-enabled      value "true".
        88  ws-record-flag-disabled     value "false".
```

**Field Type Mapping:**
- `ws-record-name`: PIC X(10) - Fixed-length alphanumeric string, 10 characters
- `ws-record-value`: PIC X(10) - Fixed-length alphanumeric string, 10 characters
- `ws-record-blank`: PIC X(10) - Fixed-length alphanumeric string, 10 characters (intentionally left blank)
- `ws-record-flag`: PIC X(5) - Fixed-length alphanumeric string, 5 characters, default "false"
  - 88-level `ws-record-flag-enabled`: Condition name for value "true"
  - 88-level `ws-record-flag-disabled`: Condition name for value "false"

**Nested Structures:**
- Single-level nesting: `ws-record` is the parent (01 level), all fields are children (05 level)
- No complex nested hierarchies

**88-Level Condition Names:**
- Used for boolean-like flag values
- `SET ws-record-flag-enabled TO TRUE` sets the underlying field to "true"
- Acts as named constants for specific field values

#### JSON Generation Program (json_generate/json_generate.cbl)

**Data Structure (lines 27-33):**
Identical to XML generation program:
```cobol
01  ws-record.
    05  ws-record-name                  pic x(10).
    05  ws-record-value                 pic x(10).
    05  ws-record-blank                 pic x(10).
    05  ws-record-flag                  pic x(5) value "false".
        88  ws-record-flag-enabled      value "true".
        88  ws-record-flag-disabled     value "false".
```

### COBOL Serialization Features in Use

#### XML Generation Features (xml_generate.cbl, lines 41-56)

1. **Field Name Mapping (lines 45-48):**
   ```cobol
   name of
       ws-record-name is "name",
       ws-record-value is "value",
       ws-record-flag is "enabled"
   ```
   - Maps COBOL field names (with hyphens) to XML element names
   - Prevents hyphenated names in output

2. **XML Attributes (line 49):**
   ```cobol
   type of ws-record-flag is attribute
   ```
   - Converts `ws-record-flag` field to XML attribute instead of element
   - Results in: `<ws-record enabled="true">` instead of `<ws-record><enabled>true</enabled></ws-record>`

3. **Suppression Rules (line 50):**
   ```cobol
   suppress when spaces
   ```
   - Prevents empty/whitespace-only fields from appearing in XML output
   - Critical for COBOL's fixed-length fields which are often padded with spaces

4. **Character Count Tracking (line 43):**
   ```cobol
   count in ws-xml-char-count
   ```
   - Tracks the number of characters written to the output buffer
   - Useful for determining actual content length vs buffer size

5. **XML Declaration (line 44):**
   ```cobol
   with xml-declaration
   ```
   - Includes `<?xml version="1.0"?>` header in output

6. **Exception Handling (lines 51-55):**
   ```cobol
   on exception
       display "Error generating xml error " XML-CODE
       stop run
   not on exception
       display "XML document successfully generated."
   ```
   - Catches serialization errors
   - Provides error code via `XML-CODE` special register
   - Allows graceful error handling vs program crash

#### JSON Generation Features (json_generate.cbl, lines 42-54)

1. **Field Name Mapping (lines 45-48):**
   ```cobol
   name of
       ws-record-name is "name",
       ws-record-value is "value",
       ws-record-flag is "enabled"
   ```
   - Same as XML: maps COBOL field names to JSON property names

2. **Character Count Tracking (line 44):**
   ```cobol
   count in ws-json-char-count
   ```
   - Same as XML: tracks output character count

3. **Exception Handling (lines 49-53):**
   ```cobol
   on exception
       display "Error generating JSON error " JSON-CODE
       stop run
   not on exception
       display "JSON document successfully generated."
   ```
   - Catches serialization errors
   - Provides error code via `JSON-CODE` special register

**Notable Differences from XML:**
- No XML attributes feature (JSON doesn't have attributes)
- No suppression rules specified (though JSON typically omits null values by default)
- No declaration header (JSON has no equivalent to XML declaration)

### Java Type Mapping Design

#### COBOL to Java Type Conversions

| COBOL Type | COBOL Example | Java Type | Rationale |
|------------|---------------|-----------|-----------|
| PIC X(n) | PIC X(10) | String | Variable-length strings in Java, need trimming for COBOL fixed-length |
| PIC 9(n) | PIC 9(4) | int or long | Numeric fields, choose based on size (int for ≤9 digits, long for >9) |
| 88-level condition | value "true"/"false" | boolean | Direct mapping to Java boolean type |

#### Handling Fixed-Length Strings

COBOL `PIC X(n)` fields are always exactly n characters, space-padded if shorter. Java `String` is variable-length.

**Conversion Strategy:**
- **COBOL → Java**: Trim trailing spaces using `String.trim()` or `String.stripTrailing()`
- **Java → COBOL**: Pad with spaces to fixed length, truncate if too long
- **Serialization**: Always trim before serializing to match COBOL's `suppress when spaces` behavior

#### Handling Numeric Types

COBOL `PIC 9(n)` represents n-digit numeric fields.

**Conversion Strategy:**
- `PIC 9(1-9)`: Java `int` (max 2,147,483,647 = 10 digits)
- `PIC 9(10-18)`: Java `long` (max 9,223,372,036,854,775,807 = 19 digits)
- `PIC 9(19+)`: Java `BigInteger` for arbitrary precision

For this migration:
- `ws-xml-char-count` and `ws-json-char-count` are `PIC 9(4)` → Java `int`

#### Handling 88-Level Condition Names

COBOL 88-levels are condition names that test if a field equals a specific value.

**Conversion Strategy:**
- Create Java `boolean` fields for each 88-level condition
- Use getter methods that check the underlying string value
- Use setter methods that update the underlying string value
- Alternative: Use Java enums for multiple condition names on same field

For this migration:
- `ws-record-flag` with 88-levels `ws-record-flag-enabled` and `ws-record-flag-disabled`
- Java approach: Store as `boolean enabled` field, serialize as "true"/"false" string

### Java Serialization Library Selection

#### Library Comparison

| Feature | Jackson | Gson | JAXB |
|---------|---------|------|------|
| JSON Support | ✓ Excellent | ✓ Excellent | ✗ No |
| XML Support | ✓ Via jackson-dataformat-xml | ✗ No | ✓ Excellent |
| Annotations | ✓ Rich | ✓ Simple | ✓ Standard |
| Field Name Mapping | @JsonProperty, @JacksonXmlProperty | @SerializedName | @XmlElement |
| XML Attributes | @JacksonXmlProperty(isAttribute=true) | N/A | @XmlAttribute |
| Null Suppression | @JsonInclude(NON_EMPTY) | excludeFieldsWithoutExposeAnnotation | @XmlElement(nillable=false) |
| Error Handling | Try-catch JsonProcessingException | Try-catch JsonSyntaxException | Try-catch JAXBException |
| Performance | Fast | Fast | Moderate |
| Community | Very Active | Active | Declining (Java 11+ deprecated) |

#### Selected Library: Jackson

**Rationale:**
- **Unified API**: Single library for both JSON and XML serialization
- **Rich Feature Set**: Supports all COBOL features we need (field mapping, attributes, suppression)
- **Active Development**: Well-maintained, modern Java standards
- **Flexibility**: Highly configurable for exact COBOL behavior replication

**Dependencies:**
```xml
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.15.2</version>
</dependency>
<dependency>
    <groupId>com.fasterxml.jackson.dataformat</groupId>
    <artifactId>jackson-dataformat-xml</artifactId>
    <version>2.15.2</version>
</dependency>
```

### Java POJO Design

#### Record Class Design

```java
package com.example.cobol.migration.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JacksonXmlRootElement(localName = "ws-record")
public class Record {
    
    @JsonProperty("name")
    @JacksonXmlProperty(localName = "name")
    private String name;
    
    @JsonProperty("value")
    @JacksonXmlProperty(localName = "value")
    private String value;
    
    @JsonProperty("blank")
    @JacksonXmlProperty(localName = "blank")
    private String blank;
    
    @JsonProperty("enabled")
    @JacksonXmlProperty(localName = "enabled", isAttribute = true)
    private String enabled;
    
    // Constructors, getters, setters
    
    // Helper methods for COBOL compatibility
    public void setEnabledFlag(boolean flag) {
        this.enabled = flag ? "true" : "false";
    }
    
    public boolean isEnabled() {
        return "true".equals(this.enabled);
    }
}
```

#### Data Conversion Utility Design

```java
package com.example.cobol.migration.util;

public class CobolDataConverter {
    
    /**
     * Trims trailing spaces from COBOL fixed-length string
     */
    public static String trimCobolString(String cobolString) {
        return cobolString == null ? null : cobolString.stripTrailing();
    }
    
    /**
     * Pads string to COBOL fixed length
     */
    public static String padCobolString(String javaString, int length) {
        if (javaString == null) {
            return " ".repeat(length);
        }
        if (javaString.length() >= length) {
            return javaString.substring(0, length);
        }
        return javaString + " ".repeat(length - javaString.length());
    }
    
    /**
     * Converts COBOL numeric string to Java int
     */
    public static int cobolNumericToInt(String cobolNumeric) {
        return Integer.parseInt(cobolNumeric.trim());
    }
    
    /**
     * Converts Java int to COBOL numeric string with leading zeros
     */
    public static String intToCobolNumeric(int value, int length) {
        return String.format("%0" + length + "d", value);
    }
}
```

### Summary of Phase 1 Findings

**COBOL Data Structures:**
- Simple single-level record structure with 4 fields
- Fixed-length string fields (PIC X)
- Numeric counter fields (PIC 9)
- Boolean-like 88-level condition names

**COBOL Serialization Features:**
- Field name mapping (hyphenated COBOL names → clean JSON/XML names)
- XML attributes (flag field becomes attribute)
- Suppression of empty/space-filled fields
- Character count tracking
- Exception handling with error codes

**Java Design Decisions:**
- Use Jackson for both JSON and XML (unified API)
- Map PIC X(n) → String with trimming utilities
- Map PIC 9(n) → int/long based on size
- Map 88-level conditions → boolean fields with helper methods
- Use annotations for field mapping, attributes, and suppression
- Implement try-catch for exception handling

**Next Steps (Phase 2):**
- Create Maven project structure
- Implement Java data classes with Jackson annotations
- Implement serialization methods
- Implement data conversion utilities
- Add error handling
