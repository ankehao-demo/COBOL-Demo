package com.coboldemo.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Migrated from: sql/sql_example.cbl
 *
 * Demonstrates COBOL Embedded SQL (ESQL) with PostgreSQL. Maps COBOL
 * EXEC SQL statements to JDBC calls:
 * - CONNECT TO -> DriverManager.getConnection()
 * - DECLARE CURSOR / OPEN / FETCH / CLOSE -> PreparedStatement + ResultSet
 * - SQLSTATE / SQLCODE error checking -> try/catch SQLException
 * - CONNECT RESET -> Connection.close()
 *
 * Prerequisites: PostgreSQL with create_test_db.sql executed.
 */
public class SqlExample {

    static class Account {
        int id;
        String firstName;
        String lastName;
        String phone;
        String address;
        String isEnabled;
        String createDt;
        String modDt;

        @Override
        public String toString() {
            return String.format("%05d | %-8s | %-8s | %-10s | %-22s | %s",
                    id, firstName, lastName, phone, address, isEnabled);
        }
    }

    private static final String DEFAULT_JDBC_URL =
            "jdbc:postgresql://localhost:5432/cobol_db_example";
    private static final String DEFAULT_USER = "postgres";
    private static final String DEFAULT_PASSWORD = "password";

    public static void main(String[] args) {
        String jdbcUrl = System.getenv().getOrDefault("JDBC_URL", DEFAULT_JDBC_URL);
        String dbUser = System.getenv().getOrDefault("DB_USER", DEFAULT_USER);
        String dbPassword = System.getenv().getOrDefault("DB_PASSWORD", DEFAULT_PASSWORD);

        System.out.println();
        System.out.println("COBOL SQL DB Example Program");
        System.out.println("----------------------------");
        System.out.println();

        try (Connection conn = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword)) {
            System.out.println("Connected to database.");

            Scanner scanner = new Scanner(System.in);
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
                    case "1" -> displayAllAccounts(conn);
                    case "2" -> displayDisabledAccounts(conn);
                    case "3" -> queryAccounts(conn, scanner);
                    case "4" -> running = false;
                    default -> System.out.println("Please make a selection between 1-4");
                }
            }

            System.out.println("Disconnected.");
            System.out.println();
            scanner.close();

        } catch (SQLException e) {
            System.out.println();
            System.out.println("SQL Error:");
            System.out.println("SQLCODE: " + e.getErrorCode());
            System.out.println("SQLSTATE: " + e.getSQLState());
            if (e.getMessage() != null && !e.getMessage().isEmpty()) {
                System.out.println("ERROR MESSAGE: " + e.getMessage());
            }
            System.out.println();
        }
    }

    private static void displayAllAccounts(Connection conn) throws SQLException {
        String sql = """
                SELECT ID, FIRST_NAME, LAST_NAME, PHONE,
                       ADDRESS, IS_ENABLED, CREATE_DT, MOD_DT
                FROM ACCOUNTS
                ORDER BY ID""";

        List<Account> accounts = fetchAccounts(conn, sql);
        displayAccountResults(accounts);
    }

    private static void displayDisabledAccounts(Connection conn) throws SQLException {
        String sql = """
                SELECT ID, FIRST_NAME, LAST_NAME, PHONE,
                       ADDRESS, IS_ENABLED, CREATE_DT, MOD_DT
                FROM ACCOUNTS
                WHERE IS_ENABLED = 'N'
                ORDER BY ID""";

        List<Account> accounts = fetchAccounts(conn, sql);
        displayAccountResults(accounts);
    }

    private static void queryAccounts(Connection conn, Scanner scanner) throws SQLException {
        boolean searchAgain = true;

        while (searchAgain) {
            System.out.println();
            System.out.print("Enter search value: ");
            String searchString = scanner.nextLine().trim();

            String searchValue = "%" + searchString + "%";
            System.out.println("Search value: " + searchValue);
            System.out.println("Length: " + searchValue.length());

            String sql = """
                    SELECT ID, FIRST_NAME, LAST_NAME, PHONE,
                           ADDRESS, IS_ENABLED, CREATE_DT, MOD_DT
                    FROM ACCOUNTS
                    WHERE FIRST_NAME LIKE ?
                       OR LAST_NAME LIKE ?
                       OR PHONE LIKE ?
                       OR ADDRESS LIKE ?
                    ORDER BY ID""";

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, searchValue);
                pstmt.setString(2, searchValue);
                pstmt.setString(3, searchValue);
                pstmt.setString(4, searchValue);

                List<Account> accounts = new ArrayList<>();
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        accounts.add(mapResultSetToAccount(rs));
                    }
                }
                displayAccountResults(accounts);
            }

            System.out.println();
            System.out.print("Search again? (Y/[N]) ");
            String again = scanner.nextLine().trim().toUpperCase();
            searchAgain = "Y".equals(again);
        }
    }

    private static List<Account> fetchAccounts(Connection conn, String sql) throws SQLException {
        List<Account> accounts = new ArrayList<>();
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                accounts.add(mapResultSetToAccount(rs));
            }
        }
        return accounts;
    }

    private static Account mapResultSetToAccount(ResultSet rs) throws SQLException {
        Account account = new Account();
        account.id = rs.getInt("ID");
        account.firstName = rs.getString("FIRST_NAME");
        account.lastName = rs.getString("LAST_NAME");
        account.phone = rs.getString("PHONE");
        account.address = rs.getString("ADDRESS");
        account.isEnabled = rs.getString("IS_ENABLED");
        account.createDt = rs.getString("CREATE_DT");
        account.modDt = rs.getString("MOD_DT");
        return account;
    }

    private static void displayAccountResults(List<Account> accounts) {
        System.out.println();
        System.out.println("ACCOUNTS:");
        System.out.println();
        System.out.println(" ID   | First    | Last     | Phone      | Address                | Enabled ");
        System.out.println("------|----------|----------|------------|------------------------|---------");

        for (Account account : accounts) {
            System.out.println(account);
        }
    }
}
