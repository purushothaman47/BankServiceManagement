package com.bank.dao;

import com.bank.config.DBConfig;
import com.bank.exception.DataException;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class TransactionDAO {

    static final int INSERT_ACCOUNT_ID = 1;
    static final int INSERT_TYPE = 2;
    static final int INSERT_AMOUNT = 3;

    static final String SAVE_TRANSACTION =
            "insert into transactions(account_id, type, amount) values (?, ?, ?)";

    public void saveTransaction(int accountId, String type, double amount) {

        if (accountId <= 0) {
            throw new DataException("Invalid account id");
        }
        if (type == null || type.isBlank()) {
            throw new DataException("Transaction type cannot be null or empty");
        }
        if (amount < 0) {
            throw new DataException("Amount cannot be negative");
        }

        try (Connection con = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(SAVE_TRANSACTION)) {

            ps.setInt(INSERT_ACCOUNT_ID, accountId);
            ps.setString(INSERT_TYPE, type);
            ps.setDouble(INSERT_AMOUNT, amount);
            ps.executeUpdate();

        } catch (Exception e) {
            throw new DataException("Failed to save transaction", e);
        }
    }
}
