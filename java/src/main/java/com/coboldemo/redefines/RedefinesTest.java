package com.coboldemo.redefines;

/**
 * Migrated from redifines/redefines.cbl
 * Demonstrates REDEFINES on fields using Java equivalents.
 */
public class RedefinesTest {

    static class Address {
        String street;  // PIC X(20)
        String state;   // PIC XX
        int zipCode;    // PIC 9(5)

        Address(String street, String state, int zipCode) {
            this.street = street;
            this.state = state;
            this.zipCode = zipCode;
        }
    }

    static class Customer {
        int customerType;      // 1=PERSON, 2=CORP
        String firstName;      // PIC X(10) - used when type=PERSON
        String lastName;       // PIC X(20) - used when type=PERSON
        String corpName;       // PIC X(30) - REDEFINES firstName+lastName, used when type=CORP
        Address address;

        // Set as person
        void setPersonName(String first, String last) {
            this.customerType = 1;
            this.firstName = String.format("%-10s", first).substring(0, 10);
            this.lastName = String.format("%-20s", last).substring(0, 20);
            this.corpName = this.firstName + this.lastName; // redefines overlay
        }

        // Set as corp (shares same 30-char space as firstName+lastName)
        void setCorpName(String name) {
            this.corpName = String.format("%-30s", name).substring(0, 30);
            // REDEFINES: first 10 chars map to firstName, next 20 to lastName
            this.firstName = this.corpName.substring(0, 10);
            this.lastName = this.corpName.substring(10, 30);
        }
    }

    static class DiffDataType {
        char dataType;         // 'D' for display, 'C' for comp
        String dispValue;      // PIC X(10)
        double compValue;      // COMP-2 (REDEFINES dispValue)
    }

    public static void main(String[] args) {
        setupAndDisplayCustomerData();
        setupAndDisplaySecondTestData();
    }

    private static void setupAndDisplayCustomerData() {
        Customer[] customers = new Customer[3];

        // 1. Person record with first/last name
        System.out.println();
        System.out.println("1. Person record with first/last name entered.");
        customers[0] = new Customer();
        customers[0].setPersonName("test-first", "test-last");
        customers[0].address = new Address("123 fake st", "NV", 12345);

        // 2. Corp record with corp name
        System.out.println("2. Corp record with corp name entered.");
        customers[1] = new Customer();
        customers[1].customerType = 2;
        customers[1].setCorpName("no-name corp");
        customers[1].address = new Address("567 real st", "NY", 11795);

        // 3. Person record but data set via corpName (REDEFINES demonstration)
        System.out.println("3. Person record with corp name entered.");
        customers[2] = new Customer();
        customers[2].customerType = 1; // Person type
        customers[2].setCorpName("SET CORP VALUE");
        customers[2].address = new Address("890 what st", "MA", 9345);

        // Display customer data
        System.out.println();
        System.out.println("Displaying fake customer data:");
        System.out.println("------------------------------");
        System.out.println();

        for (Customer c : customers) {
            if (c.customerType == 1) {
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
    }

    private static void setupAndDisplaySecondTestData() {
        // Demonstrate REDEFINES with different data types
        DiffDataType[] records = new DiffDataType[2];

        // Record 1: Display type with string value
        records[0] = new DiffDataType();
        records[0].dataType = 'D';
        records[0].dispValue = String.format("%-10s", "ABC123");
        records[0].compValue = 0; // meaningless when interpreted as display type

        // Record 2: Comp type with numeric value
        records[1] = new DiffDataType();
        records[1].dataType = 'C';
        records[1].dispValue = ""; // meaningless when interpreted as comp type
        records[1].compValue = 12345.63;

        System.out.println();
        System.out.println("Redefines with different variable types:");
        System.out.println("----------------------------------------");
        System.out.println("Value entered in ws-data-disp-value: ABC123");
        System.out.println("ws-data-disp-value x(10): " + records[0].dispValue);
        System.out.println("ws-data-comp-value comp-2: " + records[0].compValue);
        System.out.println();
        System.out.println("----------------------------------------");
        System.out.println("Value entered in ws-data-comp-value: 12345.63");
        System.out.println("ws-data-disp-value x(10): " + records[1].dispValue);
        System.out.println("ws-data-comp-value comp-2: " + records[1].compValue);
        System.out.println();
        // Note: In COBOL, REDEFINES shares the same memory. In Java, we use
        // separate typed fields since Java doesn't have memory-level REDEFINES.
        System.out.println("Note: Java does not have memory-level REDEFINES.");
        System.out.println("Each interpretation uses separate typed fields.");
    }
}
