# Serialization Requirements Document

This document specifies all serialization behaviors from the COBOL implementation that must be preserved in the Java migration.

## Overview

The COBOL programs use GnuCOBOL's built-in `JSON GENERATE` and `XML GENERATE` commands, which depend on external libraries (libjson-c and libxml2 respectively). The Java implementation must replicate all functional behaviors while eliminating these native library dependencies.

## JSON Serialization Requirements

### Source Reference
- File: `json_generate/json_generate.cbl`
- Relevant lines: 42-54

### R-JSON-001: Field Name Mapping

**COBOL Implementation (lines 45-48):**
```cobol
name of
    ws-record-name is "name",
    ws-record-value is "value",
    ws-record-flag is "enabled"
```

**Requirement:** The Java implementation must map COBOL field names to JSON property names as follows:

| COBOL Field Name | JSON Property Name |
|------------------|-------------------|
| ws-record-name | name |
| ws-record-value | value |
| ws-record-flag | enabled |
| ws-record-blank | ws-record-blank (unmapped, uses default) |

**Java Implementation:** Use `@JsonProperty` annotations on POJO fields.

### R-JSON-002: Character Count Tracking

**COBOL Implementation (line 44):**
```cobol
count in ws-json-char-count
```

**Requirement:** The Java implementation must provide a mechanism to retrieve the character count of the generated JSON output.

**Java Implementation:** Return the length of the serialized JSON string via `String.length()`.

### R-JSON-003: Error Handling

**COBOL Implementation (lines 49-51):**
```cobol
on exception
    display "Error generating JSON error " JSON-CODE
    stop run
```

**Requirement:** The Java implementation must:
1. Catch serialization exceptions
2. Provide error information equivalent to JSON-CODE
3. Support graceful error handling (not necessarily program termination)

**Java Implementation:** Use try-catch blocks with `JsonProcessingException` and custom exception handling.

### R-JSON-004: Success Notification

**COBOL Implementation (lines 52-53):**
```cobol
not on exception
    display "JSON document successfully generated."
```

**Requirement:** The Java implementation should support success callbacks or return values indicating successful serialization.

**Java Implementation:** Return the serialized string on success; throw exception on failure.

### R-JSON-005: Output Format

**COBOL Implementation (line 58):**
```cobol
display function trim(ws-json-output)
```

**Requirement:** The JSON output should be compact (no unnecessary whitespace) and properly trimmed.

**Expected Output Format:**
```json
{"name":"Test Name","value":"Test Value","ws-record-blank":"","enabled":true}
```

**Java Implementation:** Use Jackson's default compact serialization.

## XML Serialization Requirements

### Source Reference
- File: `xml_generate/xml_generate.cbl`
- Relevant lines: 41-56

### R-XML-001: Field Name Mapping

**COBOL Implementation (lines 45-48):**
```cobol
name of
    ws-record-name is "name",
    ws-record-value is "value",
    ws-record-flag is "enabled"
```

**Requirement:** The Java implementation must map COBOL field names to XML element/attribute names as follows:

| COBOL Field Name | XML Name |
|------------------|----------|
| ws-record-name | name |
| ws-record-value | value |
| ws-record-flag | enabled |
| ws-record-blank | ws-record-blank (unmapped, uses default) |

**Java Implementation:** Use `@XmlElement` and `@XmlAttribute` annotations.

### R-XML-002: XML Declaration

**COBOL Implementation (line 44):**
```cobol
with xml-declaration
```

**Requirement:** The generated XML must include a standard XML declaration header.

**Expected Format:**
```xml
<?xml version="1.0" encoding="UTF-8"?>
```

**Java Implementation:** Configure JAXB Marshaller with `Marshaller.JAXB_FRAGMENT` set to `false`.

### R-XML-003: Attribute Type Specification

**COBOL Implementation (line 49):**
```cobol
type of ws-record-flag is attribute
```

**Requirement:** The `ws-record-flag` field (mapped to "enabled") must be serialized as an XML attribute on the root element, not as a child element.

**Expected Output:**
```xml
<ws-record enabled="true">
    <name>Test Name</name>
    <value>Test Value</value>
</ws-record>
```

**Java Implementation:** Use `@XmlAttribute(name = "enabled")` annotation on the flag field.

### R-XML-004: Suppress When Spaces

**COBOL Implementation (line 50):**
```cobol
suppress when spaces
```

**Requirement:** Fields containing only spaces (empty in COBOL terms) must be excluded from the XML output.

**Behavior:**
- If `ws-record-blank` contains only spaces, it should NOT appear in the XML output
- If `ws-record-blank` contains any non-space characters, it should appear in the XML output

**Java Implementation:** 
- Use custom JAXB adapter or `@XmlElement(nillable = false)` with null handling
- Convert empty/whitespace-only strings to null before serialization
- Configure JAXB to exclude null elements

### R-XML-005: Character Count Tracking

**COBOL Implementation (line 43):**
```cobol
count in ws-xml-char-count
```

**Requirement:** The Java implementation must provide a mechanism to retrieve the character count of the generated XML output.

**Java Implementation:** Return the length of the serialized XML string via `String.length()`.

### R-XML-006: Error Handling

**COBOL Implementation (lines 51-53):**
```cobol
on exception
    display "Error generating xml error " XML-CODE
    stop run
```

**Requirement:** The Java implementation must:
1. Catch serialization exceptions
2. Provide error information equivalent to XML-CODE
3. Support graceful error handling

**Java Implementation:** Use try-catch blocks with `JAXBException` and custom exception handling.

### R-XML-007: Root Element Name

**Requirement:** The root XML element must be named "ws-record" (matching the COBOL group item name).

**Java Implementation:** Use `@XmlRootElement(name = "ws-record")` annotation.

## Common Requirements

### R-COMMON-001: String Trimming

**Requirement:** All string values must be trimmed of trailing spaces before serialization. COBOL PIC X fields are fixed-length and padded with spaces.

**Java Implementation:** Apply `String.trim()` to all string fields before serialization, or use custom serializers.

### R-COMMON-002: Boolean Representation

**Requirement:** The flag field must be serialized as a boolean value:
- COBOL "true " (with trailing space) -> JSON/XML `true`
- COBOL "false" -> JSON/XML `false`

**Java Implementation:** Use native `boolean` type in the POJO.

### R-COMMON-003: Encoding

**Requirement:** Output must use UTF-8 encoding (standard for both JSON and XML).

**Java Implementation:** Configure serializers to use UTF-8 encoding explicitly.

### R-COMMON-004: Library Independence

**COBOL Dependencies (to be eliminated):**
- `json_generate/json_generate.cbl` lines 7-12: libjson-c dependency
- `xml_generate/xml_generate.cbl` lines 7-11: libxml2 dependency

**Requirement:** The Java implementation must not depend on native C libraries. All serialization must be handled by pure Java libraries.

**Java Implementation:** 
- JSON: Jackson (com.fasterxml.jackson)
- XML: JAXB (jakarta.xml.bind) or Jackson XML module

## Functional Equivalence Test Cases

### Test Case 1: JSON Generation with Sample Data

**Input:**
```
ws-record-name = "Test Name"
ws-record-value = "Test Value"
ws-record-blank = "          " (spaces)
ws-record-flag = "true "
```

**Expected JSON Output:**
```json
{"name":"Test Name","value":"Test Value","ws-record-blank":"","enabled":true}
```

### Test Case 2: XML Generation with Sample Data

**Input:**
```
ws-record-name = "Test Name"
ws-record-value = "Test Value"
ws-record-blank = "          " (spaces)
ws-record-flag = "true "
```

**Expected XML Output:**
```xml
<?xml version="1.0" encoding="UTF-8"?>
<ws-record enabled="true"><name>Test Name</name><value>Test Value</value></ws-record>
```

Note: `ws-record-blank` is suppressed because it contains only spaces.

### Test Case 3: XML Generation with Non-Empty Blank Field

**Input:**
```
ws-record-name = "Test Name"
ws-record-value = "Test Value"
ws-record-blank = "Has Data  "
ws-record-flag = "false"
```

**Expected XML Output:**
```xml
<?xml version="1.0" encoding="UTF-8"?>
<ws-record enabled="false"><name>Test Name</name><value>Test Value</value><ws-record-blank>Has Data</ws-record-blank></ws-record>
```

## Non-Functional Requirements

### R-NFR-001: Performance

The Java implementation should provide comparable or better performance than the COBOL/native library implementation for typical record sizes.

### R-NFR-002: Thread Safety

The Java serialization utilities should be thread-safe and suitable for use in multi-threaded applications.

### R-NFR-003: Maintainability

The Java implementation should follow standard Java conventions and be easily maintainable by Java developers unfamiliar with COBOL.
