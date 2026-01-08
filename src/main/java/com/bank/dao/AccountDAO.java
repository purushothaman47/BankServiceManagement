package com.bank.dao;

import com.bank.config.DBConfig;
import com.bank.model.Account;

import java.sql.*;

public class AccountDAO {

    public void createAccount(Account account) {
        String sql = "insert into accounts(name, balance) values (?, ?)";

        try (Connection con = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, account.getName());
            ps.setDouble(2, account.getBalance());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Account findById(int id) {
        String sql = "select * from accounts where id=?";

        try (Connection con = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Account acc = new Account();
                acc.setId(rs.getInt("id"));
                acc.setName(rs.getString("name"));
                acc.setBalance(rs.getDouble("balance"));
                return acc;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public void updateBalance(int id, double balance) {
        String sql = "update accounts set balance=? where id=?";

        try (Connection con = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDouble(1, balance);
            ps.setInt(2, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
