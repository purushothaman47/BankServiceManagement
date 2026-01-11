package com.bank.dao;

import com.bank.config.DBConfig;
import com.bank.exception.DataException;
import com.bank.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO {

    static final int USER_NAME = 1;
    static final int PASSWORD = 2;

    static final String FIND_BY_USER_NAME =
            "select * from users where username = ?";
    static final String SAVE_USER =
            "insert into users(username, password) values (?, ?)";

    public User findByUsername(String username) {

        if (username == null || username.isBlank()) {
            throw new DataException("Username cannot be null or empty");
        }

        try (Connection con = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(FIND_BY_USER_NAME)) {

            ps.setString(USER_NAME, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                return user;
            }

            return null;

        } catch (Exception e) {
            throw new DataException("Error fetching user", e);
        }
    }

    public void save(String username, String hashedPassword) {

        if (username == null || username.isBlank()) {
            throw new DataException("Username cannot be null or empty");
        }
        if (hashedPassword == null || hashedPassword.isBlank()) {
            throw new DataException("Password cannot be null or empty");
        }

        try (Connection con = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(SAVE_USER)) {

            ps.setString(USER_NAME, username);
            ps.setString(PASSWORD, hashedPassword);
            ps.executeUpdate();

        } catch (Exception e) {
            throw new DataException("Error saving user", e);
        }
    }
}
