package com.bank.dao;

import com.bank.config.DBConfig;
import com.bank.exception.DataException;
import com.bank.model.Account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class AccountDAO {

    static final int INSERT_NAME=1;
    static final int INSERT_BALANCE=2;

    static final int USER_ID=1;

    static final int UPDATE_BALANCE=1;
    static final int UPDATE_ACCOuNT_ID=2;

    static final String INSERT_ACCOUNT_VALUE = "insert into accounts(name, balance) values (?, ?)";
    static final String FIND_BY_ID = "select * from accounts where id=?";
    static final String UPDATE_ACCOUNT_BALANCE="update accounts set balance=? where id=?";

    public void createAccount(Account account) {

      //  String sql = "insert into accounts(name, balance) values (?, ?)";

        try (Connection con = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(INSERT_ACCOUNT_VALUE)) {

            ps.setString(INSERT_NAME, account.getName());
            ps.setDouble(INSERT_BALANCE, account.getBalance());
            ps.executeUpdate();

        }
        catch (SQLException e)
        {
            throw new DataException("failed",e);
        }
    }

    public Account findById(int id) {
       // String sql = "select * from accounts where id=?";

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
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
        return null;
    }
    public void updateBalance(int id, double balance) {
       // String sql = "update accounts set balance=? where id=?";

        try (Connection con = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(UPDATE_ACCOUNT_BALANCE)) {

            ps.setDouble(UPDATE_BALANCE, balance);
            ps.setInt(UPDATE_ACCOuNT_ID, id);
            ps.executeUpdate();

        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }
}
