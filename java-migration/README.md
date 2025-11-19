# COBOL to Java Migration

This project is a Java migration of the COBOL application from the parent repository. It demonstrates how to convert COBOL programs with database integration, XML/JSON generation, and various COBOL programming patterns to modern Java.

## Overview

This migration includes:

- **Database Operations**: PostgreSQL connectivity using JDBC, replacing COBOL's embedded SQL
- **Data Structures**: Java classes representing COBOL records and data structures
- **XML Serialization**: JAXB-based XML generation replacing COBOL's XML GENERATE
- **JSON Serialization**: Jackson-based JSON generation replacing COBOL's JSON GENERATE
- **Search Patterns**: Binary and sequential search implementations
- **REDEFINES Pattern**: Inheritance-based approach to union-like behavior

## Project Structure

```
java-migration/
├── pom.xml                                    # Maven build configuration
├── src/main/java/com/example/cobol/
│   ├── model/                                 # Data model classes
│   │   ├── Account.java                       # Account record (from sql_example.cbl)
│   │   ├── Customer.java                      # Base customer class
│   │   ├── PersonCustomer.java                # Person customer (from redefines.cbl)
│   │   ├── CorpCustomer.java                  # Corporate customer (from redefines.cbl)
│   │   └── Record.java                        # Generic record for XML/JSON examples
│   ├── database/                              # Database layer
│   │   ├── DatabaseConnection.java            # Connection management
│   │   └── AccountRepository.java             # Account data access
│   ├── util/                                  # Utility classes
│   │   ├── XmlSerializer.java                 # XML serialization
│   │   ├── JsonSerializer.java                # JSON serialization
│   │   └── SearchUtil.java                    # Search utilities
│   ├── SqlExampleApp.java                     # Main SQL application (from sql_example.cbl)
│   ├── XmlGenerateExample.java                # XML generation demo (from xml_generate.cbl)
│   ├── JsonGenerateExample.java               # JSON generation demo (from json_generate.cbl)
│   ├── SearchExample.java                     # Search demo (from search.cbl)
│   └── RedefinesExample.java                  # REDEFINES pattern demo (from redefines.cbl)
└── README.md                                  # This file
```

## Prerequisites

- Java 11 or higher
- Maven 3.6 or higher
- PostgreSQL database (for SQL examples)

## Setup

### 1. Build the Project

```bash
cd java-migration
mvn clean compile
```

### 2. Database Setup (for SQL Examples)

If you want to run the SQL example application, you need to set up the PostgreSQL database:

```bash
# Create the database and populate test data
psql -U postgres -f ../sql/create_test_db.sql
```

The default connection settings are:
- **URL**: `jdbc:postgresql://localhost:5432/cobol_db_example`
- **User**: `postgres`
- **Password**: `password`

You can modify these in `DatabaseConnection.java` if needed.

## Running the Applications

### SQL Example Application

This is the main application that demonstrates database operations with a menu-driven interface.

```bash
mvn exec:java -Dexec.mainClass="com.example.cobol.SqlExampleApp"
```

**Features:**
- Display all accounts
- Display disabled accounts only
- Search accounts by name, phone, or address
- Interactive menu system

### XML Generation Example

Demonstrates XML serialization using JAXB.

```bash
mvn exec:java -Dexec.mainClass="com.example.cobol.XmlGenerateExample"
```

### JSON Generation Example

Demonstrates JSON serialization using Jackson.

```bash
mvn exec:java -Dexec.mainClass="com.example.cobol.JsonGenerateExample"
```

### Search Example

Demonstrates binary and sequential search patterns.

```bash
mvn exec:java -Dexec.mainClass="com.example.cobol.SearchExample"
```

### REDEFINES Example

Demonstrates the REDEFINES pattern using inheritance.

```bash
mvn exec:java -Dexec.mainClass="com.example.cobol.RedefinesExample"
```

## Migration Details

### Data Structure Conversion

#### COBOL Records → Java Classes

COBOL records with PIC clauses are converted to Java classes with appropriate field types:

**COBOL:**
```cobol
01  ws-sql-account-record.
    05  ws-sql-account-id                  pic 9(5).
    05  ws-sql-account-first-name          pic x(8).
    05  ws-sql-account-last-name           pic x(8).
    05  ws-sql-account-phone               pic x(10).
    05  ws-sql-account-address             pic x(22).
    05  ws-sql-account-is-enabled          pic x.
    05  ws-sql-account-create-dt           pic x(20).
    05  ws-sql-account-mod-dt              pic x(20).
```

**Java:**
```java
public class Account {
    private int id;                    // PIC 9(5)
    private String firstName;          // PIC X(8) - max 8 chars
    private String lastName;           // PIC X(8) - max 8 chars
    private String phone;              // PIC X(10) - max 10 chars
    private String address;            // PIC X(22) - max 22 chars
    private String isEnabled;          // PIC X - single char
    private LocalDateTime createDt;    // PIC X(20) - timestamp
    private LocalDateTime modDt;       // PIC X(20) - timestamp
    // getters/setters
}
```

#### REDEFINES → Inheritance

COBOL's REDEFINES clause (union-like behavior) is converted to Java inheritance:

**COBOL:**
```cobol
01  ws-customer.
    05  ws-customer-name.
        10  ws-customer-first-name      pic x(10).
        10  ws-customer-last-name       pic x(20).
    05  ws-corp-name redefines ws-customer-name pic x(30).
```

**Java:**
```java
abstract class Customer {
    String streetAddress;
    String state;
    int zipCode;
}

class PersonCustomer extends Customer {
    String firstName;
    String lastName;
}

class CorpCustomer extends Customer {
    String corpName;
}
```

#### OCCURS/Tables → Collections

COBOL tables with OCCURS are converted to Java collections:

**COBOL:**
```cobol
01  ws-account-record                occurs 0 to 100 times
                                     depending on ws-num-accounts.
```

**Java:**
```java
List<Account> accounts = new ArrayList<>();
```

### Database Layer Migration

#### Connection Setup

**COBOL (Embedded SQL):**
```cobol
EXEC SQL
    CONNECT TO :ws-db-connection-string
END-EXEC.
```

**Java (JDBC):**
```java
String url = "jdbc:postgresql://localhost:5432/cobol_db_example";
Properties props = new Properties();
props.setProperty("user", "postgres");
props.setProperty("password", "password");
Connection conn = DriverManager.getConnection(url, props);
```

#### Cursor Operations → ResultSets

**COBOL:**
```cobol
EXEC SQL 
    DECLARE ACCOUNT-ALL-CUR CURSOR FOR 
    SELECT ID, FIRST_NAME, LAST_NAME, PHONE, 
           ADDRESS, IS_ENABLED, CREATE_DT, MOD_DT 
    FROM ACCOUNTS 
    ORDER BY ID;
END-EXEC

EXEC SQL 
    OPEN ACCOUNT-ALL-CUR 
END-EXEC

EXEC SQL 
    FETCH ACCOUNT-ALL-CUR 
    INTO :ws-sql-account-id, :ws-sql-account-first-name, ...
END-EXEC
```

**Java:**
```java
String sql = "SELECT ID, FIRST_NAME, LAST_NAME, PHONE, " +
             "ADDRESS, IS_ENABLED, CREATE_DT, MOD_DT " +
             "FROM ACCOUNTS ORDER BY ID";
             
try (PreparedStatement stmt = conn.prepareStatement(sql);
     ResultSet rs = stmt.executeQuery()) {
    
    while (rs.next()) {
        Account account = new Account();
        account.setId(rs.getInt("ID"));
        account.setFirstName(rs.getString("FIRST_NAME"));
        // ... map other fields
        accounts.add(account);
    }
}
```

#### Parameterized Queries

**COBOL:**
```cobol
EXEC SQL 
    DECLARE ACCOUNT-QUERY-CUR CURSOR FOR 
    SELECT * FROM ACCOUNTS 
    WHERE FIRST_NAME LIKE :ws-search-value
       OR LAST_NAME LIKE :ws-search-value
    ORDER BY ID;
END-EXEC
```

**Java:**
```java
String searchValue = "%" + userInput.trim() + "%";
String sql = "SELECT * FROM ACCOUNTS WHERE " +
             "FIRST_NAME LIKE ? OR LAST_NAME LIKE ? OR " +
             "PHONE LIKE ? OR ADDRESS LIKE ? ORDER BY ID";
             
try (PreparedStatement stmt = conn.prepareStatement(sql)) {
    for (int i = 1; i <= 4; i++) {
        stmt.setString(i, searchValue);
    }
    ResultSet rs = stmt.executeQuery();
    // process results
}
```

### XML/JSON Serialization

#### XML Generation

**COBOL:**
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
end-xml
```

**Java:**
```java
@XmlRootElement(name = "ws-record")
public class Record {
    @XmlElement(name = "name")
    private String recordName;
    
    @XmlElement(name = "value")
    private String recordValue;
    
    @XmlAttribute(name = "enabled")
    private boolean enabled;
}

JAXBContext context = JAXBContext.newInstance(Record.class);
Marshaller marshaller = context.createMarshaller();
marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
StringWriter writer = new StringWriter();
marshaller.marshal(record, writer);
String xml = writer.toString();
```

#### JSON Generation

**COBOL:**
```cobol
json generate ws-json-output
    from ws-record
    count in ws-json-char-count
    name of
        ws-record-name is "name",
        ws-record-value is "value"
end-json
```

**Java:**
```java
ObjectMapper mapper = new ObjectMapper();
String json = mapper.writeValueAsString(record);
```

### Business Logic & Control Flow

#### PERFORM Loops → For Loops

**COBOL:**
```cobol
perform varying ws-account-idx from 1 by 1 
until ws-account-idx > ws-num-accounts
    display ws-account-id(ws-account-idx)
end-perform
```

**Java:**
```java
for (int i = 0; i < accounts.size(); i++) {
    Account account = accounts.get(i);
    System.out.println(account.getId());
}
```

#### Menu System

**COBOL:**
```cobol
perform forever
    display "1) Display all accounts"
    display "2) Display disabled accounts"
    display "3) Query accounts"
    display "4) Exit"
    accept ws-menu-choice
    
    evaluate ws-menu-choice
        when '1' perform display-all-accounts
        when '2' perform display-disabled-accounts
        when '3' perform query-accounts
        when '4' exit perform
    end-evaluate
end-perform
```

**Java:**
```java
Scanner scanner = new Scanner(System.in);
boolean running = true;

while (running) {
    System.out.println("1) Display all accounts");
    System.out.println("2) Display disabled accounts");
    System.out.println("3) Query accounts");
    System.out.println("4) Exit");
    System.out.print("Selection: ");
    
    String choice = scanner.nextLine();
    
    switch (choice) {
        case "1": displayAllAccounts(); break;
        case "2": displayDisabledAccounts(); break;
        case "3": queryAccounts(); break;
        case "4": running = false; break;
    }
}
```

### Search Patterns

#### Binary Search

**COBOL:**
```cobol
search all ws-item-table
    at end
        display "Item not found."
    when ws-item-id-1(idx) = ws-accept-id-1
        perform display-found-item
end-search
```

**Java:**
```java
Item found = SearchUtil.binarySearchFind(itemTable, searchKey,
        Comparator.comparingInt(item -> item.id1));

if (found != null) {
    displayFoundItem(found);
} else {
    System.out.println("Item not found.");
}
```

#### Sequential Search

**COBOL:**
```cobol
search ws-no-key-item-table
    at end
        display "Item not found."
    when ws-no-key-id(idx-2) = ws-accept-id-1
        display "Record found"
end-search
```

**Java:**
```java
NoKeyItem found = SearchUtil.sequentialSearch(noKeyItemTable, 
        item -> item.id == searchId);

if (found != null) {
    System.out.println("Record found");
} else {
    System.out.println("Item not found.");
}
```

## Key Differences and Considerations

1. **Field Length Constraints**: Java doesn't enforce COBOL's PIC clause length constraints automatically. The `truncate()` methods in model classes ensure fields don't exceed their COBOL-defined lengths.

2. **Error Handling**: COBOL's `SQLSTATE` and `SQLCODE` are replaced with Java's `SQLException` with `getSQLState()` and `getErrorCode()` methods.

3. **Memory Management**: COBOL's static memory allocation is replaced with Java's dynamic memory management and garbage collection.

4. **Type Safety**: Java provides compile-time type checking, whereas COBOL relies more on runtime validation.

5. **Object-Oriented Design**: The migration uses OOP principles (inheritance, encapsulation) to represent COBOL's procedural patterns.

## Dependencies

The project uses the following key dependencies:

- **PostgreSQL JDBC Driver** (42.6.0): Database connectivity
- **Jackson** (2.15.2): JSON serialization
- **JAXB** (2.3.1): XML serialization

See `pom.xml` for the complete list of dependencies.

## Building a JAR

To build an executable JAR:

```bash
mvn clean package
```

This creates `target/cobol-migration-1.0.0.jar` with the main class set to `SqlExampleApp`.

Run it with:

```bash
java -jar target/cobol-migration-1.0.0.jar
```

## License

This project follows the same license as the parent COBOL-Demo repository (MIT License).
