package com.example.cobol.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {
    private static final String DEFAULT_URL = "jdbc:postgresql://localhost:5432/cobol_db_example";
    private static final String DEFAULT_USER = "postgres";
    private static final String DEFAULT_PASSWORD = "password";

    public static Connection connect() throws SQLException {
        return connect(DEFAULT_URL, DEFAULT_USER, DEFAULT_PASSWORD);
    }

    public static Connection connect(String url, String user, String password) throws SQLException {
        Properties props = new Properties();
        props.setProperty("user", user);
        props.setProperty("password", password);
        
        try {
            Connection conn = DriverManager.getConnection(url, props);
            System.out.println("Connected to database successfully.");
            return conn;
        } catch (SQLException e) {
            System.err.println("SQL Error:");
            System.err.println("SQLState: " + e.getSQLState());
            System.err.println("Error Code: " + e.getErrorCode());
            System.err.println("Message: " + e.getMessage());
            throw e;
        }
    }

    public static void disconnect(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
                System.out.println("Disconnected.");
            } catch (SQLException e) {
                System.err.println("Error disconnecting: " + e.getMessage());
            }
        }
    }

    public static void handleSQLException(SQLException e) {
        System.err.println();
        System.err.println("SQL Error:");
        System.err.println("SQLCODE: " + e.getErrorCode());
        System.err.println("SQLSTATE: " + e.getSQLState());
        
        if (e.getMessage() != null && !e.getMessage().isEmpty()) {
            System.err.println("ERROR MESSAGE: " + e.getMessage());
        }
        System.err.println();
    }
}
