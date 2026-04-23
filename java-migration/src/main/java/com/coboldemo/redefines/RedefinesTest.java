package com.coboldemo.redefines;

import java.util.ArrayList;
import java.util.List;

/**
 * Java port of redifines/redefines.cbl.
 *
 * COBOL's {@code REDEFINES} lets the same memory region be interpreted in
 * multiple shapes. Java has no direct analog; the two cases in the original
 * program are modelled with:
 *
 * <ol>
 *   <li>A polymorphic {@link Customer} hierarchy for the person/corp case.</li>
 *   <li>A {@link DiffDataTypes} pair mirroring the "display vs COMP-2" case,
 *       with both representations stored explicitly.</li>
 * </ol>
 */
public final class RedefinesTest {

    private RedefinesTest() {
    }

    public static void main(String[] args) {
        List<Customer> customers = setupTestData();
        displayCustomerData(customers);

        DiffDataTypes[] diff = setupSecondTestData();
        displaySecondTestData(diff);
    }

    private static List<Customer> setupTestData() {
        System.out.println();
        System.out.println("1. Person record with first/last name entered.");
        PersonCustomer p1 = new PersonCustomer(
                "test-first", "test-last",
                new Address("123 fake st", "NV", 12345));

        System.out.println("2. Corp record with corp name entered.");
        CorpCustomer c2 = new CorpCustomer(
                "no-name corp",
                new Address("567 real st", "NY", 11795));

        System.out.println("3. Person record with corp name entered.");
        // The COBOL version deliberately writes a corp name over a person
        // record's memory. We simulate that by constructing a PersonCustomer
        // whose first/last fields hold the two halves of the corp string.
        String blended = "SET CORP VALUE";
        String first = blended.length() >= 10
                ? blended.substring(0, 10)
                : blended;
        String last = blended.length() > 10 ? blended.substring(10) : "";
        PersonCustomer p3 = new PersonCustomer(
                first, last,
                new Address("890 what st", "MA", 9345));

        List<Customer> customers = new ArrayList<>();
        customers.add(p1);
        customers.add(c2);
        customers.add(p3);
        return customers;
    }

    private static void displayCustomerData(List<Customer> customers) {
        System.out.println();
        System.out.println("Displaying fake customer data:");
        System.out.println("------------------------------");
        System.out.println();

        for (Customer c : customers) {
            c.display();
            System.out.println("Address: ");
            System.out.println(c.getAddress().getStreet());
            System.out.println(c.getAddress().getState() + ", "
                    + String.format("%05d", c.getAddress().getZip()));
            System.out.println("------------------------------");
            System.out.println();
        }
    }

    private static DiffDataTypes[] setupSecondTestData() {
        DiffDataTypes display = new DiffDataTypes();
        display.setDisplayValue("ABC123");

        DiffDataTypes comp = new DiffDataTypes();
        comp.setCompValue(12345.63);

        return new DiffDataTypes[]{display, comp};
    }

    private static void displaySecondTestData(DiffDataTypes[] entries) {
        System.out.println();
        System.out.println("Redefines with different variable types:");
        System.out.println("----------------------------------------");
        System.out.println("Value entered in ws-data-disp-value: ABC123");
        System.out.println("ws-data-disp-value x(10): "
                + entries[0].getDisplayValue());
        System.out.println("ws-data-comp-value comp-2: "
                + entries[0].getCompValue());
        System.out.println();
        System.out.println("----------------------------------------");
        System.out.println("Value entered in ws-data-comp-value: 12345.63");
        System.out.println("ws-data-disp-value x(10): "
                + entries[1].getDisplayValue());
        System.out.println("ws-data-comp-value comp-2: "
                + entries[1].getCompValue());
        System.out.println();
    }

    /** Base customer carrying the common address sub-record. */
    public abstract static class Customer {
        private final Address address;

        protected Customer(Address address) {
            this.address = address;
        }

        public Address getAddress() {
            return address;
        }

        public abstract void display();
    }

    public static final class PersonCustomer extends Customer {
        private final String firstName;
        private final String lastName;

        public PersonCustomer(String firstName, String lastName,
                              Address address) {
            super(address);
            this.firstName = firstName;
            this.lastName = lastName;
        }

        @Override
        public void display() {
            System.out.println("Customer Type: PERSON");
            System.out.println("First Name: " + firstName);
            System.out.println("Last Name: " + lastName);
        }
    }

    public static final class CorpCustomer extends Customer {
        private final String corpName;

        public CorpCustomer(String corpName, Address address) {
            super(address);
            this.corpName = corpName;
        }

        @Override
        public void display() {
            System.out.println("Customer Type: CORP");
            System.out.println("Company name: " + corpName);
        }
    }

    /** Nested Address group from ws-customer-address. */
    public static final class Address {
        private final String street;
        private final String state;
        private final int zip;

        public Address(String street, String state, int zip) {
            this.street = street;
            this.state = state;
            this.zip = zip;
        }

        public String getStreet() {
            return street;
        }

        public String getState() {
            return state;
        }

        public int getZip() {
            return zip;
        }
    }

    /**
     * Stand-in for the {@code ws-data-disp-value / ws-data-comp-value}
     * redefines. Java has no union type so both fields co-exist; the original
     * used the same bytes.
     */
    public static final class DiffDataTypes {
        private String displayValue;
        private Double compValue;

        public String getDisplayValue() {
            return displayValue;
        }

        public void setDisplayValue(String displayValue) {
            this.displayValue = displayValue;
        }

        public Double getCompValue() {
            return compValue;
        }

        public void setCompValue(Double compValue) {
            this.compValue = compValue;
        }
    }
}
