# COBOL to Java Migration

This project contains Java implementations of the COBOL demonstration programs from the COBOL-Demo repository.

## Overview

This migration converts 5 COBOL programs to Java:

1. **JSON Generation** - Demonstrates JSON serialization using Jackson
2. **XML Generation** - Demonstrates XML serialization using JAXB
3. **SQL Database Operations** - Demonstrates PostgreSQL database connectivity using JDBC
4. **Data Structure Redefines** - Demonstrates union-like behavior using Java design patterns
5. **Subprogram Calls** - Demonstrates method invocation with pass-by-value and pass-by-reference semantics

## Prerequisites

- Java 11 or higher
- Maven 3.6 or higher
- PostgreSQL database (for SQL examples)

## Project Structure

```
java-migration/
├── pom.xml
├── README.md
└── src/main/java/com/cobol/demo/
    ├── json/
    │   ├── Record.java
    │   └── JsonGenerateExample.java
    ├── xml/
    │   ├── XmlRecord.java
    │   └── XmlGenerateExample.java
    ├── sql/
    │   ├── Account.java
    │   └── SqlExample.java
    ├── redefines/
    │   ├── Customer.java
    │   ├── DataType.java
    │   └── RedefinesTest.java
    └── subprogram/
        ├── SubApp.java
        └── MainApp.java
```

## Building the Project

To compile all Java classes:

```bash
cd java-migration
mvn clean compile
```

To package the project as a JAR:

```bash
mvn clean package
```

## Running the Programs

### 1. JSON Generation Example

```bash
mvn exec:java -Dexec.mainClass="com.cobol.demo.json.JsonGenerateExample"
```

This program demonstrates:
- Converting Java POJOs to JSON using Jackson
- Custom field name mapping with `@JsonProperty` annotations
- Boolean flag handling (enabled/disabled)

### 2. XML Generation Example

```bash
mvn exec:java -Dexec.mainClass="com.cobol.demo.xml.XmlGenerateExample"
```

This program demonstrates:
- Converting Java POJOs to XML using JAXB
- XML element and attribute mapping
- XML declaration generation

### 3. SQL Database Example

**Prerequisites**: PostgreSQL database must be running with the test database created.

First, create the database using the SQL script from the original COBOL project:

```bash
psql -U postgres -f ../sql/create_test_db.sql
```

Then run the Java program:

```bash
mvn exec:java -Dexec.mainClass="com.cobol.demo.sql.SqlExample"
```

This program demonstrates:
- JDBC database connectivity
- PreparedStatement for parameterized queries
- ResultSet iteration (equivalent to COBOL cursors)
- Menu-driven console application
- SQL error handling

**Note**: Update the database connection parameters in `SqlExample.java` if your PostgreSQL configuration differs:
- Database URL: `jdbc:postgresql://localhost:5432/cobol_db_example`
- Username: `postgres`
- Password: `password`

### 4. Redefines Pattern Example

```bash
mvn exec:java -Dexec.mainClass="com.cobol.demo.redefines.RedefinesTest"
```

This program demonstrates:
- Union-like behavior using Java composition
- Multiple interpretations of the same data (person name vs. corporate name)
- Type-based data handling (display vs. computational)

### 5. Subprogram Calls Example

```bash
mvn exec:java -Dexec.mainClass="com.cobol.demo.subprogram.MainApp"
```

This program demonstrates:
- Pass-by-value semantics (COBOL BY CONTENT)
- Pass-by-reference semantics (COBOL BY REFERENCE) using StringBuilder
- Working storage persistence across calls
- Local storage re-initialization
- Program cancellation and reset

## Key Migration Patterns

### COBOL to Java Data Type Mappings

| COBOL Type | Java Type |
|------------|-----------|
| PIC X(n) | String |
| PIC 9(n) | int or long |
| PIC X | char |
| COMP-2 | double |

### COBOL to Java Control Flow Mappings

| COBOL Statement | Java Equivalent |
|-----------------|-----------------|
| PERFORM paragraph-name | Method call |
| PERFORM UNTIL | while loop |
| PERFORM VARYING | for loop |
| EVALUATE | switch expression |
| DISPLAY | System.out.println() |
| ACCEPT | Scanner.nextLine() |

### COBOL to Java I/O Mappings

| COBOL Statement | Java Equivalent |
|-----------------|-----------------|
| JSON GENERATE | Jackson ObjectMapper.writeValueAsString() |
| XML GENERATE | JAXB Marshaller.marshal() |
| EXEC SQL CONNECT | DriverManager.getConnection() |
| DECLARE CURSOR | PreparedStatement |
| FETCH | ResultSet.next() |
| CALL "program" | Method invocation |

## Dependencies

The project uses the following dependencies (managed by Maven):

- **Jackson 2.15.2** - JSON serialization
  - jackson-databind
  - jackson-core
  - jackson-annotations

- **JAXB 3.0** - XML serialization
  - jakarta.xml.bind-api
  - jaxb-impl

- **PostgreSQL JDBC 42.6.0** - Database connectivity

## Notes

### String Handling

COBOL uses fixed-length strings (PIC X(n)) while Java uses dynamic strings. The migration handles this by:
- Trimming strings when necessary
- Padding strings to match expected display widths
- Using explicit length calculations for SQL queries

### REDEFINES Pattern

COBOL's REDEFINES clause creates memory overlays where the same memory location can be interpreted as different data types. In Java, this is implemented using:
- Separate fields with getter/setter methods that manipulate the underlying data
- Type flags to indicate which interpretation is active
- Composition patterns to handle union-like behavior

### Subprogram Calls

COBOL supports both BY CONTENT (pass-by-value) and BY REFERENCE (pass-by-pointer) parameter passing. In Java:
- BY CONTENT is implemented using immutable String parameters
- BY REFERENCE is implemented using mutable StringBuilder objects
- Working storage persistence is implemented using instance variables
- Local storage re-initialization is implemented using local variables

### SQL Operations

COBOL's embedded SQL (EXEC SQL) is replaced with JDBC:
- CONNECT TO becomes DriverManager.getConnection()
- DECLARE CURSOR becomes PreparedStatement
- FETCH becomes ResultSet.next()
- SQLCODE/SQLSTATE become SQLException properties

## Original COBOL Programs

The original COBOL programs can be found in the parent directory:
- `../json_generate/json_generate.cbl`
- `../xml_generate/xml_generate.cbl`
- `../sql/sql_example.cbl`
- `../redifines/redefines.cbl`
- `../sub_program/main_app.cbl` and `../sub_program/sub.cbl`

## License

This project follows the same license as the original COBOL-Demo repository (MIT License).
