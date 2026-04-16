package com.coboldemo.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * Migrated from: sql/sql_example.cbl
 * Original author: Erik Eriksen (2022-05-25, updated 2022-06-29)
 * Purpose: Demonstrates database connectivity with PostgreSQL using JDBC.
 *
 * Prerequisites:
 * - PostgreSQL running on localhost:5432
 * - Database created using sql/create_test_db.sql
 *
 * Usage: Run with JVM args for custom connection:
 *   -Ddb.url=jdbc:postgresql://localhost:5432/cobol_db_example
 *   -Ddb.user=postgres
 *   -Ddb.password=password
 */
public class SqlExample {

    private static final String DEFAULT_URL = "jdbc:postgresql://localhost:5432/cobol_db_example";
    private static final String DEFAULT_USER = "postgres";
    private static final String DEFAULT_PASSWORD = "password";

    public static void main(String[] args) {
        String dbUrl = System.getProperty("db.url", DEFAULT_URL);
        String dbUser = System.getProperty("db.user", DEFAULT_USER);
        String dbPassword = System.getProperty("db.password", DEFAULT_PASSWORD);

        System.out.println("SQL Example - JDBC PostgreSQL Demo");
        System.out.println("===================================");
        System.out.println();
        System.out.println("Connecting to: " + dbUrl);

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
            System.out.println("Connected successfully!");
            System.out.println();

            Scanner scanner = new Scanner(System.in);
            boolean running = true;

            while (running) {
                System.out.println("Menu:");
                System.out.println("  1. Display all accounts");
                System.out.println("  2. Display disabled accounts");
                System.out.println("  3. Search accounts");
                System.out.println("  4. Exit");
                System.out.print("Choice: ");

                String choice = scanner.nextLine().trim();

                switch (choice) {
                    case "1":
                        displayAllAccounts(conn);
                        break;
                    case "2":
                        displayDisabledAccounts(conn);
                        break;
                    case "3":
                        searchAccounts(conn, scanner);
                        break;
                    case "4":
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                        break;
                }
                System.out.println();
            }

            System.out.println("Disconnected. Goodbye!");

        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            System.err.println("SQLSTATE: " + e.getSQLState());
            System.err.println();
            System.err.println("Make sure PostgreSQL is running and the database has been created.");
            System.err.println("Run sql/create_test_db.sql to set up the database.");
        }
    }

    private static void displayAllAccounts(Connection conn) throws SQLException {
        System.out.println();
        System.out.println("All Accounts:");
        System.out.println("=============");

        String sql = "SELECT id, first_name, last_name, phone, address, is_enabled, create_dt, mod_dt "
                + "FROM accounts ORDER BY id";

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            printAccountResults(rs);
        }
    }

    private static void displayDisabledAccounts(Connection conn) throws SQLException {
        System.out.println();
        System.out.println("Disabled Accounts:");
        System.out.println("==================");

        String sql = "SELECT id, first_name, last_name, phone, address, is_enabled, create_dt, mod_dt "
                + "FROM accounts WHERE is_enabled = 'N' ORDER BY id";

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            printAccountResults(rs);
        }
    }

    private static void searchAccounts(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter search term: ");
        String searchTerm = scanner.nextLine().trim();

        if (searchTerm.isEmpty()) {
            System.out.println("No search term entered.");
            return;
        }

        System.out.println();
        System.out.println("Search Results for '" + searchTerm + "':");
        System.out.println("=".repeat(40));

        String likeTerm = "%" + searchTerm + "%";
        String sql = "SELECT id, first_name, last_name, phone, address, is_enabled, create_dt, mod_dt "
                + "FROM accounts "
                + "WHERE first_name LIKE ? OR last_name LIKE ? OR phone LIKE ? OR address LIKE ? "
                + "ORDER BY id";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, likeTerm);
            stmt.setString(2, likeTerm);
            stmt.setString(3, likeTerm);
            stmt.setString(4, likeTerm);

            try (ResultSet rs = stmt.executeQuery()) {
                printAccountResults(rs);
            }
        }
    }

    private static void printAccountResults(ResultSet rs) throws SQLException {
        int count = 0;
        System.out.printf("%-4s %-15s %-15s %-15s %-25s %-7s %-12s %-12s%n",
                "ID", "First Name", "Last Name", "Phone", "Address", "Active", "Created", "Modified");
        System.out.println("-".repeat(110));

        while (rs.next()) {
            int id = rs.getInt("id");
            String firstName = rs.getString("first_name");
            String lastName = rs.getString("last_name");
            String phone = rs.getString("phone");
            String address = rs.getString("address");
            String isEnabled = rs.getString("is_enabled");
            String createDt = rs.getString("create_dt");
            String modDt = rs.getString("mod_dt");

            System.out.printf("%-4d %-15s %-15s %-15s %-25s %-7s %-12s %-12s%n",
                    id,
                    firstName != null ? firstName : "",
                    lastName != null ? lastName : "",
                    phone != null ? phone : "",
                    address != null ? address : "",
                    "Y".equals(isEnabled) ? "Yes" : "No",
                    createDt != null ? createDt : "",
                    modDt != null ? modDt : "");
            count++;
        }

        System.out.println();
        System.out.println("Total records: " + count);
    }
}
