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
 * Java migration of sql/sql_example.cbl
 *
 * COBOL-to-Java mapping:
 * - Embedded SQL (ESQL) with ODBC driver  -> JDBC with PostgreSQL driver
 * - EXEC SQL CONNECT                      -> DriverManager.getConnection()
 * - DECLARE CURSOR / OPEN / FETCH / CLOSE -> PreparedStatement + ResultSet
 * - WORKING-STORAGE SECTION variables      -> Java fields and records
 * - ACCEPT / DISPLAY                       -> Scanner / System.out
 * - check-sql-state paragraph              -> try-catch with SQLException
 * - ws-account-record table (OCCURS)       -> List<Account>
 *
 * The original COBOL program connects to a PostgreSQL database via unixODBC,
 * declares cursors for three query types (all accounts, disabled accounts,
 * search by name/phone/address), and presents a menu-driven interface.
 *
 * Prerequisites: PostgreSQL database with create_test_db.sql script executed.
 */
public class SqlExample {

    // ---------------------------------------------------------------
    // Connection parameters (COBOL: ws-db-connection-string)
    // Replace values as needed for your own local test environment.
    // ---------------------------------------------------------------
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/cobol_db_example";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "password";

    // ---------------------------------------------------------------
    // SQL queries (COBOL: DECLARE CURSOR statements)
    // ---------------------------------------------------------------

    /** COBOL: ACCOUNT-ALL-CUR — selects every row from ACCOUNTS */
    private static final String SQL_ALL_ACCOUNTS =
            "SELECT id, first_name, last_name, phone, address, is_enabled, create_dt, mod_dt "
          + "FROM accounts ORDER BY id";

    /** COBOL: ACCOUNT-DISABLED-CUR — selects rows where IS_ENABLED = 'N' */
    private static final String SQL_DISABLED_ACCOUNTS =
            "SELECT id, first_name, last_name, phone, address, is_enabled, create_dt, mod_dt "
          + "FROM accounts WHERE is_enabled = 'N' ORDER BY id";

    /**
     * COBOL: ACCOUNT-QUERY-CUR — searches FIRST_NAME, LAST_NAME, PHONE,
     * and ADDRESS using LIKE with wildcards (%).
     * Uses PreparedStatement parameter binding instead of COBOL host variables.
     */
    private static final String SQL_SEARCH_ACCOUNTS =
            "SELECT id, first_name, last_name, phone, address, is_enabled, create_dt, mod_dt "
          + "FROM accounts "
          + "WHERE first_name LIKE ? OR last_name LIKE ? OR phone LIKE ? OR address LIKE ? "
          + "ORDER BY id";

    // ---------------------------------------------------------------
    // Account record (COBOL: ws-account-record / ws-sql-account-record)
    //
    // Field widths mirror the COBOL PIC clauses:
    //   id           PIC 9(5)
    //   firstName    PIC X(8)
    //   lastName     PIC X(8)
    //   phone        PIC X(10)
    //   address      PIC X(22)
    //   isEnabled    PIC X       ("Y" or "N")
    //   createDt     PIC X(20)
    //   modDt        PIC X(20)
    // ---------------------------------------------------------------
    record Account(
            int id,
            String firstName,
            String lastName,
            String phone,
            String address,
            String isEnabled,
            Timestamp createDt,
            Timestamp modDt
    ) {}

    // ---------------------------------------------------------------
    // Main entry point (COBOL: main-procedure)
    // ---------------------------------------------------------------
    public static void main(String[] args) {
        System.out.println();
        System.out.println("COBOL SQL DB Example Program (Java Migration)");
        System.out.println("----------------------------------------------");
        System.out.println();

        // COBOL: EXEC SQL CONNECT TO :ws-db-connection-string END-EXEC
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Connected to database.");

            // COBOL: main menu loop (PERFORM FOREVER ... END-PERFORM)
            boolean running = true;
            while (running) {
                System.out.println();
                System.out.println("1) Display all accounts");
                System.out.println("2) Display disabled accounts");
                System.out.println("3) Query accounts");
                System.out.println("4) Exit");
                System.out.print("Selection: ");

                String choice = scanner.nextLine().trim();

                // COBOL: EVALUATE ws-menu-choice
                switch (choice) {
                    case "1" -> displayAllAccounts(connection);
                    case "2" -> displayDisabledAccounts(connection);
                    case "3" -> queryAccounts(connection, scanner);
                    case "4" -> running = false;
                    default  -> System.out.println("Please make a selection between 1-4");
                }
            }

            // COBOL: EXEC SQL CONNECT RESET END-EXEC
            System.out.println("Disconnected.");
            System.out.println();

        } catch (SQLException e) {
            // COBOL: check-sql-state paragraph (error path)
            handleSqlError(e);
        }
    }

    // ---------------------------------------------------------------
    // Display all accounts (COBOL: display-all-accounts paragraph)
    //
    // COBOL opens ACCOUNT-ALL-CUR, fetches rows into
    // ws-account-record table, then calls display-account-results.
    // Java equivalent uses a simple PreparedStatement + ResultSet.
    // ---------------------------------------------------------------
    private static void displayAllAccounts(Connection connection) throws SQLException {
        List<Account> accounts = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(SQL_ALL_ACCOUNTS);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                accounts.add(mapRow(rs));
            }
        }

        displayAccountResults(accounts);
    }

    // ---------------------------------------------------------------
    // Display disabled accounts (COBOL: display-disabled-accounts paragraph)
    //
    // COBOL opens ACCOUNT-DISABLED-CUR with WHERE IS_ENABLED = 'N'.
    // ---------------------------------------------------------------
    private static void displayDisabledAccounts(Connection connection) throws SQLException {
        List<Account> accounts = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(SQL_DISABLED_ACCOUNTS);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                accounts.add(mapRow(rs));
            }
        }

        displayAccountResults(accounts);
    }

    // ---------------------------------------------------------------
    // Query accounts by search term (COBOL: query-accounts paragraph)
    //
    // COBOL trims user input, wraps with '%' wildcards, computes
    // stored-char-length, and opens ACCOUNT-QUERY-CUR.
    // Java uses PreparedStatement parameter binding which handles
    // length and escaping automatically.
    //
    // The "search again?" loop mirrors:
    //   PERFORM UNTIL NOT ws-search-again
    // ---------------------------------------------------------------
    private static void queryAccounts(Connection connection, Scanner scanner) throws SQLException {
        boolean searchAgain = true;

        while (searchAgain) {
            System.out.println();
            System.out.print("Enter search value: ");
            String searchString = scanner.nextLine().trim();

            // COBOL: STRING '%' FUNCTION TRIM(ws-search-string) '%'
            //        INTO ws-search-value-text
            String searchValue = "%" + searchString + "%";

            System.out.println("Search value: " + searchValue);
            System.out.println("Length: " + searchValue.length());

            List<Account> accounts = new ArrayList<>();

            // COBOL: OPEN ACCOUNT-QUERY-CUR / FETCH ... INTO / CLOSE
            try (PreparedStatement stmt = connection.prepareStatement(SQL_SEARCH_ACCOUNTS)) {
                stmt.setString(1, searchValue);
                stmt.setString(2, searchValue);
                stmt.setString(3, searchValue);
                stmt.setString(4, searchValue);

                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        accounts.add(mapRow(rs));
                    }
                }
            }

            displayAccountResults(accounts);

            // COBOL: ACCEPT ws-search-again-sw / FUNCTION UPPER-CASE
            System.out.println();
            System.out.print("Search again? (Y/[N]) ");
            String again = scanner.nextLine().trim().toUpperCase();
            searchAgain = "Y".equals(again);
        }
    }

    // ---------------------------------------------------------------
    // Map a ResultSet row to an Account record
    // (COBOL: FETCH ... INTO :ws-sql-account-* host variables,
    //  then MOVE ws-sql-account-record TO ws-account-record)
    // ---------------------------------------------------------------
    private static Account mapRow(ResultSet rs) throws SQLException {
        return new Account(
                rs.getInt("id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("phone"),
                rs.getString("address"),
                rs.getString("is_enabled"),
                rs.getTimestamp("create_dt"),
                rs.getTimestamp("mod_dt")
        );
    }

    // ---------------------------------------------------------------
    // Display account results in table format
    // (COBOL: display-account-results paragraph)
    //
    // Column widths match COBOL PIC sizes:
    //   ID: 5  |  First: 8  |  Last: 8  |  Phone: 10  |  Address: 22  |  Enabled: 1
    //
    // The COBOL DISPLAY concatenates fixed-width fields separated by
    // " | " delimiters. Java uses String.format with %-Ns for
    // left-aligned, fixed-width columns.
    // ---------------------------------------------------------------
    private static void displayAccountResults(List<Account> accounts) {
        System.out.println();
        System.out.println("ACCOUNTS:");
        System.out.println();

        // COBOL: DISPLAY " ID   | First    | Last     | Phone      | Address                | Enabled "
        System.out.println(
                " ID   | First    | Last     | Phone      | Address                | Enabled ");

        // COBOL: DISPLAY "------|----------|----------|------------|------------------------|---------"
        System.out.println(
                "------|----------|----------|------------|------------------------|---------");

        // COBOL: PERFORM VARYING ws-account-idx FROM 1 BY 1
        //        UNTIL ws-account-idx > ws-num-accounts
        for (Account account : accounts) {
            // COBOL field widths: ID=5, First=8, Last=8, Phone=10, Address=22, Enabled=1
            // COBOL displays each field at its PIC width followed by " | "
            System.out.printf("%-5d | %-8s | %-8s | %-10s | %-22s | %s%n",
                    account.id(),
                    truncate(account.firstName(), 8),
                    truncate(account.lastName(), 8),
                    truncate(account.phone(), 10),
                    truncate(account.address(), 22),
                    truncate(account.isEnabled(), 1));
        }
    }

    // ---------------------------------------------------------------
    // Truncate a string to a maximum length.
    // COBOL PIC X(n) fields are fixed-width; values longer than n
    // are truncated. This helper mirrors that behavior for display.
    // ---------------------------------------------------------------
    private static String truncate(String value, int maxLength) {
        if (value == null) {
            return "";
        }
        return value.length() > maxLength ? value.substring(0, maxLength) : value;
    }

    // ---------------------------------------------------------------
    // SQL error handler (COBOL: check-sql-state paragraph)
    //
    // COBOL checks SQLSTATE: if SQL-SUCCESS or SQL-NODATA, returns.
    // Otherwise displays SQLCODE, SQLSTATE, SQLERRMC, disconnects
    // (if connected), and terminates with STOP RUN.
    //
    // In Java, SQLExceptions are caught and handled here. The
    // try-with-resources in main() ensures the connection is closed.
    // ---------------------------------------------------------------
    private static void handleSqlError(SQLException e) {
        System.out.println();
        System.out.println("SQL Error:");
        System.out.println("SQLSTATE: " + e.getSQLState());
        System.out.println("Error Code: " + e.getErrorCode());
        System.out.println("ERROR MESSAGE: " + e.getMessage());
        System.out.println();
    }
}
