package com.bank.dao;

import com.bank.model.User;
import com.bank.exception.DataException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

class UserDAOTest {

    private UserDAO userDAO;

    @BeforeEach
    void setup() throws Exception {
        userDAO = new UserDAO();

        try (Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/masterdb",
                "root",
                "1234@Dpp#")) {

            Statement stmt = con.createStatement();
            stmt.execute("TRUNCATE TABLE users");
        }
    }

    @Test
    void testSaveAndFindUser() {
        userDAO.save("machi", "hashed");

        User user = userDAO.findByUsername("machi");

        assertNotNull(user, "User should not be null after save");
        assertEquals("machi", user.getUsername(), "Username should match");
        assertEquals("hashed", user.getPassword(), "Password should match");
    }

    @Test
    void testReturnNullWhenUserNotFound() {
        User user = userDAO.findByUsername("unknown");
        assertNull(user, "Unknown user should return null");
    }

    @Test
    void testFailForDuplicateUsername() {
        userDAO.save("dup", "p1");

        assertThrows(
                DataException.class,
                () -> userDAO.save("dup", "p2"),
                "Duplicate username should throw DataException"
        );
    }

    @Test
    void testFailWhenUsernameIsNull() {
        assertThrows(
                DataException.class,
                () -> userDAO.save(null, "pwd"),
                "Null username should throw DataException"
        );
    }
}
