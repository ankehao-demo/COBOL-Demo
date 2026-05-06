package com.example.cobol.redefines;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Java port of {@code redifines/redefines.cbl}.
 *
 * <p>The COBOL {@code REDEFINES} clause lets one storage location be viewed
 * with two different layouts. Java has no native equivalent, so we model it
 * with a class that exposes the same backing fields under two
 * "interpretations" — corp-name vs first/last name — and uses
 * {@link ByteBuffer} for the second example, which redefines a 10-character
 * string field as an 8-byte double.
 */
public final class RedefinesTest {

    private static final int CUSTOMER_TYPE_PERSON = 1;
    private static final int CUSTOMER_TYPE_CORP = 2;

    /** Represents the union (REDEFINES) of person- and corp- views of a customer. */
    private static final class Customer {
        int type;                   // 1 = person, 2 = corp
        String firstName = "";      // 10 chars (person view)
        String lastName = "";       // 20 chars (person view)
        String streetAddress = "";  // 20 chars
        String state = "";          //  2 chars
        int zipCode;                // 5-digit numeric

        /** Corp name uses the same backing storage as first/last (concatenated). */
        String corpName() {
            return (firstName + lastName).strip();
        }

        void setCorpName(String value) {
            String padded = String.format("%-30s", value);
            this.firstName = padded.substring(0, 10);
            this.lastName = padded.substring(10);
        }
    }

    /** Represents a 10-byte field viewed either as text or as a comp-2 (double). */
    private static final class DiffDataType {
        char dataType;                  // 'D' or 'C'
        // 10-byte backing buffer.
        final byte[] bytes = new byte[10];

        String displayValue() {
            int len = bytes.length;
            for (int i = 0; i < bytes.length; i++) {
                if (bytes[i] == 0) {
                    len = i;
                    break;
                }
            }
            return new String(bytes, 0, len);
        }

        void setDisplayValue(String s) {
            byte[] src = s.getBytes();
            for (int i = 0; i < bytes.length; i++) {
                bytes[i] = i < src.length ? src[i] : (byte) ' ';
            }
        }

        double compValue() {
            // Read the first 8 bytes as a little-endian double (COMP-2).
            return ByteBuffer.wrap(bytes, 0, 8)
                    .order(ByteOrder.LITTLE_ENDIAN)
                    .getDouble();
        }

        void setCompValue(double v) {
            ByteBuffer.wrap(bytes, 0, 8)
                    .order(ByteOrder.LITTLE_ENDIAN)
                    .putDouble(v);
            // bytes 8-9 left as-is; matches COBOL's behaviour where the
            // unused bytes of a redefined field are not zeroed.
        }
    }

    private RedefinesTest() {}

    public static void main(String[] args) {
        // First test data set — three "customers".
        Customer[] customers = new Customer[3];
        for (int i = 0; i < customers.length; i++) {
            customers[i] = new Customer();
        }

        System.out.println();
        System.out.println("1. Person record with first/last name entered.");
        customers[0].type = CUSTOMER_TYPE_PERSON;
        customers[0].firstName = "test-first";
        customers[0].lastName = "test-last";
        customers[0].streetAddress = "123 fake st";
        customers[0].state = "NV";
        customers[0].zipCode = 12345;

        System.out.println("2. Corp record with corp name entered.");
        customers[1].type = CUSTOMER_TYPE_CORP;
        customers[1].setCorpName("no-name corp");
        customers[1].streetAddress = "567 real st";
        customers[1].state = "NY";
        customers[1].zipCode = 11795;

        System.out.println("3. Person record with corp name entered.");
        customers[2].type = CUSTOMER_TYPE_PERSON;
        customers[2].setCorpName("SET CORP VALUE");
        customers[2].streetAddress = "890 what st";
        customers[2].state = "MA";
        customers[2].zipCode = 9345;

        System.out.println();
        System.out.println("Displaying fake customer data:");
        System.out.println("------------------------------");
        System.out.println();

        for (Customer c : customers) {
            if (c.type == CUSTOMER_TYPE_PERSON) {
                System.out.println("Customer Type: PERSON");
                System.out.println("First Name: " + c.firstName);
                System.out.println("Last Name: " + c.lastName);
            } else {
                System.out.println("Customer Type: CORP");
                System.out.println("Company name: " + c.corpName());
            }
            System.out.println("Address: ");
            System.out.println(c.streetAddress);
            System.out.printf("%s, %05d%n", c.state, c.zipCode);
            System.out.println("------------------------------");
            System.out.println();
        }

        // Second test data set — type-redefining buffer.
        DiffDataType[] vals = new DiffDataType[] {new DiffDataType(), new DiffDataType()};

        vals[0].dataType = 'D';
        vals[0].setDisplayValue("ABC123");

        vals[1].dataType = 'C';
        vals[1].setCompValue(12345.63);

        System.out.println();
        System.out.println("Redefines with different variable types:");
        System.out.println("----------------------------------------");
        System.out.println("Value entered in ws-data-disp-value: ABC123");
        System.out.println("ws-data-disp-value x(10): " + vals[0].displayValue());
        System.out.println("ws-data-comp-value comp-2: " + vals[0].compValue());
        System.out.println();
        System.out.println("----------------------------------------");
        System.out.println("Value entered in ws-data-comp-value: 12345.63");
        System.out.println("ws-data-disp-value x(10): " + vals[1].displayValue());
        System.out.println("ws-data-comp-value comp-2: " + vals[1].compValue());
        System.out.println();
    }
}
