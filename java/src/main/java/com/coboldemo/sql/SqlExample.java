package com.coboldemo.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Migrated from sql/sql_example.cbl
 * Demonstrates connecting to and querying a PostgreSQL database.
 * Prerequisites: PostgreSQL with create_test_db.sql applied.
 */
public class SqlExample {

    private static final String DB_URL = "jdbc:postgresql://localhost:5432/cobol_db_example";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "password";

    static class Account {
        int id;
        String firstName;
        String lastName;
        String phone;
        String address;
        String isEnabled;
        String createDt;
        String modDt;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println();
        System.out.println("COBOL SQL DB Example Program");
        System.out.println("----------------------------");
        System.out.println();

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            System.out.println("Connected to database.");

            boolean running = true;
            while (running) {
                System.out.println();
                System.out.println("1) Display all accounts");
                System.out.println("2) Display disabled accounts");
                System.out.println("3) Query accounts");
                System.out.println("4) Exit");
                System.out.print("Selection: ");
                String choice = scanner.nextLine().trim();

                switch (choice) {
                    case "1":
                        displayAllAccounts(conn);
                        break;
                    case "2":
                        displayDisabledAccounts(conn);
                        break;
                    case "3":
                        queryAccounts(conn, scanner);
                        break;
                    case "4":
                        running = false;
                        break;
                    default:
                        System.out.println("Please make a selection between 1-4");
                }
            }

            System.out.println("Disconnected.");
            System.out.println();

        } catch (SQLException e) {
            System.err.println("Database error: SQLSTATE=" + e.getSQLState()
                    + " Message=" + e.getMessage());
        }

        scanner.close();
    }

    private static void displayAllAccounts(Connection conn) {
        String sql = "SELECT id, first_name, last_name, phone, address, is_enabled, "
                + "create_dt, mod_dt FROM accounts ORDER BY id";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            List<Account> accounts = extractAccounts(rs);
            displayAccountResults(accounts);

        } catch (SQLException e) {
            checkSqlState(e);
        }
    }

    private static void displayDisabledAccounts(Connection conn) {
        String sql = "SELECT id, first_name, last_name, phone, address, is_enabled, "
                + "create_dt, mod_dt FROM accounts WHERE is_enabled = 'N' ORDER BY id";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            List<Account> accounts = extractAccounts(rs);
            displayAccountResults(accounts);

        } catch (SQLException e) {
            checkSqlState(e);
        }
    }

    private static void queryAccounts(Connection conn, Scanner scanner) {
        String sql = "SELECT id, first_name, last_name, phone, address, is_enabled, "
                + "create_dt, mod_dt FROM accounts "
                + "WHERE first_name LIKE ? OR last_name LIKE ? "
                + "OR phone LIKE ? OR address LIKE ? ORDER BY id";

        boolean searchAgain = true;
        while (searchAgain) {
            System.out.print("Enter search value: ");
            String searchString = scanner.nextLine().trim();
            String likePattern = "%" + searchString + "%";

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, likePattern);
                pstmt.setString(2, likePattern);
                pstmt.setString(3, likePattern);
                pstmt.setString(4, likePattern);

                try (ResultSet rs = pstmt.executeQuery()) {
                    List<Account> accounts = extractAccounts(rs);
                    displayAccountResults(accounts);
                }
            } catch (SQLException e) {
                checkSqlState(e);
            }

            System.out.print("Search again? (Y/N): ");
            String again = scanner.nextLine().trim().toUpperCase();
            searchAgain = "Y".equals(again);
        }
    }

    private static List<Account> extractAccounts(ResultSet rs) throws SQLException {
        List<Account> accounts = new ArrayList<>();
        while (rs.next()) {
            Account acct = new Account();
            acct.id = rs.getInt("id");
            acct.firstName = rs.getString("first_name");
            acct.lastName = rs.getString("last_name");
            acct.phone = rs.getString("phone");
            acct.address = rs.getString("address");
            acct.isEnabled = rs.getString("is_enabled");
            acct.createDt = rs.getString("create_dt");
            acct.modDt = rs.getString("mod_dt");
            accounts.add(acct);
        }
        return accounts;
    }

    private static void displayAccountResults(List<Account> accounts) {
        if (accounts.isEmpty()) {
            System.out.println("No records found.");
            return;
        }

        System.out.println();
        System.out.printf("%-5s %-8s %-8s %-12s %-22s %-3s %-20s %-20s%n",
                "ID", "First", "Last", "Phone", "Address", "Ena", "Created", "Modified");
        System.out.println("-----------------------------------------------------------------------------------------------");

        for (Account acct : accounts) {
            System.out.printf("%-5d %-8s %-8s %-12s %-22s %-3s %-20s %-20s%n",
                    acct.id,
                    truncate(acct.firstName, 8),
                    truncate(acct.lastName, 8),
                    truncate(acct.phone, 12),
                    truncate(acct.address, 22),
                    acct.isEnabled,
                    truncate(acct.createDt, 20),
                    truncate(acct.modDt, 20));
        }

        System.out.println();
        System.out.println("Total records: " + accounts.size());
    }

    private static void checkSqlState(SQLException e) {
        System.err.println("SQL Error:");
        System.err.println("  SQLSTATE: " + e.getSQLState());
        System.err.println("  Message:  " + e.getMessage());
        System.err.println("  Code:     " + e.getErrorCode());
    }

    private static String truncate(String s, int maxLen) {
        if (s == null) return "";
        return s.length() > maxLen ? s.substring(0, maxLen) : s;
    }
}
