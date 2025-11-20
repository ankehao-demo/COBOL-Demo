package com.cobol.demo.sql;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SqlExample {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/cobol_db_example";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "password";
    
    private Connection connection;
    private Scanner scanner;

    public SqlExample() {
        this.scanner = new Scanner(System.in);
    }

    public static void main(String[] args) {
        SqlExample app = new SqlExample();
        app.run();
    }

    public void run() {
        System.out.println();
        System.out.println("COBOL SQL DB Example Program");
        System.out.println("----------------------------");
        System.out.println();

        try {
            connectToDatabase();
            mainMenu();
        } catch (SQLException e) {
            handleSqlError(e);
        } finally {
            disconnectFromDatabase();
        }
    }

    private void connectToDatabase() throws SQLException {
        connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        System.out.println("Connected to database.");
    }

    private void disconnectFromDatabase() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Disconnected.");
                System.out.println();
            } catch (SQLException e) {
                System.err.println("Error disconnecting: " + e.getMessage());
            }
        }
        if (scanner != null) {
            scanner.close();
        }
    }

    private void mainMenu() throws SQLException {
        boolean exit = false;
        
        while (!exit) {
            System.out.println();
            System.out.println("1) Display all accounts");
            System.out.println("2) Display disabled accounts");
            System.out.println("3) Query accounts");
            System.out.println("4) Exit");
            System.out.print("Selection: ");
            
            String choice = scanner.nextLine().trim();
            
            switch (choice) {
                case "1":
                    displayAllAccounts();
                    break;
                case "2":
                    displayDisabledAccounts();
                    break;
                case "3":
                    queryAccounts();
                    break;
                case "4":
                    exit = true;
                    break;
                default:
                    System.out.println("Please make a selection between 1-4");
                    break;
            }
        }
    }

    private void displayAllAccounts() throws SQLException {
        String sql = "SELECT ID, FIRST_NAME, LAST_NAME, PHONE, ADDRESS, IS_ENABLED, CREATE_DT, MOD_DT " +
                     "FROM ACCOUNTS ORDER BY ID";
        
        List<Account> accounts = new ArrayList<>();
        
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Account account = new Account(
                    rs.getInt("ID"),
                    rs.getString("FIRST_NAME"),
                    rs.getString("LAST_NAME"),
                    rs.getString("PHONE"),
                    rs.getString("ADDRESS"),
                    rs.getString("IS_ENABLED"),
                    rs.getString("CREATE_DT"),
                    rs.getString("MOD_DT")
                );
                accounts.add(account);
            }
        }
        
        displayAccountResults(accounts);
    }

    private void displayDisabledAccounts() throws SQLException {
        String sql = "SELECT ID, FIRST_NAME, LAST_NAME, PHONE, ADDRESS, IS_ENABLED, CREATE_DT, MOD_DT " +
                     "FROM ACCOUNTS WHERE IS_ENABLED = 'N' ORDER BY ID";
        
        List<Account> accounts = new ArrayList<>();
        
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Account account = new Account(
                    rs.getInt("ID"),
                    rs.getString("FIRST_NAME"),
                    rs.getString("LAST_NAME"),
                    rs.getString("PHONE"),
                    rs.getString("ADDRESS"),
                    rs.getString("IS_ENABLED"),
                    rs.getString("CREATE_DT"),
                    rs.getString("MOD_DT")
                );
                accounts.add(account);
            }
        }
        
        displayAccountResults(accounts);
    }

    private void queryAccounts() throws SQLException {
        boolean searchAgain = true;
        
        while (searchAgain) {
            System.out.println();
            System.out.print("Enter search value: ");
            String searchString = scanner.nextLine();
            
            String searchValue = "%" + searchString.trim() + "%";
            
            System.out.println("Search value: " + searchValue);
            System.out.println("Length: " + searchValue.length());
            
            String sql = "SELECT ID, FIRST_NAME, LAST_NAME, PHONE, ADDRESS, IS_ENABLED, CREATE_DT, MOD_DT " +
                         "FROM ACCOUNTS WHERE " +
                         "FIRST_NAME LIKE ? OR LAST_NAME LIKE ? OR PHONE LIKE ? OR ADDRESS LIKE ? " +
                         "ORDER BY ID";
            
            List<Account> accounts = new ArrayList<>();
            
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, searchValue);
                stmt.setString(2, searchValue);
                stmt.setString(3, searchValue);
                stmt.setString(4, searchValue);
                
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        Account account = new Account(
                            rs.getInt("ID"),
                            rs.getString("FIRST_NAME"),
                            rs.getString("LAST_NAME"),
                            rs.getString("PHONE"),
                            rs.getString("ADDRESS"),
                            rs.getString("IS_ENABLED"),
                            rs.getString("CREATE_DT"),
                            rs.getString("MOD_DT")
                        );
                        accounts.add(account);
                    }
                }
            }
            
            displayAccountResults(accounts);
            
            System.out.println();
            System.out.print("Search again? (Y/[N]) ");
            String response = scanner.nextLine().trim().toUpperCase();
            searchAgain = "Y".equals(response);
        }
    }

    private void displayAccountResults(List<Account> accounts) {
        System.out.println();
        System.out.println("ACCOUNTS:");
        System.out.println();
        System.out.println(" ID   | First    | Last     | Phone      | Address                | Enabled ");
        System.out.println("------|----------|----------|------------|------------------------|--------");
        
        for (Account account : accounts) {
            System.out.printf("%5d | %-8s | %-8s | %-10s | %-22s | %s%n",
                account.getId(),
                padOrTrim(account.getFirstName(), 8),
                padOrTrim(account.getLastName(), 8),
                padOrTrim(account.getPhone(), 10),
                padOrTrim(account.getAddress(), 22),
                account.getIsEnabled()
            );
        }
    }

    private String padOrTrim(String str, int length) {
        if (str == null) {
            str = "";
        }
        if (str.length() > length) {
            return str.substring(0, length);
        }
        return String.format("%-" + length + "s", str);
    }

    private void handleSqlError(SQLException e) {
        System.out.println();
        System.out.println("SQL Error:");
        System.out.println("SQLCODE: " + e.getErrorCode());
        System.out.println("SQLSTATE: " + e.getSQLState());
        System.out.println("ERROR MESSAGE: " + e.getMessage());
        System.out.println();
        
        disconnectFromDatabase();
        System.exit(1);
    }
}
