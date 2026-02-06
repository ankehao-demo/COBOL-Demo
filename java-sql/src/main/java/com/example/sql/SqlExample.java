package com.example.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SqlExample {

    private static final String DEFAULT_DB_URL = "jdbc:postgresql://localhost:5432/cobol_db_example";
    private static final String DEFAULT_DB_USER = "postgres";

    private static String getDbUrl() {
        String url = System.getenv("DB_URL");
        return url != null ? url : DEFAULT_DB_URL;
    }

    private static String getDbUser() {
        String user = System.getenv("DB_USER");
        return user != null ? user : DEFAULT_DB_USER;
    }

    private static String getDbPassword() {
        String password = System.getenv("DB_PASSWORD");
        if (password == null) {
            throw new IllegalStateException("DB_PASSWORD environment variable is required");
        }
        return password;
    }

    private static final String SELECT_ALL_ACCOUNTS =
        "SELECT ID, FIRST_NAME, LAST_NAME, PHONE, ADDRESS, IS_ENABLED, CREATE_DT, MOD_DT " +
        "FROM ACCOUNTS ORDER BY ID";

    private static final String SELECT_DISABLED_ACCOUNTS =
        "SELECT ID, FIRST_NAME, LAST_NAME, PHONE, ADDRESS, IS_ENABLED, CREATE_DT, MOD_DT " +
        "FROM ACCOUNTS WHERE IS_ENABLED = 'N' ORDER BY ID";

    private static final String SEARCH_ACCOUNTS =
        "SELECT ID, FIRST_NAME, LAST_NAME, PHONE, ADDRESS, IS_ENABLED, CREATE_DT, MOD_DT " +
        "FROM ACCOUNTS WHERE FIRST_NAME LIKE ? OR LAST_NAME LIKE ? OR PHONE LIKE ? OR ADDRESS LIKE ? " +
        "ORDER BY ID";

    private Connection connection;

    public static void main(String[] args) {
        SqlExample app = new SqlExample();
        app.run();
    }

    public void run() {
        System.out.println();
        System.out.println("Java SQL DB Example Program");
        System.out.println("----------------------------");
        System.out.println();

        try {
            connect();
            runMenu();
        } catch (SQLException e) {
            handleSqlError(e);
        } finally {
            disconnect();
        }
    }

    private void connect() throws SQLException {
        connection = DriverManager.getConnection(getDbUrl(), getDbUser(), getDbPassword());
        System.out.println("Connected to database.");
    }

    private void disconnect() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Disconnected.");
                System.out.println();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }

    private void runMenu() {
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
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
                        queryAccounts(scanner);
                        break;
                    case "4":
                        return;
                    default:
                        System.out.println("Please make a selection between 1-4");
                        break;
                }
            }
        }
    }

    private void displayAllAccounts() {
        try (PreparedStatement stmt = connection.prepareStatement(SELECT_ALL_ACCOUNTS);
             ResultSet rs = stmt.executeQuery()) {

            List<Account> accounts = fetchAccounts(rs);
            displayAccountResults(accounts);

        } catch (SQLException e) {
            handleSqlError(e);
        }
    }

    private void displayDisabledAccounts() {
        try (PreparedStatement stmt = connection.prepareStatement(SELECT_DISABLED_ACCOUNTS);
             ResultSet rs = stmt.executeQuery()) {

            List<Account> accounts = fetchAccounts(rs);
            displayAccountResults(accounts);

        } catch (SQLException e) {
            handleSqlError(e);
        }
    }

    private void queryAccounts(Scanner scanner) {
        boolean searchAgain = true;

        while (searchAgain) {
            System.out.println();
            System.out.print("Enter search value: ");
            String searchString = scanner.nextLine().trim();

            String searchValue = "%" + searchString + "%";
            System.out.println("Search value: " + searchValue);
            System.out.println("Length: " + searchValue.length());

            try (PreparedStatement stmt = connection.prepareStatement(SEARCH_ACCOUNTS)) {
                stmt.setString(1, searchValue);
                stmt.setString(2, searchValue);
                stmt.setString(3, searchValue);
                stmt.setString(4, searchValue);

                try (ResultSet rs = stmt.executeQuery()) {
                    List<Account> accounts = fetchAccounts(rs);
                    displayAccountResults(accounts);
                }

            } catch (SQLException e) {
                handleSqlError(e);
            }

            System.out.println();
            System.out.print("Search again? (Y/[N]) ");
            String response = scanner.nextLine().trim().toUpperCase();
            searchAgain = "Y".equals(response);
        }
    }

    private List<Account> fetchAccounts(ResultSet rs) throws SQLException {
        List<Account> accounts = new ArrayList<>();

        while (rs.next()) {
            Account account = new Account(
                rs.getInt("ID"),
                rs.getString("FIRST_NAME"),
                rs.getString("LAST_NAME"),
                rs.getString("PHONE"),
                rs.getString("ADDRESS"),
                rs.getString("IS_ENABLED"),
                rs.getObject("CREATE_DT", LocalDateTime.class),
                rs.getObject("MOD_DT", LocalDateTime.class)
            );
            accounts.add(account);
        }

        return accounts;
    }

    private void displayAccountResults(List<Account> accounts) {
        System.out.println();
        System.out.println("ACCOUNTS:");
        System.out.println();
        System.out.println(" ID   | First    | Last     | Phone      | Address                | Enabled ");
        System.out.println("------|----------|----------|------------|------------------------|---------");

        for (Account account : accounts) {
            System.out.printf("%5d | %-8s | %-8s | %-10s | %-22s | %s%n",
                account.getId(),
                truncate(account.getFirstName(), 8),
                truncate(account.getLastName(), 8),
                truncate(account.getPhone(), 10),
                truncate(account.getAddress(), 22),
                account.getIsEnabled()
            );
        }
    }

    private String truncate(String value, int maxLength) {
        if (value == null) {
            return "";
        }
        return value.length() <= maxLength ? value : value.substring(0, maxLength);
    }

    private void handleSqlError(SQLException e) {
        System.out.println();
        System.out.println("SQL Error:");
        System.out.println("SQLCODE: " + e.getErrorCode());
        System.out.println("SQLSTATE: " + e.getSQLState());
        if (e.getMessage() != null && !e.getMessage().isEmpty()) {
            System.out.println("ERROR MESSAGE: " + e.getMessage());
        }
        System.out.println();

        disconnect();
    }
}
