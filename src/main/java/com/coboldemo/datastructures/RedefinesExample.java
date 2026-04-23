package com.coboldemo.datastructures;

/**
 * Migrated from: redifines/redefines.cbl
 *
 * Demonstrates COBOL REDEFINES clause which creates union-like overlays.
 * In Java, this is modeled using a Customer class with a type discriminator
 * and separate fields for person vs. corporate customers. A second example
 * shows redefining the same memory as different data types.
 */
public class RedefinesExample {

    static final int CUSTOMER_TYPE_PERSON = 1;
    static final int CUSTOMER_TYPE_CORP = 2;

    static class Customer {
        int customerType;
        // Person fields (REDEFINES: ws-customer-name -> ws-corp-name)
        String firstName;  // PIC X(10)
        String lastName;   // PIC X(20)
        // Corp field (redefines firstName + lastName as single field)
        String corpName;   // PIC X(30)
        // Address fields
        String streetAddress; // PIC X(20)
        String state;         // PIC XX
        int zipCode;          // PIC 9(5)

        void setPersonName(String first, String last) {
            this.customerType = CUSTOMER_TYPE_PERSON;
            this.firstName = padRight(first, 10);
            this.lastName = padRight(last, 20);
            this.corpName = this.firstName + this.lastName;
        }

        void setCorpName(String name) {
            this.customerType = CUSTOMER_TYPE_CORP;
            this.corpName = padRight(name, 30);
            this.firstName = this.corpName.substring(0, 10);
            this.lastName = this.corpName.substring(10, 30);
        }
    }

    static class DiffDataType {
        char dataType; // 'D' for display, 'C' for comp
        String dispValue;  // PIC X(10)
        double compValue;  // COMP-2 (redefines dispValue)
    }

    public static void main(String[] args) {
        int numRecords = 3;
        Customer[] customers = new Customer[numRecords];
        for (int i = 0; i < numRecords; i++) {
            customers[i] = new Customer();
        }

        // Setup test data
        System.out.println();
        System.out.println("1. Person record with first/last name entered.");
        customers[0].setPersonName("test-first", "test-last");
        customers[0].streetAddress = padRight("123 fake st", 20);
        customers[0].state = "NV";
        customers[0].zipCode = 12345;

        System.out.println("2. Corp record with corp name entered.");
        customers[1].setCorpName("no-name corp");
        customers[1].streetAddress = padRight("567 real st", 20);
        customers[1].state = "NY";
        customers[1].zipCode = 11795;

        System.out.println("3. Person record with corp name entered.");
        customers[2].customerType = CUSTOMER_TYPE_PERSON;
        customers[2].setCorpName("SET CORP VALUE");
        customers[2].customerType = CUSTOMER_TYPE_PERSON; // Override type back to person
        customers[2].streetAddress = padRight("890 what st", 20);
        customers[2].state = "MA";
        customers[2].zipCode = 9345;

        // Display customer data
        System.out.println();
        System.out.println("Displaying fake customer data:");
        System.out.println("------------------------------");
        System.out.println();

        for (int i = 0; i < numRecords; i++) {
            Customer c = customers[i];
            if (c.customerType == CUSTOMER_TYPE_PERSON) {
                System.out.println("Customer Type: PERSON");
                System.out.println("First Name: " + c.firstName);
                System.out.println("Last Name: " + c.lastName);
            } else {
                System.out.println("Customer Type: CORP");
                System.out.println("Company name: " + c.corpName);
            }

            System.out.println("Address: ");
            System.out.println(c.streetAddress);
            System.out.println(c.state + ", " + String.format("%05d", c.zipCode));
            System.out.println("------------------------------");
            System.out.println();
        }

        // Second REDEFINES example: different data types sharing same storage
        DiffDataType[] diffData = new DiffDataType[2];
        diffData[0] = new DiffDataType();
        diffData[1] = new DiffDataType();

        diffData[0].dataType = 'D';
        diffData[0].dispValue = padRight("ABC123", 10);

        diffData[1].dataType = 'C';
        diffData[1].compValue = 12345.63;

        System.out.println();
        System.out.println("Redefines with different variable types:");
        System.out.println("----------------------------------------");
        System.out.println("Value entered in ws-data-disp-value: ABC123");
        System.out.println("ws-data-disp-value x(10): " + diffData[0].dispValue);
        System.out.println("ws-data-comp-value comp-2: " + diffData[0].compValue);
        System.out.println();
        System.out.println("----------------------------------------");
        System.out.println("Value entered in ws-data-comp-value: 12345.63");
        System.out.println("ws-data-disp-value x(10): " + diffData[1].dispValue);
        System.out.println("ws-data-comp-value comp-2: " + diffData[1].compValue);
        System.out.println();
    }

    static String padRight(String s, int len) {
        if (s == null) s = "";
        if (s.length() >= len) return s.substring(0, len);
        return String.format("%-" + len + "s", s);
    }
}
