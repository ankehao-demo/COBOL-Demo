package com.coboldemo.redefines;

/**
 * Java equivalent of redifines/redefines.cbl
 *
 * Demonstrates the COBOL REDEFINES clause, which allows the same memory area
 * to be interpreted as different data types. Java has no direct memory overlay;
 * instead we use a discriminated union pattern with a type field.
 *
 * Test 1: Customer records where PERSON has first/last name but CORP has a
 *         single corp name — both sharing the same 30-character name field.
 * Test 2: A field that can be viewed as either a display string (PIC X) or
 *         a computational double (COMP-2).
 */
public class RedefinesExample {

    enum CustomerType { PERSON, CORP }

    /** Address inner class shared by all customer types. */
    static class Address {
        String street;
        String state;
        int zipCode;

        Address(String street, String state, int zipCode) {
            this.street = street;
            this.state = state;
            this.zipCode = zipCode;
        }
    }

    /** Customer with a discriminated name field (REDEFINES). */
    static class Customer {
        CustomerType type;
        String firstName;  // used when type == PERSON
        String lastName;   // used when type == PERSON
        String corpName;   // used when type == CORP (REDEFINES first+last)
        Address address;

        Customer(CustomerType type) {
            this.type = type;
        }
    }

    /**
     * Wrapper for the second REDEFINES test: same storage interpreted as
     * either a display string or a comp-2 double.
     */
    static class DualTypeField {
        char dataType; // 'D' for display, 'C' for comp
        String displayValue;
        double compValue;
    }

    public static void main(String[] args) {
        // ===== Test 1: Customer records with REDEFINES on name =====
        Customer[] customers = new Customer[3];

        // 1. Person record with first/last name
        System.out.println();
        System.out.println("1. Person record with first/last name entered.");
        customers[0] = new Customer(CustomerType.PERSON);
        customers[0].firstName = "test-first";
        customers[0].lastName = "test-last";
        customers[0].address = new Address("123 fake st", "NV", 12345);

        // 2. Corp record with corp name
        System.out.println("2. Corp record with corp name entered.");
        customers[1] = new Customer(CustomerType.CORP);
        customers[1].corpName = "no-name corp";
        customers[1].address = new Address("567 real st", "NY", 11795);

        // 3. Person record but corp name was set (demonstrates REDEFINES overlap)
        System.out.println("3. Person record with corp name entered.");
        customers[2] = new Customer(CustomerType.PERSON);
        customers[2].corpName = "SET CORP VALUE";
        // When corp name is set on a PERSON type, first/last overlap:
        // In COBOL the 30-byte name area would contain "SET CORP VALUE"
        // We simulate by splitting the corp name into first(10) + last(20)
        if (customers[2].corpName != null && customers[2].firstName == null) {
            String padded = String.format("%-30s", customers[2].corpName);
            customers[2].firstName = padded.substring(0, 10);
            customers[2].lastName = padded.substring(10, 30);
        }
        customers[2].address = new Address("890 what st", "MA", 9345);

        // Display customer data
        System.out.println();
        System.out.println("Displaying fake customer data:");
        System.out.println("------------------------------");
        System.out.println();

        for (Customer c : customers) {
            if (c.type == CustomerType.PERSON) {
                System.out.println("Customer Type: PERSON");
                System.out.println("First Name: " + c.firstName);
                System.out.println("Last Name: " + c.lastName);
            } else {
                System.out.println("Customer Type: CORP");
                System.out.println("Company name: " + c.corpName);
            }
            System.out.println("Address: ");
            System.out.println(c.address.street);
            System.out.println(c.address.state + ", " + String.format("%05d", c.address.zipCode));
            System.out.println("------------------------------");
            System.out.println();
        }

        // ===== Test 2: REDEFINES changing data type =====
        DualTypeField[] fields = new DualTypeField[2];

        fields[0] = new DualTypeField();
        fields[0].dataType = 'D';
        fields[0].displayValue = "ABC123";

        fields[1] = new DualTypeField();
        fields[1].dataType = 'C';
        fields[1].compValue = 12345.63;

        System.out.println();
        System.out.println("Redefines with different variable types:");
        System.out.println("----------------------------------------");
        System.out.println("Value entered in ws-data-disp-value: ABC123");
        System.out.println("ws-data-disp-value x(10): " + String.format("%-10s", fields[0].displayValue));
        // When display value is interpreted as comp-2, it's garbage in COBOL
        System.out.println("ws-data-comp-value comp-2: " + "(undefined - raw bytes as double)");
        System.out.println();
        System.out.println("----------------------------------------");
        System.out.println("Value entered in ws-data-comp-value: 12345.63");
        // When comp value is interpreted as display, it's raw bytes in COBOL
        System.out.println("ws-data-disp-value x(10): " + "(undefined - raw bytes as string)");
        System.out.println("ws-data-comp-value comp-2: " + fields[1].compValue);
        System.out.println();
    }
}
