# COBOL to Java Data Structure Mapping

This document provides a detailed mapping of COBOL data structures used in the serialization programs to their proposed Java equivalents.

## Overview

The COBOL programs `json_generate/json_generate.cbl` and `xml_generate/xml_generate.cbl` share identical data structures for the record being serialized. This document analyzes these structures and proposes equivalent Java representations.

## COBOL Data Structures Analysis

### Primary Record Structure: `ws-record`

Both COBOL programs define the same hierarchical record structure in their WORKING-STORAGE SECTION.

**Source Location:**
- `json_generate/json_generate.cbl` lines 27-33
- `xml_generate/xml_generate.cbl` lines 26-32

**COBOL Definition:**
```cobol
01  ws-record.
    05  ws-record-name                  pic x(10).
    05  ws-record-value                 pic x(10).
    05  ws-record-blank                 pic x(10).
    05  ws-record-flag                  pic x(5) value "false".
        88  ws-record-flag-enabled      value "true".
        88  ws-record-flag-disabled     value "false".
```

### Field-by-Field Analysis

#### Field 1: `ws-record-name`

| Attribute | COBOL Value | Notes |
|-----------|-------------|-------|
| Level | 05 | Child of ws-record (level 01) |
| Picture Clause | PIC X(10) | Alphanumeric, 10 characters |
| Data Type | Alphanumeric string | Fixed-length character field |
| Initial Value | Spaces | COBOL default for PIC X fields |
| Serialized Name | "name" | Mapped via NAME OF clause |

**Java Equivalent:** `String` with max length validation of 10 characters. The field will be trimmed during serialization to remove trailing spaces (COBOL pads with spaces).

#### Field 2: `ws-record-value`

| Attribute | COBOL Value | Notes |
|-----------|-------------|-------|
| Level | 05 | Child of ws-record (level 01) |
| Picture Clause | PIC X(10) | Alphanumeric, 10 characters |
| Data Type | Alphanumeric string | Fixed-length character field |
| Initial Value | Spaces | COBOL default for PIC X fields |
| Serialized Name | "value" | Mapped via NAME OF clause |

**Java Equivalent:** `String` with max length validation of 10 characters.

#### Field 3: `ws-record-blank`

| Attribute | COBOL Value | Notes |
|-----------|-------------|-------|
| Level | 05 | Child of ws-record (level 01) |
| Picture Clause | PIC X(10) | Alphanumeric, 10 characters |
| Data Type | Alphanumeric string | Fixed-length character field |
| Initial Value | Spaces | COBOL default for PIC X fields |
| Serialized Name | "ws-record-blank" | No NAME OF mapping (uses default) |

**Java Equivalent:** `String` with max length validation of 10 characters. This field demonstrates the SUPPRESS WHEN SPACES behavior in XML serialization - when the field contains only spaces, it is excluded from XML output.

#### Field 4: `ws-record-flag`

| Attribute | COBOL Value | Notes |
|-----------|-------------|-------|
| Level | 05 | Child of ws-record (level 01) |
| Picture Clause | PIC X(5) | Alphanumeric, 5 characters |
| Data Type | Alphanumeric string | Fixed-length character field |
| Initial Value | "false" | Explicitly initialized |
| Serialized Name | "enabled" | Mapped via NAME OF clause |
| 88-Level Conditions | ws-record-flag-enabled ("true"), ws-record-flag-disabled ("false") | Boolean-like behavior |

**Java Equivalent:** `boolean` type. The 88-level conditions in COBOL provide boolean semantics, making this a natural fit for Java's primitive `boolean` type. The string values "true" and "false" will be converted to native boolean values.

### Output Buffer Structures

#### JSON Output Buffer

**Source:** `json_generate/json_generate.cbl` lines 23-25

```cobol
01  ws-json-output                       pic x(256).
01  ws-json-char-count                   pic 9(4).
```

| Field | COBOL Type | Size | Java Equivalent |
|-------|------------|------|-----------------|
| ws-json-output | PIC X(256) | 256 chars | `String` (dynamically sized) |
| ws-json-char-count | PIC 9(4) | 4 digits (0-9999) | `int` (for length tracking) |

#### XML Output Buffer

**Source:** `xml_generate/xml_generate.cbl` lines 22-24

```cobol
01  ws-xml-output                       pic x(256).
01  ws-xml-char-count                   pic 9(4).
```

| Field | COBOL Type | Size | Java Equivalent |
|-------|------------|------|-----------------|
| ws-xml-output | PIC X(256) | 256 chars | `String` (dynamically sized) |
| ws-xml-char-count | PIC 9(4) | 4 digits (0-9999) | `int` (for length tracking) |

## Proposed Java Data Model

### Primary POJO: `Record.java`

```java
package com.example.serialization.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.xml.bind.annotation.*;

@XmlRootElement(name = "ws-record")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Record {

    @JsonProperty("name")
    @XmlElement(name = "name")
    private String name;

    @JsonProperty("value")
    @XmlElement(name = "value")
    private String value;

    @JsonProperty("ws-record-blank")
    @XmlElement(name = "ws-record-blank")
    private String blank;

    @JsonProperty("enabled")
    @XmlAttribute(name = "enabled")
    private boolean enabled;

    // Constructors, getters, setters, and validation methods
}
```

### Type Mapping Summary

| COBOL Type | COBOL Example | Java Type | Notes |
|------------|---------------|-----------|-------|
| PIC X(n) | PIC X(10) | String | Trimmed, max length n |
| PIC 9(n) | PIC 9(4) | int | For small numeric values |
| 88-level conditions | 88 ws-flag-enabled | boolean | Boolean semantics |
| Level 01 group | 01 ws-record | Class (POJO) | Encapsulating class |
| Level 05 elementary | 05 ws-field | Field | Class member |

### Hierarchical Structure Mapping

```
COBOL Structure                    Java Structure
----------------                   --------------
01 ws-record                  -->  class Record
   |                                  |
   +-- 05 ws-record-name      -->     +-- String name
   |                                  |
   +-- 05 ws-record-value     -->     +-- String value
   |                                  |
   +-- 05 ws-record-blank     -->     +-- String blank
   |                                  |
   +-- 05 ws-record-flag      -->     +-- boolean enabled
       |                              
       +-- 88 ws-record-flag-enabled   (handled by boolean type)
       +-- 88 ws-record-flag-disabled  (handled by boolean type)
```

## Data Transformation Considerations

### String Handling

COBOL PIC X fields are fixed-length and padded with trailing spaces. When converting to Java:

1. **Trimming:** All string values should be trimmed using `String.trim()` before serialization
2. **Empty Detection:** A field containing only spaces in COBOL is considered "empty" and may be suppressed
3. **Max Length:** Validation should enforce maximum lengths matching COBOL field sizes

### Boolean Conversion

The COBOL 88-level conditions provide boolean semantics through string values:
- "true" (with trailing space to fill 5 chars) maps to Java `true`
- "false" maps to Java `false`

The Java implementation should use native `boolean` type, with Jackson/JAXB handling the serialization to "true"/"false" strings automatically.

### Null Handling

COBOL does not have a concept of null - fields always contain data (spaces for alphanumeric). In Java:
- Empty/blank strings should be treated as equivalent to COBOL spaces
- The `@JsonInclude(JsonInclude.Include.NON_EMPTY)` annotation handles suppression of empty values in JSON
- Custom JAXB adapters may be needed for XML suppression behavior
