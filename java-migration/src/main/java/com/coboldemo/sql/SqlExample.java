package com.coboldemo.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Java port of sql/sql_example.cbl.
 *
 * The COBOL original uses the esqlOC precompiler against PostgreSQL; this
 * port uses JDBC directly. The generated helper {@code sql/generated_sql_ex.cbl}
 * is intentionally not ported (per the migration instructions it is a
 * precompiler artifact).
 */
public final class SqlExample {

    private static final String DEFAULT_URL =
            "jdbc:postgresql://localhost:5432/cobol_db_example";
    private static final String DEFAULT_USER = "postgres";
    private static final String DEFAULT_PASSWORD = "password";

    private SqlExample() {
    }

    public static void main(String[] args) {
        String url = System.getenv().getOrDefault("COBOL_DEMO_JDBC_URL",
                DEFAULT_URL);
        String user = System.getenv().getOrDefault("COBOL_DEMO_JDBC_USER",
                DEFAULT_USER);
        String password = System.getenv().getOrDefault(
                "COBOL_DEMO_JDBC_PASSWORD", DEFAULT_PASSWORD);

        System.out.println();
        System.out.println("COBOL SQL DB Example Program");
        System.out.println("----------------------------");
        System.out.println();

        try (Connection conn = DriverManager.getConnection(url, user,
                password)) {
            Scanner scanner = new Scanner(System.in);
            runMenu(conn, scanner);
            System.out.println("Disconnected.");
            System.out.println();
        } catch (SQLException ex) {
            System.out.println();
            System.out.println("SQL Error:");
            System.out.println("SQLCODE: " + ex.getErrorCode());
            System.out.println("SQLSTATE: " + ex.getSQLState());
            System.out.println("ERROR MESSAGE: " + ex.getMessage());
        }
    }

    private static void runMenu(Connection conn, Scanner scanner)
            throws SQLException {
        while (true) {
            System.out.println();
            System.out.println("1) Display all accounts");
            System.out.println("2) Display disabled accounts");
            System.out.println("3) Query accounts");
            System.out.println("4) Exit");
            System.out.print("Selection: ");
            String choice = scanner.hasNextLine()
                    ? scanner.nextLine().trim()
                    : "4";

            switch (choice) {
                case "1" -> displayAll(conn);
                case "2" -> displayDisabled(conn);
                case "3" -> queryAccounts(conn, scanner);
                case "4" -> {
                    return;
                }
                default -> System.out.println(
                        "Please make a selection between 1-4");
            }
        }
    }

    private static void displayAll(Connection conn) throws SQLException {
        String sql = "SELECT ID, FIRST_NAME, LAST_NAME, PHONE, ADDRESS, "
                + "IS_ENABLED, CREATE_DT, MOD_DT FROM ACCOUNTS ORDER BY ID";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            displayResults(fetchAccounts(rs));
        }
    }

    private static void displayDisabled(Connection conn) throws SQLException {
        String sql = "SELECT ID, FIRST_NAME, LAST_NAME, PHONE, ADDRESS, "
                + "IS_ENABLED, CREATE_DT, MOD_DT FROM ACCOUNTS "
                + "WHERE IS_ENABLED = 'N' ORDER BY ID";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            displayResults(fetchAccounts(rs));
        }
    }

    private static void queryAccounts(Connection conn, Scanner scanner)
            throws SQLException {
        boolean again = true;
        String sql = "SELECT ID, FIRST_NAME, LAST_NAME, PHONE, ADDRESS, "
                + "IS_ENABLED, CREATE_DT, MOD_DT FROM ACCOUNTS WHERE "
                + "FIRST_NAME LIKE ? OR LAST_NAME LIKE ? OR PHONE LIKE ? "
                + "OR ADDRESS LIKE ? ORDER BY ID";
        while (again) {
            System.out.println();
            System.out.print("Enter search value: ");
            String term = scanner.hasNextLine()
                    ? scanner.nextLine().trim()
                    : "";
            String like = "%" + term + "%";
            System.out.println("Search value: " + like);
            System.out.println("Length: " + like.length());

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, like);
                ps.setString(2, like);
                ps.setString(3, like);
                ps.setString(4, like);
                try (ResultSet rs = ps.executeQuery()) {
                    displayResults(fetchAccounts(rs));
                }
            }

            System.out.println();
            System.out.print("Search again? (Y/[N]) ");
            String answer = scanner.hasNextLine()
                    ? scanner.nextLine().trim().toUpperCase()
                    : "N";
            again = answer.equals("Y");
        }
    }

    private static List<Account> fetchAccounts(ResultSet rs)
            throws SQLException {
        List<Account> out = new ArrayList<>();
        while (rs.next()) {
            out.add(new Account(
                    rs.getInt("ID"),
                    rs.getString("FIRST_NAME"),
                    rs.getString("LAST_NAME"),
                    rs.getString("PHONE"),
                    rs.getString("ADDRESS"),
                    rs.getString("IS_ENABLED"),
                    rs.getTimestamp("CREATE_DT"),
                    rs.getTimestamp("MOD_DT")));
        }
        return out;
    }

    private static void displayResults(List<Account> accounts) {
        System.out.println();
        System.out.println("ACCOUNTS:");
        System.out.println();
        System.out.println(" ID   | First    | Last     | Phone      "
                + "| Address                | Enabled ");
        System.out.println("------|----------|----------|------------"
                + "|------------------------|---------");
        for (Account a : accounts) {
            System.out.printf("%05d | %-8s | %-8s | %-10s | %-22s | %s%n",
                    a.id,
                    truncOrPad(a.firstName, 8),
                    truncOrPad(a.lastName, 8),
                    truncOrPad(a.phone, 10),
                    truncOrPad(a.address, 22),
                    a.isEnabled);
        }
    }

    private static String truncOrPad(String value, int width) {
        String v = value == null ? "" : value;
        if (v.length() > width) {
            return v.substring(0, width);
        }
        return v;
    }

    /** Account row mirroring the COBOL ws-sql-account-record group. */
    public static final class Account {
        final int id;
        final String firstName;
        final String lastName;
        final String phone;
        final String address;
        final String isEnabled;
        final Timestamp createDt;
        final Timestamp modDt;

        public Account(int id, String firstName, String lastName,
                       String phone, String address, String isEnabled,
                       Timestamp createDt, Timestamp modDt) {
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
}
