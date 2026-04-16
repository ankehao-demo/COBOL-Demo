package com.coboldemo.redefines;

/**
 * Migrated from: redifines/redefines.cbl
 * Original author: Erik Eriksen (2021-09-28, updated 2022-03-15)
 * Purpose: Demonstrates REDEFINES for overlaying different data structures.
 *
 * Notes:
 * - Java does not have memory-level REDEFINES. Instead, we use a class hierarchy
 *   with a type discriminator field to represent the union-like behavior.
 * - The second test (different data types sharing memory) is represented with
 *   separate typed fields.
 */
public class RedefinesTest {

    // Customer record - type determines which fields are valid
    static class Customer {
        String customerType; // "P" for person, "C" for corporate
        // Person fields
        String firstName;
        String lastName;
        // Corporate field (REDEFINES first+last name)
        String corpName;
        // Address
        String street;
        String state;
        String zipCode;
    }

    // Demonstrates REDEFINES with different data types sharing same space
    static class RedefinesDemo {
        // Original definition: PIC X(8) (alphanumeric)
        String alphaValue;
        // REDEFINES as: PIC 9(8) (numeric)
        long numericValue;
        // REDEFINES as: PIC 9(4)V9(4) (decimal)
        double decimalValue;
    }

    public static void main(String[] args) {
        System.out.println("REDEFINES Test");
        System.out.println("==============");
        System.out.println();

        // --- Test 1: Customer records with REDEFINES ---
        System.out.println("Test 1: Customer Records with REDEFINES");
        System.out.println("----------------------------------------");

        // Create sample customers (matching the COBOL data)
        Customer[] customers = new Customer[5];

        customers[0] = createPerson("P", "John", "Smith", "123 Main St", "NY", "10001");
        customers[1] = createCorporate("C", "Acme Corporation", "456 Oak Ave", "CA", "90210");
        customers[2] = createPerson("P", "Jane", "Doe", "789 Pine Rd", "TX", "75001");
        customers[3] = createCorporate("C", "Widget Industries", "321 Elm Blvd", "FL", "33101");
        customers[4] = createPerson("P", "Bob", "Jones", "654 Maple Dr", "WA", "98101");

        for (int i = 0; i < customers.length; i++) {
            Customer c = customers[i];
            System.out.println("Customer " + (i + 1) + ":");

            if ("P".equals(c.customerType)) {
                System.out.println("  Type: Person");
                System.out.println("  First Name: " + c.firstName);
                System.out.println("  Last Name:  " + c.lastName);
            } else if ("C".equals(c.customerType)) {
                System.out.println("  Type: Corporate");
                System.out.println("  Corp Name:  " + c.corpName);
            }

            System.out.println("  Street:     " + c.street);
            System.out.println("  State:      " + c.state);
            System.out.println("  Zip Code:   " + c.zipCode);
            System.out.println();
        }

        // --- Test 2: Different data types sharing memory via REDEFINES ---
        System.out.println("Test 2: Different data types (REDEFINES equivalent)");
        System.out.println("---------------------------------------------------");

        // In COBOL, all these share the same memory; in Java, they are separate fields
        RedefinesDemo demo = new RedefinesDemo();

        // Set alpha value
        demo.alphaValue = "12345678";
        // Interpret same bytes as numeric
        demo.numericValue = Long.parseLong(demo.alphaValue);
        // Interpret same bytes as decimal (9(4)V9(4) = first 4 digits.last 4 digits)
        demo.decimalValue = demo.numericValue / 10000.0;

        System.out.println("Alpha value (PIC X(8)):       " + demo.alphaValue);
        System.out.println("Numeric value (PIC 9(8)):     " + demo.numericValue);
        System.out.printf("Decimal value (PIC 9(4)V9(4)): %.4f%n", demo.decimalValue);
        System.out.println();
        System.out.println("Note: In COBOL, all three interpretations share the same memory.");
        System.out.println("In Java, these are separate fields for demonstration purposes.");
    }

    private static Customer createPerson(String type, String first, String last,
                                          String street, String state, String zip) {
        Customer c = new Customer();
        c.customerType = type;
        c.firstName = first;
        c.lastName = last;
        c.street = street;
        c.state = state;
        c.zipCode = zip;
        return c;
    }

    private static Customer createCorporate(String type, String corpName,
                                             String street, String state, String zip) {
        Customer c = new Customer();
        c.customerType = type;
        c.corpName = corpName;
        c.street = street;
        c.state = state;
        c.zipCode = zip;
        return c;
    }
}
