package com.bank.dao;

import com.bank.config.DBConfig;
import com.bank.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO {

    public User findByUsername(String username) {

        String sql = "select * from users where username = ?";

        try (Connection con = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);
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
            throw new RuntimeException("Error fetching user", e);
        }
    }

    public void save(String username, String hashedPassword) {

        String sql = "insert into users(username, password) values (?, ?)";

        try (Connection con = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, hashedPassword);
            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException("Error saving user", e);
        }
    }
}
