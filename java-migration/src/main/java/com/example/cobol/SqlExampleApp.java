package com.example.cobol;

import com.example.cobol.database.AccountRepository;
import com.example.cobol.database.DatabaseConnection;
import com.example.cobol.model.Account;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class SqlExampleApp {
    private Connection connection;
    private AccountRepository accountRepository;
    private Scanner scanner;

    public SqlExampleApp() {
        this.scanner = new Scanner(System.in);
    }

    public static void main(String[] args) {
        SqlExampleApp app = new SqlExampleApp();
        app.run();
    }

    public void run() {
        System.out.println();
        System.out.println("COBOL SQL DB Example Program");
        System.out.println("----------------------------");
        System.out.println();

        try {
            connection = DatabaseConnection.connect();
            accountRepository = new AccountRepository(connection);

            runMainMenu();

        } catch (SQLException e) {
            DatabaseConnection.handleSQLException(e);
            System.exit(1);
        } finally {
            DatabaseConnection.disconnect(connection);
            scanner.close();
        }

        System.out.println();
    }

    private void runMainMenu() {
        boolean running = true;

        while (running) {
            System.out.println();
            System.out.println("1) Display all accounts");
            System.out.println("2) Display disabled accounts");
            System.out.println("3) Query accounts");
            System.out.println("4) Exit");
            System.out.print("Selection: ");

            String choice = scanner.nextLine();

            try {
                switch (choice) {
                    case "1":
                        displayAllAccounts();
                        break;

                    case "2":
                        displayDisabledAccounts();
                        break;

                    case "3":
                        queryAccounts();
                        break;

                    case "4":
                        running = false;
                        break;

                    default:
                        System.out.println("Please make a selection between 1-4");
                }
            } catch (SQLException e) {
                DatabaseConnection.handleSQLException(e);
                running = false;
            }
        }
    }

    private void displayAllAccounts() throws SQLException {
        List<Account> accounts = accountRepository.findAll();
        displayAccountResults(accounts);
    }

    private void displayDisabledAccounts() throws SQLException {
        List<Account> accounts = accountRepository.findDisabled();
        displayAccountResults(accounts);
    }

    private void queryAccounts() throws SQLException {
        boolean searchAgain = true;

        while (searchAgain) {
            System.out.println();
            System.out.print("Enter search value: ");
            String searchString = scanner.nextLine();

            String searchValue = searchString.trim();
            System.out.println("Search value: %" + searchValue + "%");
            System.out.println("Length: " + searchValue.length());

            List<Account> accounts = accountRepository.search(searchValue);
            displayAccountResults(accounts);

            System.out.println();
            System.out.print("Search again? (Y/[N]) ");
            String response = scanner.nextLine();

            searchAgain = response.trim().toUpperCase().equals("Y");
        }
    }

    private void displayAccountResults(List<Account> accounts) {
        System.out.println();
        System.out.println("ACCOUNTS:");
        System.out.println();
        System.out.println(" ID   | First    | Last     | Phone      | Address                | Enabled ");
        System.out.println("------|----------|----------|------------|------------------------|--------");

        for (Account account : accounts) {
            System.out.println(account.toString());
        }
    }
}
