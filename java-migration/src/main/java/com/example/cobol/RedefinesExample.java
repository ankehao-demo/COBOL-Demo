package com.example.cobol;

import com.example.cobol.model.CorpCustomer;
import com.example.cobol.model.Customer;
import com.example.cobol.model.PersonCustomer;

import java.util.ArrayList;
import java.util.List;

public class RedefinesExample {
    
    public static void main(String[] args) {
        List<Customer> customers = setupTestData();
        
        displayCustomerData(customers);
        
        displaySecondTestData();
    }
    
    private static List<Customer> setupTestData() {
        List<Customer> customers = new ArrayList<>();
        
        System.out.println();
        System.out.println("1. Person record with first/last name entered.");
        PersonCustomer person1 = new PersonCustomer("test-first", "test-last", "123 fake st", "NV", 12345);
        customers.add(person1);
        
        System.out.println("2. Corp record with corp name entered.");
        CorpCustomer corp = new CorpCustomer("no-name corp", "567 real st", "NY", 11795);
        customers.add(corp);
        
        System.out.println("3. Person record with corp name entered.");
        PersonCustomer person2 = new PersonCustomer("SET CORP", "VALUE", "890 what st", "MA", 9345);
        customers.add(person2);
        
        return customers;
    }
    
    private static void displayCustomerData(List<Customer> customers) {
        System.out.println();
        System.out.println("Displaying fake customer data:");
        System.out.println("------------------------------");
        System.out.println();
        
        for (Customer customer : customers) {
            if (customer instanceof PersonCustomer) {
                PersonCustomer person = (PersonCustomer) customer;
                System.out.println(person);
            } else if (customer instanceof CorpCustomer) {
                CorpCustomer corp = (CorpCustomer) customer;
                System.out.println(corp);
            }
            
            System.out.println("------------------------------");
            System.out.println();
        }
    }
    
    private static void displaySecondTestData() {
        System.out.println();
        System.out.println("Redefines with different variable types:");
        System.out.println("----------------------------------------");
        System.out.println("Value entered in ws-data-disp-value: ABC123");
        
        String dispValue = "ABC123";
        System.out.println("ws-data-disp-value x(10): " + dispValue);
        
        byte[] bytes = dispValue.getBytes();
        double compValue = Double.longBitsToDouble(
            ((long)bytes[0] << 56) | 
            ((long)(bytes.length > 1 ? bytes[1] : 0) << 48) |
            ((long)(bytes.length > 2 ? bytes[2] : 0) << 40) |
            ((long)(bytes.length > 3 ? bytes[3] : 0) << 32) |
            ((long)(bytes.length > 4 ? bytes[4] : 0) << 24) |
            ((long)(bytes.length > 5 ? bytes[5] : 0) << 16) |
            ((long)(bytes.length > 6 ? bytes[6] : 0) << 8) |
            (long)(bytes.length > 7 ? bytes[7] : 0)
        );
        System.out.println("ws-data-comp-value comp-2: " + compValue);
        System.out.println();
        
        System.out.println("----------------------------------------");
        System.out.println("Value entered in ws-data-comp-value: 12345.63");
        
        double originalCompValue = 12345.63;
        long longBits = Double.doubleToLongBits(originalCompValue);
        byte[] compBytes = new byte[8];
        for (int i = 0; i < 8; i++) {
            compBytes[i] = (byte)((longBits >> (56 - i * 8)) & 0xFF);
        }
        String reconstructedDispValue = new String(compBytes);
        System.out.println("ws-data-disp-value x(10): " + reconstructedDispValue);
        System.out.println("ws-data-comp-value comp-2: " + originalCompValue);
        System.out.println();
    }
}
