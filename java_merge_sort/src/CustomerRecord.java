/**
 * CustomerRecord.java
 * 
 * Data model representing a customer record from the COBOL merge_sort program.
 * Maintains the same field structure and sizes as the COBOL implementation.
 * 
 * Record Structure (135 characters total):
 * - Customer ID: 5 digits (positions 1-5)
 * - Last Name: 50 characters (positions 6-55)
 * - First Name: 50 characters (positions 56-105)
 * - Contract ID: 5 digits (positions 106-110)
 * - Comment: 25 characters (positions 111-135)
 */
public class CustomerRecord {
    private int customerId;
    private String lastName;
    private String firstName;
    private int contractId;
    private String comment;

    /**
     * Default constructor
     */
    public CustomerRecord() {
        this.customerId = 0;
        this.lastName = "";
        this.firstName = "";
        this.contractId = 0;
        this.comment = "";
    }

    /**
     * Parameterized constructor
     */
    public CustomerRecord(int customerId, String lastName, String firstName, 
                         int contractId, String comment) {
        this.customerId = customerId;
        this.lastName = lastName;
        this.firstName = firstName;
        this.contractId = contractId;
        this.comment = comment;
    }

    public int getCustomerId() {
        return customerId;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public int getContractId() {
        return contractId;
    }

    public String getComment() {
        return comment;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setContractId(int contractId) {
        this.contractId = contractId;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * Converts the record to a fixed-width format string (135 characters)
     * matching the COBOL record layout.
     * 
     * @return Fixed-width formatted string
     */
    public String toFixedWidthString() {
        return String.format("%05d%-50s%-50s%05d%-25s",
            customerId,
            lastName,
            firstName,
            contractId,
            comment);
    }

    /**
     * Parses a fixed-width format string into a CustomerRecord object.
     * 
     * @param line Fixed-width formatted string (135 characters)
     * @return CustomerRecord object
     * @throws IllegalArgumentException if line length is invalid
     */
    public static CustomerRecord fromFixedWidthString(String line) {
        if (line == null || line.length() < 135) {
            throw new IllegalArgumentException(
                "Invalid record length. Expected 135 characters, got: " + 
                (line == null ? "null" : line.length()));
        }

        CustomerRecord record = new CustomerRecord();
        
        record.setCustomerId(Integer.parseInt(line.substring(0, 5)));
        
        record.setLastName(line.substring(5, 55).trim());
        
        record.setFirstName(line.substring(55, 105).trim());
        
        record.setContractId(Integer.parseInt(line.substring(105, 110)));
        
        record.setComment(line.substring(110, 135).trim());
        
        return record;
    }

    @Override
    public String toString() {
        return String.format("CustomerRecord[customerId=%d, lastName='%s', firstName='%s', " +
                           "contractId=%d, comment='%s']",
            customerId, lastName, firstName, contractId, comment);
    }
}
