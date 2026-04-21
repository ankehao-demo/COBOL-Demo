package com.coboldemo.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Port of {@code sql/sql_example.cbl} — connects to a PostgreSQL database
 * and offers a small menu for querying the {@code ACCOUNTS} table.
 *
 * <p>The COBOL program used embedded SQL via esqlOC; this version uses
 * JDBC with {@link PreparedStatement} for the parameterized LIKE search
 * and try-with-resources for connection / cursor management.
 */
public final class SqlExample {

    private static final String CONNECTION_URL =
            "jdbc:postgresql://localhost:5432/cobol_db_example";
    private static final String DEFAULT_USER = "postgres";
    private static final String DEFAULT_PASSWORD = "password";

    private static final String SELECT_ALL =
            "SELECT id, first_name, last_name, phone, address, is_enabled, create_dt, mod_dt "
                    + "FROM accounts ORDER BY id";
    private static final String SELECT_DISABLED =
            "SELECT id, first_name, last_name, phone, address, is_enabled, create_dt, mod_dt "
                    + "FROM accounts WHERE is_enabled = 'N' ORDER BY id";
    private static final String SELECT_QUERY =
            "SELECT id, first_name, last_name, phone, address, is_enabled, create_dt, mod_dt "
                    + "FROM accounts "
                    + "WHERE first_name LIKE ? OR last_name LIKE ? OR phone LIKE ? OR address LIKE ? "
                    + "ORDER BY id";

    private SqlExample() {
    }

    /** Java analogue of the COBOL {@code ws-account-record} structure. */
    public record AccountRecord(int id, String firstName, String lastName,
                                String phone, String address, String isEnabled,
                                Timestamp createDt, Timestamp modDt) {
    }

    public static void main(String[] args) {
        System.out.println();
        System.out.println("COBOL SQL DB Example Program");
        System.out.println("----------------------------");
        System.out.println();

        String user = System.getenv().getOrDefault("COBOL_DB_USER", DEFAULT_USER);
        String password = System.getenv().getOrDefault("COBOL_DB_PASSWORD", DEFAULT_PASSWORD);

        try (Connection connection = DriverManager.getConnection(CONNECTION_URL, user, password);
             Scanner scanner = new Scanner(System.in)) {
            menuLoop(connection, scanner);
            System.out.println("Disconnected.");
            System.out.println();
        } catch (SQLException e) {
            System.out.println();
            System.out.println("SQL Error:");
            System.out.println("SQLCODE: " + e.getErrorCode());
            System.out.println("SQLSTATE: " + e.getSQLState());
            System.out.println("ERROR MESSAGE: " + e.getMessage());
        }
    }

    private static void menuLoop(Connection connection, Scanner scanner) throws SQLException {
        while (true) {
            System.out.println();
            System.out.println("1) Display all accounts");
            System.out.println("2) Display disabled accounts");
            System.out.println("3) Query accounts");
            System.out.println("4) Exit");
            System.out.print("Selection: ");
            String selection = scanner.hasNextLine() ? scanner.nextLine().trim() : "4";
            switch (selection) {
                case "1" -> displayAccounts(connection, SELECT_ALL);
                case "2" -> displayAccounts(connection, SELECT_DISABLED);
                case "3" -> queryAccounts(connection, scanner);
                case "4" -> {
                    return;
                }
                default -> System.out.println("Please make a selection between 1-4");
            }
        }
    }

    private static void displayAccounts(Connection connection, String sql) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            displayAccountResults(collect(rs));
        }
    }

    private static void queryAccounts(Connection connection, Scanner scanner) throws SQLException {
        boolean searchAgain = true;
        while (searchAgain) {
            System.out.println();
            System.out.print("Enter search value: ");
            String raw = scanner.hasNextLine() ? scanner.nextLine().trim() : "";
            String value = "%" + raw + "%";
            System.out.println("Search value: " + value);
            System.out.println("Length: " + value.length());

            try (PreparedStatement stmt = connection.prepareStatement(SELECT_QUERY)) {
                stmt.setString(1, value);
                stmt.setString(2, value);
                stmt.setString(3, value);
                stmt.setString(4, value);
                try (ResultSet rs = stmt.executeQuery()) {
                    displayAccountResults(collect(rs));
                }
            }

            System.out.println();
            System.out.print("Search again? (Y/[N]) ");
            String again = scanner.hasNextLine() ? scanner.nextLine().trim() : "";
            searchAgain = again.equalsIgnoreCase("Y");
        }
    }

    private static List<AccountRecord> collect(ResultSet rs) throws SQLException {
        List<AccountRecord> results = new ArrayList<>();
        while (rs.next()) {
            results.add(new AccountRecord(
                    rs.getInt("id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("phone"),
                    rs.getString("address"),
                    rs.getString("is_enabled"),
                    rs.getTimestamp("create_dt"),
                    rs.getTimestamp("mod_dt")));
        }
        return results;
    }

    private static void displayAccountResults(List<AccountRecord> records) {
        System.out.println();
        System.out.println("ACCOUNTS:");
        System.out.println();
        System.out.println(" ID   | First    | Last     | Phone      |"
                + " Address                | Enabled ");
        System.out.println("------|----------|----------|------------|"
                + "------------------------|---------");
        for (AccountRecord record : records) {
            System.out.println(String.format("%5d | %-8s | %-8s | %-10s | %-22s | %s",
                    record.id(), safe(record.firstName()), safe(record.lastName()),
                    safe(record.phone()), safe(record.address()), safe(record.isEnabled())));
        }
    }

    private static String safe(String s) {
        return s == null ? "" : s;
    }
}
