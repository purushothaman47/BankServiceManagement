package com.bank.dao;

import com.bank.config.DBConfig;
import com.bank.exception.DataException;
import com.bank.model.Account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountDAO {

    static final int INSERT_NAME = 1;
    static final int INSERT_BALANCE = 2;

    static final int USER_ID = 1;

    static final int UPDATE_BALANCE = 1;
    static final int UPDATE_ACCOUNT_ID = 2;

    static final String INSERT_ACCOUNT_VALUE =
            "insert into accounts(name, balance) values (?, ?)";
    static final String FIND_BY_ID =
            "select * from accounts where id=?";
    static final String UPDATE_ACCOUNT_BALANCE =
            "update accounts set balance=? where id=?";

    public void createAccount(Account account) {

        if (account == null) {
            throw new DataException("Account cannot be null");
        }
        if (account.getName() == null || account.getName().isBlank()) {
            throw new DataException("Account name cannot be null or empty");
        }
        if (account.getBalance() < 0) {
            throw new DataException("Initial balance cannot be negative");
        }

        try (Connection con = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(INSERT_ACCOUNT_VALUE)) {

            ps.setString(INSERT_NAME, account.getName());
            ps.setDouble(INSERT_BALANCE, account.getBalance());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DataException("Failed to create account", e);
        }
    }

    public Account findById(int id) {

        if (id <= 0) {
            throw new DataException("Invalid account id");
        }

        try (Connection con = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(FIND_BY_ID)) {

            ps.setInt(USER_ID, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Account acc = new Account();
                acc.setId(rs.getInt("id"));
                acc.setName(rs.getString("name"));
                acc.setBalance(rs.getDouble("balance"));
                return acc;
            }

            return null;

        } catch (SQLException e) {
            throw new DataException("Error fetching account", e);
        }
    }

    public void updateBalance(int id, double balance) {

        if (id <= 0) {
            throw new DataException("Invalid account id");
        }
        if (balance < 0) {
            throw new DataException("Balance cannot be negative");
        }

        try (Connection con = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(UPDATE_ACCOUNT_BALANCE)) {

            ps.setDouble(UPDATE_BALANCE, balance);
            ps.setInt(UPDATE_ACCOUNT_ID, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DataException("Error updating balance", e);
        }
    }
}
