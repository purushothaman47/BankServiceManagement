package com.bank.service;

import com.bank.dao.UserDAO;
import com.bank.model.User;
import com.bank.util.PasswordUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthService {

    private static final Logger LOG =
            LoggerFactory.getLogger(AuthService.class);

    private final UserDAO userDAO = new UserDAO();
    public boolean register(String username, String password) {
        LOG.info("Register request");

        if (userDAO.findByUsername(username) != null) {
            return false;
        }

        String hashed = PasswordUtil.hash(password);
        userDAO.save(username, hashed);

        LOG.info("User registered successfully");

        return true;
    }

    public User login(String username, String rawPassword) {
        LOG.info("Login attempt");

        User user = userDAO.findByUsername(username);
        if (user == null) {
            return null;
        }

        String hashedInput = PasswordUtil.hash(rawPassword);

        if (!hashedInput.equals(user.getPassword())) {
            return null;
        }
        LOG.info("Login Successfully");

        return user;
    }
}
