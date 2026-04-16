package com.coboldemo.redefines;

/**
 * Migrated from: redifines/redefines.cbl
 *
 * COBOL-to-Java mapping:
 *   REDEFINES (overlapping memory)  -> Inheritance / union pattern (Java has no memory-level REDEFINES)
 *   PIC X(n)                        -> String
 *   PIC 9(5)                        -> int
 *   COMP-2                          -> double (64-bit floating point)
 *   OCCURS n TIMES                  -> Array of size n
 *   88-level condition names        -> Constants + enum or char flags
 *   INDEXED BY                      -> Array index (0-based in Java vs 1-based in COBOL)
 *
 * COBOL REDEFINES allows two different data descriptions to share the same memory.
 * Java does not support memory-level overlapping. Instead, we use:
 *
 * 1. For the customer record: A Customer class with a customerType field.
 *    When type is PERSON ('P'), firstName and lastName are used.
 *    When type is CORP ('C'), corpName is used.
 *    Both name representations exist as fields; the type flag indicates which is valid.
 *
 * 2. For the data type test: Separate typed fields (String and double) with a type flag.
 *    Java cannot share memory between a String and a double, so we store both
 *    and use the type flag to determine which interpretation is active.
 */
public class RedefinesTest {

    // ----- Customer Record (REDEFINES for name: person name vs corp name) -----

    /**
     * Represents a customer address.
     * COBOL: 10 ws-address
     *          15 ws-street-address PIC X(30).
     *          15 ws-state          PIC X(2).
     *          15 ws-zip-code       PIC 9(5).
     */
    static class Address {
        String street;
        String state;
        int zipCode;

        Address(String street, String state, int zipCode) {
            this.street = street;
            this.state = state;
            this.zipCode = zipCode;
        }

        @Override
        public String toString() {
            return street.trim() + ", " + state + " " + String.format("%05d", zipCode);
        }
    }

    /**
     * Represents a customer record.
     *
     * COBOL REDEFINES allows ws-customer-name (firstName + lastName) and
     * ws-corp-name to share the same 40-byte memory area. In Java, we
     * store both representations and use customerType to indicate which
     * interpretation is valid.
     *
     * COBOL:
     *   10 ws-customer-type-flag PIC X.
     *       88 ws-customer-type-person VALUE 'P'.
     *       88 ws-customer-type-corp   VALUE 'C'.
     *   10 ws-customer-name.
     *       15 ws-customer-first-name PIC X(20).
     *       15 ws-customer-last-name  PIC X(20).
     *   10 ws-corp-name REDEFINES ws-customer-name PIC X(40).
     *   10 ws-address.
     *       15 ws-street-address PIC X(30).
     *       15 ws-state          PIC X(2).
     *       15 ws-zip-code       PIC 9(5).
     */
    static class Customer {
        static final char TYPE_PERSON = 'P';
        static final char TYPE_CORP = 'C';

        char customerType;

        // Person name fields (valid when customerType == 'P')
        String firstName;
        String lastName;

        // Corporate name field (valid when customerType == 'C')
        // In COBOL, this REDEFINES ws-customer-name, sharing the same memory.
        // In Java, this is a separate field; the type flag determines which to use.
        String corpName;

        Address address;

        /**
         * Creates a person-type customer.
         */
        static Customer createPerson(String firstName, String lastName, Address address) {
            Customer c = new Customer();
            c.customerType = TYPE_PERSON;
            c.firstName = firstName;
            c.lastName = lastName;
            // In COBOL, corp-name would overlay first+last in memory.
            // We set it to the concatenation to mirror that behavior.
            c.corpName = padRight(firstName, 20) + padRight(lastName, 20);
            c.address = address;
            return c;
        }

        /**
         * Creates a corporate-type customer.
         */
        static Customer createCorp(String corpName, Address address) {
            Customer c = new Customer();
            c.customerType = TYPE_CORP;
            c.corpName = corpName;
            // In COBOL, first-name and last-name would overlay corp-name in memory.
            // We split the 40-char corp-name into the two 20-char fields.
            String padded = padRight(corpName, 40);
            c.firstName = padded.substring(0, 20);
            c.lastName = padded.substring(20, 40);
            c.address = address;
            return c;
        }

        void display(int index) {
            System.out.println("Customer " + index + ":");
            System.out.println("  Type: " + (customerType == TYPE_PERSON ? "Person" : "Corporation"));
            if (customerType == TYPE_PERSON) {
                System.out.println("  First Name: " + firstName.trim());
                System.out.println("  Last Name:  " + lastName.trim());
            } else {
                System.out.println("  Corp Name:  " + corpName.trim());
            }
            System.out.println("  Address:    " + address);
        }
    }

    // ----- Data Type Test (REDEFINES with different data types) -----

    /**
     * Represents a data entry where REDEFINES allows a display value (PIC X(10))
     * and a computational value (COMP-2 / double) to share the same memory.
     *
     * COBOL:
     *   05 ws-data-entry OCCURS 3 TIMES.
     *       10 ws-data-type-flag     PIC X.
     *           88 ws-display-type   VALUE 'D'.
     *           88 ws-comp-type      VALUE 'C'.
     *       10 ws-data-disp-value    PIC X(10).
     *       10 ws-data-comp-value REDEFINES ws-data-disp-value COMP-2.
     *
     * Java does not support memory-level REDEFINES. We store both a String
     * and a double, using the type flag to indicate which interpretation is active.
     * This is the standard Java approach to COBOL REDEFINES with different types.
     */
    static class DataEntry {
        static final char DISPLAY_TYPE = 'D';
        static final char COMP_TYPE = 'C';

        char dataTypeFlag;
        String dispValue;   // Valid when dataTypeFlag == 'D'
        double compValue;   // Valid when dataTypeFlag == 'C'

        static DataEntry createDisplay(String value) {
            DataEntry entry = new DataEntry();
            entry.dataTypeFlag = DISPLAY_TYPE;
            entry.dispValue = value;
            entry.compValue = 0.0;
            return entry;
        }

        static DataEntry createComp(double value) {
            DataEntry entry = new DataEntry();
            entry.dataTypeFlag = COMP_TYPE;
            entry.compValue = value;
            entry.dispValue = "";
            return entry;
        }

        void display(int index) {
            System.out.println("Data Entry " + index + ":");
            System.out.println("  Type: " + (dataTypeFlag == DISPLAY_TYPE ? "Display" : "Comp-2"));
            if (dataTypeFlag == DISPLAY_TYPE) {
                System.out.println("  Display Value: " + dispValue);
            } else {
                System.out.println("  Comp-2 Value:  " + compValue);
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("=== COBOL REDEFINES Demonstration ===");
        System.out.println();

        // ----- Test 1: Customer Table with REDEFINES -----
        // COBOL: 01 ws-customer-table.
        //   05 ws-customer-record OCCURS 3 TIMES INDEXED BY ws-customer-idx.
        System.out.println("--- Customer Table (REDEFINES: person name vs corp name) ---");
        System.out.println();

        // Create the customer table (3 entries, matching COBOL OCCURS 3 TIMES)
        Customer[] customerTable = new Customer[3];

        // Customer 1: Person
        // COBOL: SET ws-customer-type-person TO TRUE
        //        MOVE "John" TO ws-customer-first-name(1)
        //        MOVE "Smith" TO ws-customer-last-name(1)
        customerTable[0] = Customer.createPerson(
                "John", "Smith",
                new Address("123 Main Street", "CA", 90210)
        );

        // Customer 2: Corporation
        // COBOL: SET ws-customer-type-corp TO TRUE
        //        MOVE "Acme Corporation" TO ws-corp-name(2)
        customerTable[1] = Customer.createCorp(
                "Acme Corporation",
                new Address("456 Business Blvd", "NY", 10001)
        );

        // Customer 3: Person
        customerTable[2] = Customer.createPerson(
                "Jane", "Doe",
                new Address("789 Oak Avenue", "TX", 75001)
        );

        // Display all customers
        // COBOL: PERFORM VARYING ws-customer-idx FROM 1 BY 1 UNTIL ws-customer-idx > 3
        for (int i = 0; i < customerTable.length; i++) {
            customerTable[i].display(i + 1);
            System.out.println();
        }

        // Demonstrate the REDEFINES overlay behavior
        System.out.println("--- REDEFINES Overlay Demonstration ---");
        System.out.println("For Person 'John Smith':");
        System.out.println("  As person name: [" + customerTable[0].firstName.trim()
                + "] [" + customerTable[0].lastName.trim() + "]");
        System.out.println("  As corp name (REDEFINES overlay): ["
                + customerTable[0].corpName.trim() + "]");
        System.out.println("  (In COBOL, both views share the same 40 bytes of memory)");
        System.out.println();

        System.out.println("For Corp 'Acme Corporation':");
        System.out.println("  As corp name: [" + customerTable[1].corpName.trim() + "]");
        System.out.println("  As person name (REDEFINES overlay): ["
                + customerTable[1].firstName.trim() + "] ["
                + customerTable[1].lastName.trim() + "]");
        System.out.println("  (In COBOL, reading person fields from a corp record");
        System.out.println("   would show the first 20 and last 20 chars of the corp name)");
        System.out.println();

        // ----- Test 2: Different Data Types sharing memory via REDEFINES -----
        System.out.println("--- Data Type REDEFINES (display vs comp-2) ---");
        System.out.println();
        System.out.println("Note: Java does not have memory-level REDEFINES.");
        System.out.println("In COBOL, PIC X(10) and COMP-2 share the same bytes.");
        System.out.println("In Java, we use separate typed fields with a type flag.");
        System.out.println();

        // COBOL: 01 ws-data-table.
        //   05 ws-data-entry OCCURS 3 TIMES.
        DataEntry[] dataTable = new DataEntry[3];

        // Entry 1: Display type
        // COBOL: SET ws-display-type(1) TO TRUE
        //        MOVE "HELLO" TO ws-data-disp-value(1)
        dataTable[0] = DataEntry.createDisplay("HELLO");

        // Entry 2: Comp type
        // COBOL: SET ws-comp-type(2) TO TRUE
        //        MOVE 3.14159 TO ws-data-comp-value(2)
        dataTable[1] = DataEntry.createComp(3.14159);

        // Entry 3: Display type
        // COBOL: SET ws-display-type(3) TO TRUE
        //        MOVE "WORLD" TO ws-data-disp-value(3)
        dataTable[2] = DataEntry.createDisplay("WORLD");

        // Display all data entries
        for (int i = 0; i < dataTable.length; i++) {
            dataTable[i].display(i + 1);
            System.out.println();
        }

        System.out.println("=== End of REDEFINES Demonstration ===");
    }

    /**
     * Pads a string with spaces on the right to reach the target length.
     * Mimics COBOL fixed-length PIC X(n) field behavior.
     */
    private static String padRight(String value, int length) {
        if (value.length() >= length) {
            return value.substring(0, length);
        }
        return String.format("%-" + length + "s", value);
    }
}
