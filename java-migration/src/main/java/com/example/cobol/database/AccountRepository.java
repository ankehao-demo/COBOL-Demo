package com.example.cobol.database;

import com.example.cobol.model.Account;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AccountRepository {
    private final Connection connection;

    public AccountRepository(Connection connection) {
        this.connection = connection;
    }

    public List<Account> findAll() throws SQLException {
        String sql = "SELECT ID, FIRST_NAME, LAST_NAME, PHONE, " +
                     "ADDRESS, IS_ENABLED, CREATE_DT, MOD_DT " +
                     "FROM ACCOUNTS ORDER BY ID";
        
        List<Account> accounts = new ArrayList<>();
        
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                accounts.add(mapResultSetToAccount(rs));
            }
        }
        
        return accounts;
    }

    public List<Account> findDisabled() throws SQLException {
        String sql = "SELECT ID, FIRST_NAME, LAST_NAME, PHONE, " +
                     "ADDRESS, IS_ENABLED, CREATE_DT, MOD_DT " +
                     "FROM ACCOUNTS WHERE IS_ENABLED = 'N' ORDER BY ID";
        
        List<Account> accounts = new ArrayList<>();
        
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                accounts.add(mapResultSetToAccount(rs));
            }
        }
        
        return accounts;
    }

    public List<Account> search(String searchValue) throws SQLException {
        String searchPattern = "%" + searchValue.trim() + "%";
        String sql = "SELECT ID, FIRST_NAME, LAST_NAME, PHONE, " +
                     "ADDRESS, IS_ENABLED, CREATE_DT, MOD_DT " +
                     "FROM ACCOUNTS WHERE " +
                     "FIRST_NAME LIKE ? OR LAST_NAME LIKE ? OR " +
                     "PHONE LIKE ? OR ADDRESS LIKE ? ORDER BY ID";
        
        List<Account> accounts = new ArrayList<>();
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            for (int i = 1; i <= 4; i++) {
                stmt.setString(i, searchPattern);
            }
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    accounts.add(mapResultSetToAccount(rs));
                }
            }
        }
        
        return accounts;
    }

    private Account mapResultSetToAccount(ResultSet rs) throws SQLException {
        int id = rs.getInt("ID");
        String firstName = rs.getString("FIRST_NAME");
        String lastName = rs.getString("LAST_NAME");
        String phone = rs.getString("PHONE");
        String address = rs.getString("ADDRESS");
        String isEnabled = rs.getString("IS_ENABLED");
        
        Timestamp createTs = rs.getTimestamp("CREATE_DT");
        LocalDateTime createDt = createTs != null ? createTs.toLocalDateTime() : null;
        
        Timestamp modTs = rs.getTimestamp("MOD_DT");
        LocalDateTime modDt = modTs != null ? modTs.toLocalDateTime() : null;
        
        return new Account(id, firstName, lastName, phone, address, isEnabled, createDt, modDt);
    }
}
