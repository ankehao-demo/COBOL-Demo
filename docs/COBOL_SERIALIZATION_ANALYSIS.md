# COBOL Data Serialization Analysis Document

## Overview

This document provides a comprehensive analysis of the COBOL data serialization patterns found in the `json_generate/json_generate.cbl` and `xml_generate/xml_generate.cbl` files. This analysis serves as the foundation for migrating these serialization capabilities to Java.

## 1. JSON Serialization Analysis

### 1.1 Source File Information

**File:** `json_generate/json_generate.cbl`
**Author:** Erik Eriksen
**Date:** 2022-04-12
**Purpose:** Example of using the JSON GENERATE command to create JSON documents from COBOL records

### 1.2 Library Dependencies

The JSON serialization functionality requires:

- **libjson-c**: The JSON-C library (https://github.com/json-c/json-c)
- **GnuCOBOL Configuration**: Must be compiled with `./configure --with-json` flag
- **Runtime Verification**: `cobcrun --info` should display `JSON library: json-c, version 0.15.99`

### 1.3 Data Structure Definition (Lines 27-33)

```cobol
01  ws-record.
    05  ws-record-name                  pic x(10).
    05  ws-record-value                 pic x(10).
    05  ws-record-blank                 pic x(10).
    05  ws-record-flag                  pic x(5) value "false".
        88  ws-record-flag-enabled      value "true".
        88  ws-record-flag-disabled     value "false".
```

**Data Type Analysis:**

| Field Name | COBOL Type | Size | Java Equivalent | Description |
|------------|------------|------|-----------------|-------------|
| ws-record-name | PIC X(10) | 10 chars | String | Alphanumeric string, fixed-length |
| ws-record-value | PIC X(10) | 10 chars | String | Alphanumeric string, fixed-length |
| ws-record-blank | PIC X(10) | 10 chars | String | Alphanumeric string, may contain spaces |
| ws-record-flag | PIC X(5) | 5 chars | String/Boolean | Boolean-like flag with 88-level conditions |

**88-Level Conditions (Boolean Flags):**

The 88-level conditions define named values for the parent field:
- `ws-record-flag-enabled` maps to value "true"
- `ws-record-flag-disabled` maps to value "false"

These are used with the `SET` statement: `set ws-record-flag-enabled to true` sets `ws-record-flag` to "true".

**Java Mapping Consideration:** The 88-level conditions suggest this field should be mapped to a Java `boolean` type, with serialization converting to/from string representation.

### 1.4 Output Buffer Definition (Lines 23-25)

```cobol
01  ws-json-output                       pic x(256).
01  ws-json-char-count                   pic 9(4).
```

| Field Name | COBOL Type | Size | Purpose |
|------------|------------|------|---------|
| ws-json-output | PIC X(256) | 256 chars | Buffer to hold generated JSON string |
| ws-json-char-count | PIC 9(4) | 4 digits | Stores the actual character count of generated JSON |

### 1.5 Test Data (Lines 38-40)

```cobol
move "Test Name" to ws-record-name
move "Test Value" to ws-record-value
set ws-record-flag-enabled to true
```

Note: `ws-record-blank` is intentionally left uninitialized (contains spaces).

### 1.6 JSON GENERATE Command (Lines 42-54)

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

**Command Components:**

| Component | Syntax | Purpose |
|-----------|--------|---------|
| Target | `ws-json-output` | Destination buffer for JSON string |
| Source | `from ws-record` | COBOL record to serialize |
| Character Count | `count in ws-json-char-count` | Tracks actual output length |
| Field Mapping | `name of ws-record-name is "name"` | Renames fields in JSON output |
| Error Handler | `on exception` | Handles serialization errors |
| Success Handler | `not on exception` | Executes on successful generation |

**Field Name Mappings:**

| COBOL Field | JSON Field Name | Mapping Type |
|-------------|-----------------|--------------|
| ws-record-name | "name" | Explicit rename |
| ws-record-value | "value" | Explicit rename |
| ws-record-blank | "ws-record-blank" | Default (no mapping) |
| ws-record-flag | "enabled" | Explicit rename |

### 1.7 Error Handling

- **JSON-CODE**: Special register containing error code when exception occurs
- **ON EXCEPTION**: Block executed when JSON generation fails
- **NOT ON EXCEPTION**: Block executed on successful generation

### 1.8 Example Output

```json
{"ws-record":{"name":"Test Name","value":"Test Value","ws-record-blank":" ","enabled":"true"}}
```

**Output Characteristics:**
- Root element uses the COBOL record name (`ws-record`)
- Mapped fields use their specified names
- Unmapped fields retain COBOL names with hyphens
- All values are serialized as strings (including boolean-like flags)
- Character count: 94

---

## 2. XML Serialization Analysis

### 2.1 Source File Information

**File:** `xml_generate/xml_generate.cbl`
**Author:** Erik Eriksen
**Date:** 2022-04-11
**Purpose:** Example of using the XML GENERATE command to create XML documents from COBOL records

### 2.2 Library Dependencies

The XML serialization functionality requires:

- **libxml2**: The GNOME XML library (https://github.com/GNOME/libxml2)
- **GnuCOBOL Configuration**: Must be compiled with `./configure --with-xml2` flag
- **Runtime Verification**: `cobcrun --info` should display `XML library: libxml2, version 2.9.3`

### 2.3 Data Structure Definition (Lines 26-32)

The XML example uses the identical data structure as JSON:

```cobol
01  ws-record.
    05  ws-record-name                  pic x(10).
    05  ws-record-value                 pic x(10).
    05  ws-record-blank                 pic x(10).
    05  ws-record-flag                  pic x(5) value "false".
        88  ws-record-flag-enabled      value "true".
        88  ws-record-flag-disabled     value "false".
```

### 2.4 Output Buffer Definition (Lines 22-24)

```cobol
01  ws-xml-output                       pic x(256).
01  ws-xml-char-count                   pic 9(4).
```

### 2.5 Test Data (Lines 37-39)

```cobol
move "Test Name" to ws-record-name
move "Test Value" to ws-record-value
set ws-record-flag-enabled to true
```

### 2.6 XML GENERATE Command (Lines 41-56)

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

**Command Components:**

| Component | Syntax | Purpose |
|-----------|--------|---------|
| Target | `ws-xml-output` | Destination buffer for XML string |
| Source | `from ws-record` | COBOL record to serialize |
| Character Count | `count in ws-xml-char-count` | Tracks actual output length |
| XML Declaration | `with xml-declaration` | Adds `<?xml version="1.0"?>` header |
| Field Mapping | `name of ws-record-name is "name"` | Renames fields in XML output |
| Type Specification | `type of ws-record-flag is attribute` | Renders field as XML attribute |
| Blank Suppression | `suppress when spaces` | Omits fields containing only spaces |
| Error Handler | `on exception` | Handles serialization errors |
| Success Handler | `not on exception` | Executes on successful generation |

### 2.7 XML-Specific Features

#### 2.7.1 XML Declaration

The `with xml-declaration` clause adds the standard XML prolog:
```xml
<?xml version="1.0"?>
```

#### 2.7.2 TYPE OF Clause (Attribute vs Element)

The `type of ws-record-flag is attribute` clause changes how the field is rendered:

- **Without TYPE OF**: Field becomes a child element `<enabled>true</enabled>`
- **With TYPE OF ATTRIBUTE**: Field becomes an attribute `enabled="true"` on the parent element

#### 2.7.3 SUPPRESS WHEN SPACES

The `suppress when spaces` clause prevents fields containing only whitespace from appearing in the output. In the example, `ws-record-blank` is omitted because it contains only spaces.

### 2.8 Error Handling

- **XML-CODE**: Special register containing error code when exception occurs
- **ON EXCEPTION**: Block executed when XML generation fails
- **NOT ON EXCEPTION**: Block executed on successful generation

### 2.9 Example Output

```xml
<?xml version="1.0"?>
<ws-record enabled="true"><name>Test Name</name><value>Test Value</value></ws-record>
```

**Output Characteristics:**
- Includes XML declaration
- Root element uses the COBOL record name (`ws-record`)
- `enabled` is rendered as an attribute (due to TYPE OF clause)
- `ws-record-blank` is suppressed (due to SUPPRESS WHEN SPACES)
- Mapped fields use their specified names as element tags
- Character count: 107

---

## 3. Common Patterns Summary

### 3.1 Data Type Mappings

| COBOL Type | Description | Java Equivalent |
|------------|-------------|-----------------|
| PIC X(n) | Alphanumeric string, n characters | String (with trimming consideration) |
| PIC 9(n) | Numeric, n digits | int or long |
| PIC 9(n)V9(m) | Decimal with implied point | BigDecimal |
| 88-level | Condition name (boolean-like) | boolean |

### 3.2 Field Naming Conventions

COBOL uses hyphenated names (e.g., `ws-record-name`) which are not ideal for JSON/XML. The `NAME OF` clause provides explicit mapping to cleaner names.

**Java Implementation Strategy:**
- Use `@JsonProperty("name")` for Jackson JSON serialization
- Use `@XmlElement(name = "name")` for JAXB XML serialization

### 3.3 Special Features to Implement

| Feature | COBOL Syntax | Java Implementation |
|---------|--------------|---------------------|
| Field Renaming | `name of X is "y"` | Jackson/JAXB annotations |
| XML Attributes | `type of X is attribute` | `@XmlAttribute` annotation |
| Blank Suppression | `suppress when spaces` | Custom serializer or `@JsonInclude(NON_EMPTY)` |
| Character Count | `count in X` | Return value or wrapper object |
| Error Handling | `on exception` | Try-catch with custom exceptions |

### 3.4 Character Encoding

- COBOL uses EBCDIC or ASCII depending on platform
- GnuCOBOL on Linux uses ASCII/UTF-8
- Java should use UTF-8 encoding for consistency

### 3.5 Fixed-Length Field Handling

COBOL fields are fixed-length and padded with spaces. Java implementation should:
- Trim trailing spaces when serializing
- Consider whether to preserve or trim leading spaces
- Handle the `SUPPRESS WHEN SPACES` behavior

---

## 4. Java Implementation Recommendations

### 4.1 POJO Design

```java
public class Record {
    private String name;           // ws-record-name
    private String value;          // ws-record-value
    private String blank;          // ws-record-blank
    private boolean enabled;       // ws-record-flag (with 88-level mapping)
}
```

### 4.2 JSON Serialization (Jackson)

- Use `ObjectMapper` for JSON generation
- Apply `@JsonProperty` annotations for field name mapping
- Use `@JsonInclude(Include.NON_EMPTY)` for blank suppression equivalent
- Implement custom serializer if exact COBOL output format is required

### 4.3 XML Serialization (JAXB)

- Use `@XmlRootElement` for root element naming
- Use `@XmlElement` for child element naming
- Use `@XmlAttribute` for attribute-type fields
- Implement custom adapter for blank suppression

### 4.4 Error Handling

Create custom exceptions to mirror COBOL's JSON-CODE and XML-CODE:
- `JsonSerializationException` with error code
- `XmlSerializationException` with error code

### 4.5 Character Count Tracking

Implement serialization methods that return both the serialized string and its character count, similar to COBOL's `COUNT IN` clause.

---

## 5. Migration Checklist

### Phase 1 (Current)
- [x] Analyze JSON GENERATE patterns
- [x] Analyze XML GENERATE patterns
- [x] Document data structures
- [x] Document field mappings
- [x] Document special features
- [x] Set up Java project structure

### Phase 2 (Future)
- [ ] Implement Record POJO with annotations
- [ ] Implement JSON serializer
- [ ] Implement XML serializer
- [ ] Implement error handling
- [ ] Write unit tests
- [ ] Validate output matches COBOL examples
