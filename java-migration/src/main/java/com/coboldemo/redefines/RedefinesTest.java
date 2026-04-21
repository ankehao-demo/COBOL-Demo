package com.coboldemo.redefines;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

/**
 * Port of {@code redifines/redefines.cbl} — demonstrates COBOL's
 * {@code REDEFINES} using two idiomatic Java patterns.
 *
 * <p>For the person/corporation record we use a small type hierarchy:
 * {@link Customer} is the abstract base with the address, and
 * {@link PersonCustomer} and {@link CorpCustomer} are concrete subclasses.
 *
 * <p>For the "different data types on the same bytes" example COBOL allows
 * the raw bytes of a {@code PIC X(10)} field to be reinterpreted as a
 * {@code COMP-2} (64-bit float). Java has no direct memory overlay, so we
 * demonstrate the concept using {@link ByteBuffer} reinterpretation.
 */
public final class RedefinesTest {

    private RedefinesTest() {
    }

    /** Shared address block — COBOL: {@code ws-customer-address}. */
    public record Address(String street, String state, String zip) {
    }

    /** Common fields for every customer. */
    public abstract static class Customer {
        protected final Address address;

        protected Customer(Address address) {
            this.address = address;
        }

        public abstract String displayType();

        public Address address() {
            return address;
        }
    }

    /** Person-customer — COBOL: first and last name fields. */
    public static final class PersonCustomer extends Customer {
        private final String firstName;
        private final String lastName;

        public PersonCustomer(String firstName, String lastName, Address address) {
            super(address);
            this.firstName = firstName;
            this.lastName = lastName;
        }

        public String firstName() {
            return firstName;
        }

        public String lastName() {
            return lastName;
        }

        @Override
        public String displayType() {
            return "PERSON";
        }
    }

    /** Corporate customer — COBOL: single corp-name field. */
    public static final class CorpCustomer extends Customer {
        private final String corpName;

        public CorpCustomer(String corpName, Address address) {
            super(address);
            this.corpName = corpName;
        }

        public String corpName() {
            return corpName;
        }

        @Override
        public String displayType() {
            return "CORP";
        }
    }

    public static void main(String[] args) {
        System.out.println();
        System.out.println("1. Person record with first/last name entered.");
        System.out.println("2. Corp record with corp name entered.");
        System.out.println("3. Person record with corp name entered.");

        List<Customer> customers = new ArrayList<>();
        customers.add(new PersonCustomer("test-first", "test-last",
                new Address("123 fake st", "NV", "12345")));
        customers.add(new CorpCustomer("no-name corp",
                new Address("567 real st", "NY", "11795")));
        // The third record mimics the COBOL program moving a corp name into
        // a person record — in COBOL the bytes overlap, in Java we just
        // expose a person with an empty first/last name.
        customers.add(new PersonCustomer("", "SET CORP VALUE",
                new Address("890 what st", "MA", "09345")));

        displayCustomerData(customers);
        displaySecondTestData();
    }

    private static void displayCustomerData(List<Customer> customers) {
        System.out.println();
        System.out.println("Displaying fake customer data:");
        System.out.println("------------------------------");
        System.out.println();
        for (Customer customer : customers) {
            System.out.println("Customer Type: " + customer.displayType());
            if (customer instanceof PersonCustomer person) {
                System.out.println("First Name: " + person.firstName());
                System.out.println("Last Name: " + person.lastName());
            } else if (customer instanceof CorpCustomer corp) {
                System.out.println("Company name: " + corp.corpName());
            }
            Address address = customer.address();
            System.out.println("Address: ");
            System.out.println(address.street());
            System.out.println(address.state() + ", " + address.zip());
            System.out.println("------------------------------");
            System.out.println();
        }
    }

    /**
     * Demonstrates COBOL "byte overlay" using {@link ByteBuffer}. A 10-byte
     * array is interpreted either as an ASCII string or as a sequence of
     * IEEE-754 doubles. Java does not allow direct memory aliasing, so this
     * is the closest idiomatic substitute.
     */
    private static void displaySecondTestData() {
        System.out.println();
        System.out.println("Redefines with different variable types:");
        System.out.println("----------------------------------------");

        byte[] bytes = new byte[10];
        System.arraycopy("ABC123".getBytes(), 0, bytes, 0, 6);
        System.out.println("Value entered in ws-data-disp-value: ABC123");
        System.out.println("ws-data-disp-value x(10): " + new String(bytes).trim());
        double asDoubleFromString = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getDouble();
        System.out.println("ws-data-comp-value comp-2: " + asDoubleFromString);
        System.out.println();

        System.out.println("----------------------------------------");
        ByteBuffer buffer = ByteBuffer.allocate(10).order(ByteOrder.LITTLE_ENDIAN);
        buffer.putDouble(12345.63);
        byte[] numericBytes = buffer.array();
        System.out.println("Value entered in ws-data-comp-value: 12345.63");
        System.out.println("ws-data-disp-value x(10): " + new String(numericBytes));
        double asDoubleFromDouble = ByteBuffer.wrap(numericBytes).order(ByteOrder.LITTLE_ENDIAN).getDouble();
        System.out.println("ws-data-comp-value comp-2: " + asDoubleFromDouble);
        System.out.println();
    }
}
