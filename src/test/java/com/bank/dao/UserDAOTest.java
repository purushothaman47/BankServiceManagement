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
    void shouldSaveAndFindUser() {
        userDAO.save("machi", "hashed");

        User user = userDAO.findByUsername("machi");

        assertNotNull(user);
        assertEquals("machi", user.getUsername());
        assertEquals("hashed", user.getPassword());
    }

    @Test
    void shouldReturnNullWhenUserNotFound() {
        User user = userDAO.findByUsername("unknown");
        assertNull(user);
    }

    @Test
    void shouldFailForDuplicateUsername() {
        userDAO.save("dup", "p1");

        assertThrows(DataException.class, () ->
                userDAO.save("dup", "p2")
        );
    }

    @Test
    void shouldFailWhenUsernameIsNull() {
        assertThrows(DataException.class, () ->
                userDAO.save(null, "pwd")
        );
    }
}
