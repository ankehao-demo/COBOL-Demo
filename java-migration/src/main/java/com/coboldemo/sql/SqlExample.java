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
 * Java port of {@code sql/sql_example.cbl}.
 *
 * The COBOL original uses embedded SQL via the esqlOC precompiler against
 * a PostgreSQL database. We translate cursors to JDBC {@link ResultSet}s
 * and the EXEC SQL statements to {@link PreparedStatement}s.
 *
 * <p>Connection details default to the same values used in the COBOL
 * source and can be overridden with the system properties
 * {@code db.url}, {@code db.user}, and {@code db.password}.
 */
public class SqlExample {

    private static final String DEFAULT_URL =
            "jdbc:postgresql://localhost:5432/cobol_db_example";
    private static final String DEFAULT_USER = "postgres";
    private static final String DEFAULT_PASSWORD = "password";

    public static final class Account {
        public final int id;
        public final String firstName;
        public final String lastName;
        public final String phone;
        public final String address;
        public final String isEnabled;
        public final String createDt;
        public final String modDt;

        public Account(int id, String firstName, String lastName, String phone,
                       String address, String isEnabled, String createDt, String modDt) {
            this.id = id;
            this.firstName = firstName;
            this.lastName = lastName;
            this.phone = phone;
            this.address = address;
            this.isEnabled = isEnabled;
            this.createDt = createDt;
            this.modDt = modDt;
        }
    }

    public static void main(String[] args) {
        String url = System.getProperty("db.url", DEFAULT_URL);
        String user = System.getProperty("db.user", DEFAULT_USER);
        String password = System.getProperty("db.password", DEFAULT_PASSWORD);

        System.out.println();
        System.out.println("COBOL SQL DB Example Program");
        System.out.println("----------------------------");
        System.out.println();

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.println();
                System.out.println("1) Display all accounts");
                System.out.println("2) Display disabled accounts");
                System.out.println("3) Query accounts");
                System.out.println("4) Exit");
                System.out.print("Selection: ");
                String choice = scanner.hasNextLine() ? scanner.nextLine().trim() : "4";

                switch (choice) {
                    case "1" -> displayAccounts(conn, allAccounts(conn));
                    case "2" -> displayAccounts(conn, disabledAccounts(conn));
                    case "3" -> queryAccountsLoop(conn, scanner);
                    case "4" -> {
                        System.out.println("Disconnected.");
                        return;
                    }
                    default -> System.out.println("Please make a selection between 1-4");
                }
            }
        } catch (SQLException e) {
            System.out.println();
            System.out.println("SQL Error:");
            System.out.println("SQLCODE: " + e.getErrorCode());
            System.out.println("SQLSTATE: " + e.getSQLState());
            System.out.println("ERROR MESSAGE: " + e.getMessage());
        }
    }

    private static List<Account> allAccounts(Connection conn) throws SQLException {
        String sql = "SELECT id, first_name, last_name, phone, address, is_enabled, "
                + "create_dt, mod_dt FROM accounts ORDER BY id";
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            return collect(rs);
        }
    }

    private static List<Account> disabledAccounts(Connection conn) throws SQLException {
        String sql = "SELECT id, first_name, last_name, phone, address, is_enabled, "
                + "create_dt, mod_dt FROM accounts WHERE is_enabled = 'N' ORDER BY id";
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            return collect(rs);
        }
    }

    private static void queryAccountsLoop(Connection conn, Scanner scanner) throws SQLException {
        boolean again = true;
        while (again) {
            System.out.println();
            System.out.print("Enter search value: ");
            String term = scanner.hasNextLine() ? scanner.nextLine().trim() : "";
            String pattern = "%" + term + "%";

            String sql = "SELECT id, first_name, last_name, phone, address, is_enabled, "
                    + "create_dt, mod_dt FROM accounts "
                    + "WHERE first_name LIKE ? OR last_name LIKE ? "
                    + "OR phone LIKE ? OR address LIKE ? "
                    + "ORDER BY id";

            List<Account> results;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, pattern);
                ps.setString(2, pattern);
                ps.setString(3, pattern);
                ps.setString(4, pattern);
                System.out.println("Search value: " + pattern);
                System.out.println("Length: " + pattern.length());
                try (ResultSet rs = ps.executeQuery()) {
                    results = collect(rs);
                }
            }
            displayAccounts(conn, results);

            System.out.println();
            System.out.print("Search again? (Y/[N]) ");
            String answer = scanner.hasNextLine() ? scanner.nextLine().trim() : "N";
            again = answer.toUpperCase().startsWith("Y");
        }
    }

    private static List<Account> collect(ResultSet rs) throws SQLException {
        List<Account> out = new ArrayList<>();
        while (rs.next()) {
            out.add(new Account(
                    rs.getInt(1),
                    safe(rs.getString(2)),
                    safe(rs.getString(3)),
                    safe(rs.getString(4)),
                    safe(rs.getString(5)),
                    safe(rs.getString(6)),
                    safe(rs.getString(7)),
                    safe(rs.getString(8))));
        }
        return out;
    }

    private static void displayAccounts(Connection conn, List<Account> accounts) {
        System.out.println();
        System.out.println("ACCOUNTS:");
        System.out.println();
        System.out.println(" ID   | First    | Last     | Phone      |"
                + " Address                | Enabled ");
        System.out.println("------|----------|----------|------------|"
                + "------------------------|---------");
        for (Account a : accounts) {
            System.out.println(String.format("%05d", a.id)
                    + " | " + padRight(a.firstName, 8)
                    + " | " + padRight(a.lastName, 8)
                    + " | " + padRight(a.phone, 10)
                    + " | " + padRight(a.address, 22)
                    + " | " + safe(a.isEnabled));
        }
    }

    private static String padRight(String s, int width) {
        String safe = safe(s);
        if (safe.length() >= width) {
            return safe.substring(0, width);
        }
        return String.format("%-" + width + "s", safe);
    }

    private static String safe(String s) {
        return s == null ? "" : s;
    }
}
