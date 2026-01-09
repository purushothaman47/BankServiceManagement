package com.bank.dao;

import com.bank.config.DBConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class TransactionDAO {

    public void saveTransaction(int accountId, String type, double amount) {

        String sql = "insert into transactions(account_id, type, amount) values (?, ?, ?)";

        try (Connection con = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, accountId);
            ps.setString(2, type);
            ps.setDouble(3, amount);
            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
