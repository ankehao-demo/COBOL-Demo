package com.coboldemo.redefines;

import java.util.ArrayList;
import java.util.List;

/**
 * Java port of {@code redifines/redefines.cbl}.
 *
 * Demonstrates the two REDEFINES patterns from the COBOL original:
 * <ol>
 *   <li>Overlay of two record shapes (person vs corporation) on the same
 *       memory; modelled here as a {@link Customer} hierarchy.</li>
 *   <li>Overlay of a display string and a {@code COMP-2} numeric on the
 *       same memory; modelled as a tagged union {@link DataValue}.</li>
 * </ol>
 */
public class RedefinesExample {

    /**
     * Tagged union analogue for the second REDEFINES example, which
     * overlaid a {@code PIC X(10)} display value and a {@code COMP-2}
     * floating point value on the same storage.
     */
    public static final class DataValue {

        public enum DataType {
            DISPLAY, COMP
        }

        private final DataType type;
        private String displayValue;
        private double compValue;

        public DataValue(DataType type) {
            this.type = type;
        }

        public DataType getType() {
            return type;
        }

        public String getDisplayValue() {
            return displayValue;
        }

        public void setDisplayValue(String displayValue) {
            this.displayValue = displayValue;
        }

        public double getCompValue() {
            return compValue;
        }

        public void setCompValue(double compValue) {
            this.compValue = compValue;
        }
    }

    public static void main(String[] args) {
        List<Customer> customers = setupTestData();
        displayCustomerData(customers);

        DataValue[] dataValues = setupSecondTestData();
        displaySecondTestData(dataValues);
    }

    private static List<Customer> setupTestData() {
        System.out.println();
        System.out.println("1. Person record with first/last name entered.");
        PersonCustomer person1 = new PersonCustomer();
        person1.setFirstName("test-first");
        person1.setLastName("test-last");
        person1.setStreetAddress("123 fake st");
        person1.setState("NV");
        person1.setZipCode(12345);

        System.out.println("2. Corp record with corp name entered.");
        CorpCustomer corp = new CorpCustomer();
        corp.setCorpName("no-name corp");
        corp.setStreetAddress("567 real st");
        corp.setState("NY");
        corp.setZipCode(11795);

        System.out.println("3. Person record with corp name entered.");
        // Mirrors the COBOL test where a PERSON-typed record stores its
        // name through the corp-name redefine. We keep the Person type
        // and store the corp string in the lastName field for fidelity.
        PersonCustomer person2 = new PersonCustomer();
        person2.setLastName("SET CORP VALUE");
        person2.setStreetAddress("890 what st");
        person2.setState("MA");
        person2.setZipCode(9345);

        List<Customer> all = new ArrayList<>();
        all.add(person1);
        all.add(corp);
        all.add(person2);
        return all;
    }

    private static void displayCustomerData(List<Customer> customers) {
        System.out.println();
        System.out.println("Displaying fake customer data:");
        System.out.println("------------------------------");
        System.out.println();

        for (Customer c : customers) {
            if (c instanceof PersonCustomer p) {
                System.out.println("Customer Type: PERSON");
                System.out.println("First Name: " + nullToEmpty(p.getFirstName()));
                System.out.println("Last Name: " + nullToEmpty(p.getLastName()));
            } else if (c instanceof CorpCustomer corp) {
                System.out.println("Customer Type: CORP");
                System.out.println("Company name: " + nullToEmpty(corp.getCorpName()));
            }
            System.out.println("Address: ");
            System.out.println(nullToEmpty(c.getStreetAddress()));
            System.out.println(nullToEmpty(c.getState()) + ", "
                    + String.format("%05d", c.getZipCode()));
            System.out.println("------------------------------");
            System.out.println();
        }
    }

    private static DataValue[] setupSecondTestData() {
        DataValue d0 = new DataValue(DataValue.DataType.DISPLAY);
        d0.setDisplayValue("ABC123");

        DataValue d1 = new DataValue(DataValue.DataType.COMP);
        d1.setCompValue(12345.63);
        // The COBOL example also peeks at the same memory through the
        // display redefine; we put a placeholder textual representation
        // in the display slot to mimic that behaviour without reusing
        // raw memory, which Java does not allow.
        d1.setDisplayValue(Double.toString(d1.getCompValue()));

        return new DataValue[] {d0, d1};
    }

    private static void displaySecondTestData(DataValue[] values) {
        System.out.println();
        System.out.println("Redefines with different variable types:");
        System.out.println("----------------------------------------");
        System.out.println("Value entered in ws-data-disp-value: ABC123");
        System.out.println("ws-data-disp-value x(10): " + nullToEmpty(values[0].getDisplayValue()));
        System.out.println("ws-data-comp-value comp-2: " + values[0].getCompValue());
        System.out.println();
        System.out.println("----------------------------------------");
        System.out.println("Value entered in ws-data-comp-value: 12345.63");
        System.out.println("ws-data-disp-value x(10): " + nullToEmpty(values[1].getDisplayValue()));
        System.out.println("ws-data-comp-value comp-2: " + values[1].getCompValue());
        System.out.println();
    }

    private static String nullToEmpty(String s) {
        return s == null ? "" : s;
    }
}
