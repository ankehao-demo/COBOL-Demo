package com.coboldemo.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Java equivalent of sql/sql_example.cbl
 *
 * Demonstrates COBOL embedded SQL (EXEC SQL) with PostgreSQL. In Java,
 * JDBC provides the same capabilities via Connection, PreparedStatement,
 * and ResultSet.
 *
 * COBOL Mapping:
 *   EXEC SQL CONNECT TO :connection-string  → DriverManager.getConnection()
 *   EXEC SQL DECLARE ... CURSOR FOR SELECT  → PreparedStatement
 *   EXEC SQL OPEN cursor                    → executeQuery() → ResultSet
 *   EXEC SQL FETCH cursor INTO :vars        → resultSet.next() + getXxx()
 *   EXEC SQL CLOSE cursor                   → resultSet.close()
 *   EXEC SQL CONNECT RESET                  → connection.close()
 *   SQLSTATE / SQLCODE                      → try-catch(SQLException)
 *
 * Prerequisites: PostgreSQL with create_test_db.sql executed.
 * JDBC URL: jdbc:postgresql://localhost:5432/cobol_db_example
 */
public class SqlExample {

    /** Account POJO matching the COBOL ws-account-record layout. */
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

    // Connection parameters from environment variables (with defaults for local development)
    private static final String JDBC_URL = System.getenv().getOrDefault(
            "COBOL_DB_URL", "jdbc:postgresql://localhost:5432/cobol_db_example");
    private static final String DB_USER = System.getenv().getOrDefault(
            "COBOL_DB_USER", "postgres");
    private static final String DB_PASSWORD = System.getenv().getOrDefault(
            "COBOL_DB_PASSWORD", "");

    private static final String SQL_ALL =
            "SELECT id, first_name, last_name, phone, address, is_enabled, create_dt, mod_dt"
            + " FROM accounts ORDER BY id";

    private static final String SQL_DISABLED =
            "SELECT id, first_name, last_name, phone, address, is_enabled, create_dt, mod_dt"
            + " FROM accounts WHERE is_enabled = 'N' ORDER BY id";

    private static final String SQL_QUERY =
            "SELECT id, first_name, last_name, phone, address, is_enabled, create_dt, mod_dt"
            + " FROM accounts"
            + " WHERE first_name LIKE ? OR last_name LIKE ? OR phone LIKE ? OR address LIKE ?"
            + " ORDER BY id";

    public static void main(String[] args) {
        System.out.println();
        System.out.println("COBOL SQL DB Example Program");
        System.out.println("----------------------------");
        System.out.println();

        try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)) {
            System.out.println("Connected to database.");

            Scanner scanner = new Scanner(System.in);

            // Main menu loop
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
                        break;
                }
            }

            scanner.close();
        } catch (SQLException e) {
            System.out.println();
            System.out.println("SQL Error:");
            System.out.println("SQLCODE: " + e.getErrorCode());
            System.out.println("SQLSTATE: " + e.getSQLState());
            if (e.getMessage() != null) {
                System.out.println("ERROR MESSAGE: " + e.getMessage());
            }
        }

        System.out.println("Disconnected.");
        System.out.println();
    }

    /** EXEC SQL OPEN ACCOUNT-ALL-CUR ... FETCH ... CLOSE */
    private static void displayAllAccounts(Connection conn) throws SQLException {
        List<Account> accounts = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(SQL_ALL);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                accounts.add(mapAccount(rs));
            }
        }
        displayAccountResults(accounts);
    }

    /** EXEC SQL OPEN ACCOUNT-DISABLED-CUR ... FETCH ... CLOSE */
    private static void displayDisabledAccounts(Connection conn) throws SQLException {
        List<Account> accounts = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(SQL_DISABLED);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                accounts.add(mapAccount(rs));
            }
        }
        displayAccountResults(accounts);
    }

    /** EXEC SQL OPEN ACCOUNT-QUERY-CUR ... FETCH ... CLOSE */
    private static void queryAccounts(Connection conn, Scanner scanner) throws SQLException {
        boolean searchAgain = true;
        while (searchAgain) {
            System.out.println();
            System.out.print("Enter search value: ");
            String searchString = scanner.nextLine().trim();

            // Add SQL LIKE wildcards: '%' + trimmed search + '%'
            String searchValue = "%" + searchString + "%";
            System.out.println("Search value: " + searchValue);
            System.out.println("Length: " + searchValue.length());

            List<Account> accounts = new ArrayList<>();
            try (PreparedStatement stmt = conn.prepareStatement(SQL_QUERY)) {
                stmt.setString(1, searchValue);
                stmt.setString(2, searchValue);
                stmt.setString(3, searchValue);
                stmt.setString(4, searchValue);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        accounts.add(mapAccount(rs));
                    }
                }
            }
            displayAccountResults(accounts);

            System.out.println();
            System.out.print("Search again? (Y/[N]) ");
            String again = scanner.nextLine().trim().toUpperCase();
            searchAgain = "Y".equals(again);
        }
    }

    /** Map a ResultSet row to an Account object. */
    private static Account mapAccount(ResultSet rs) throws SQLException {
        Account a = new Account();
        a.id = rs.getInt("id");
        a.firstName = rs.getString("first_name");
        a.lastName = rs.getString("last_name");
        a.phone = rs.getString("phone");
        a.address = rs.getString("address");
        a.isEnabled = rs.getString("is_enabled");
        a.createDt = rs.getString("create_dt");
        a.modDt = rs.getString("mod_dt");
        return a;
    }

    /** Display account results in a formatted table. */
    private static void displayAccountResults(List<Account> accounts) {
        System.out.println();
        System.out.println("ACCOUNTS:");
        System.out.println();
        System.out.printf(" %-5s | %-8s | %-8s | %-10s | %-22s | %-7s%n",
                "ID", "First", "Last", "Phone", "Address", "Enabled");
        System.out.println("------|----------|----------|------------|------------------------|--------");

        for (Account a : accounts) {
            System.out.printf("%05d | %-8s | %-8s | %-10s | %-22s | %s%n",
                    a.id, a.firstName, a.lastName, a.phone, a.address, a.isEnabled);
        }
    }
}
