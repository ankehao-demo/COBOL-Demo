package com.example.cobol.sql;

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
 * <p>The COBOL version connects to a PostgreSQL database via embedded SQL
 * (esqlOC + unixODBC) and offers a small interactive menu over an
 * {@code accounts} table. The Java port uses JDBC directly and keeps the
 * same menu / queries.
 *
 * <p>Database connection settings can be overridden with the standard
 * environment variables {@code PGURL}, {@code PGUSER}, and {@code PGPASSWORD}.
 * Defaults match the original example: {@code jdbc:postgresql://localhost:5432/cobol_db_example}
 * and user {@code postgres}/{@code password}.
 */
public final class SqlExample {

    private static final String DEFAULT_URL = "jdbc:postgresql://localhost:5432/cobol_db_example";
    private static final String DEFAULT_USER = "postgres";
    private static final String DEFAULT_PASSWORD = "password";

    /** Account row. */
    public static final class Account {
        public int id;
        public String firstName;
        public String lastName;
        public String phone;
        public String address;
        public String isEnabled;
        public String createDt;
        public String modDt;
    }

    private SqlExample() {}

    public static void main(String[] args) {
        String url = envOrDefault("PGURL", DEFAULT_URL);
        String user = envOrDefault("PGUSER", DEFAULT_USER);
        String pass = envOrDefault("PGPASSWORD", DEFAULT_PASSWORD);

        System.out.println();
        System.out.println("COBOL SQL DB Example Program");
        System.out.println("----------------------------");
        System.out.println();

        Connection conn;
        try {
            conn = DriverManager.getConnection(url, user, pass);
        } catch (SQLException e) {
            System.err.println("Failed to connect to " + url + ": " + e.getMessage());
            System.err.println("Set PGURL/PGUSER/PGPASSWORD env vars or run "
                    + "create_test_db.sql against your DB first.");
            return;
        }

        try (Connection c = conn; Scanner in = new Scanner(System.in)) {
            while (true) {
                System.out.println();
                System.out.println("1) Display all accounts");
                System.out.println("2) Display disabled accounts");
                System.out.println("3) Query accounts");
                System.out.println("4) Exit");
                System.out.print("Selection: ");

                if (!in.hasNextLine()) {
                    break;
                }
                String choice = in.nextLine().trim();
                switch (choice) {
                    case "1":
                        displayAccounts(c, queryAll(c));
                        break;
                    case "2":
                        displayAccounts(c, queryDisabled(c));
                        break;
                    case "3":
                        queryAccounts(c, in);
                        break;
                    case "4":
                        System.out.println("Disconnected.");
                        return;
                    default:
                        System.out.println("Please make a selection between 1-4");
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
        }
    }

    static List<Account> queryAll(Connection c) throws SQLException {
        String sql = "SELECT id, first_name, last_name, phone, address, "
                + "is_enabled, create_dt, mod_dt FROM accounts ORDER BY id";
        try (Statement st = c.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            return collect(rs);
        }
    }

    static List<Account> queryDisabled(Connection c) throws SQLException {
        String sql = "SELECT id, first_name, last_name, phone, address, "
                + "is_enabled, create_dt, mod_dt FROM accounts "
                + "WHERE is_enabled = 'N' ORDER BY id";
        try (Statement st = c.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            return collect(rs);
        }
    }

    static void queryAccounts(Connection c, Scanner in) throws SQLException {
        String sql = "SELECT id, first_name, last_name, phone, address, "
                + "is_enabled, create_dt, mod_dt FROM accounts "
                + "WHERE first_name LIKE ? OR last_name LIKE ? "
                + "OR phone LIKE ? OR address LIKE ? ORDER BY id";

        boolean again = true;
        while (again) {
            System.out.println();
            System.out.print("Enter search value: ");
            if (!in.hasNextLine()) {
                return;
            }
            String search = "%" + in.nextLine().trim() + "%";
            System.out.println("Search value: " + search);
            System.out.println("Length: " + search.length());

            try (PreparedStatement ps = c.prepareStatement(sql)) {
                for (int i = 1; i <= 4; i++) {
                    ps.setString(i, search);
                }
                try (ResultSet rs = ps.executeQuery()) {
                    displayAccounts(c, collect(rs));
                }
            }

            System.out.println();
            System.out.print("Search again? (Y/[N]) ");
            if (!in.hasNextLine()) {
                return;
            }
            again = "Y".equalsIgnoreCase(in.nextLine().trim());
        }
    }

    private static List<Account> collect(ResultSet rs) throws SQLException {
        List<Account> out = new ArrayList<>();
        while (rs.next()) {
            Account a = new Account();
            a.id = rs.getInt(1);
            a.firstName = rs.getString(2);
            a.lastName = rs.getString(3);
            a.phone = rs.getString(4);
            a.address = rs.getString(5);
            a.isEnabled = rs.getString(6);
            a.createDt = rs.getString(7);
            a.modDt = rs.getString(8);
            out.add(a);
        }
        return out;
    }

    private static void displayAccounts(Connection c, List<Account> rows) {
        System.out.println();
        System.out.println("ACCOUNTS:");
        System.out.println();
        System.out.println(" ID   | First    | Last     | Phone      | Address                | Enabled");
        System.out.println("------|----------|----------|------------|------------------------|--------");
        for (Account a : rows) {
            System.out.printf("%5d | %-8s | %-8s | %-10s | %-22s | %s%n",
                    a.id,
                    truncate(a.firstName, 8),
                    truncate(a.lastName, 8),
                    truncate(a.phone, 10),
                    truncate(a.address, 22),
                    a.isEnabled);
        }
    }

    private static String truncate(String s, int n) {
        if (s == null) {
            return "";
        }
        return s.length() > n ? s.substring(0, n) : s;
    }

    private static String envOrDefault(String key, String defaultValue) {
        String v = System.getenv(key);
        return v == null || v.isBlank() ? defaultValue : v;
    }
}
