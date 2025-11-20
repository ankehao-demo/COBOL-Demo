package com.cobol.demo.redefines;

import java.util.ArrayList;
import java.util.List;

public class RedefinesTest {
    public static void main(String[] args) {
        RedefinesTest test = new RedefinesTest();
        test.run();
    }

    public void run() {
        List<Customer> customers = setupTestData();
        displayCustomerData(customers);
        
        List<DataType> dataTypes = setupSecondTestData();
        displaySecondTestData(dataTypes);
    }

    private List<Customer> setupTestData() {
        System.out.println();
        System.out.println("1. Person record with first/last name entered.");
        
        List<Customer> customers = new ArrayList<>();
        
        Customer customer1 = new Customer();
        customer1.setCustomerType(Customer.TYPE_PERSON);
        customer1.setFirstName("test-first");
        customer1.setLastName("test-last");
        customer1.setStreetAddress("123 fake st");
        customer1.setState("NV");
        customer1.setZipCode(12345);
        customers.add(customer1);
        
        System.out.println("2. Corp record with corp name entered.");
        Customer customer2 = new Customer();
        customer2.setCustomerType(Customer.TYPE_CORP);
        customer2.setCorpName("no-name corp");
        customer2.setStreetAddress("567 real st");
        customer2.setState("NY");
        customer2.setZipCode(11795);
        customers.add(customer2);
        
        System.out.println("3. Person record with corp name entered.");
        Customer customer3 = new Customer();
        customer3.setCustomerType(Customer.TYPE_PERSON);
        customer3.setCorpName("SET CORP VALUE");
        customer3.setStreetAddress("890 what st");
        customer3.setState("MA");
        customer3.setZipCode(9345);
        customers.add(customer3);
        
        return customers;
    }

    private void displayCustomerData(List<Customer> customers) {
        System.out.println();
        System.out.println("Displaying fake customer data:");
        System.out.println("------------------------------");
        System.out.println();
        
        for (Customer customer : customers) {
            if (customer.isPersonType()) {
                System.out.println("Customer Type: PERSON");
                System.out.println("First Name: " + customer.getFirstName());
                System.out.println("Last Name: " + customer.getLastName());
            } else {
                System.out.println("Customer Type: CORP");
                System.out.println("Company name: " + customer.getCorpName());
            }
            
            System.out.println("Address: ");
            System.out.println(customer.getStreetAddress());
            System.out.println(customer.getState() + ", " + customer.getZipCode());
            System.out.println("------------------------------");
            System.out.println();
        }
    }

    private List<DataType> setupSecondTestData() {
        List<DataType> dataTypes = new ArrayList<>();
        
        DataType dataType1 = new DataType();
        dataType1.setType(DataType.TYPE_DISPLAY);
        dataType1.setDisplayValue("ABC123");
        dataTypes.add(dataType1);
        
        DataType dataType2 = new DataType();
        dataType2.setType(DataType.TYPE_COMP);
        dataType2.setCompValue(12345.63);
        dataTypes.add(dataType2);
        
        return dataTypes;
    }

    private void displaySecondTestData(List<DataType> dataTypes) {
        System.out.println();
        System.out.println("Redefines with different variable types:");
        System.out.println("----------------------------------------");
        System.out.println("Value entered in ws-data-disp-value: ABC123");
        System.out.println("ws-data-disp-value x(10): " + dataTypes.get(0).getDisplayValue());
        System.out.println("ws-data-comp-value comp-2: " + dataTypes.get(0).getCompValue());
        System.out.println();
        System.out.println("----------------------------------------");
        System.out.println("Value entered in ws-data-comp-value: 12345.63");
        System.out.println("ws-data-disp-value x(10): " + dataTypes.get(1).getDisplayValue());
        System.out.println("ws-data-comp-value comp-2: " + dataTypes.get(1).getCompValue());
        System.out.println();
    }
}
