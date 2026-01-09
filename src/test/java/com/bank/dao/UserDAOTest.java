package com.bank.dao;

import com.bank.model.User;
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
                "1234@Dpp")) {

            Statement stmt = con.createStatement();
            stmt.execute("DELETE FROM users");
        }
    }


    @Test
    void testSaveUser() {

        userDAO.save("machi", "hashed_password");

        User user = userDAO.findByUsername("machi");

        assertNotNull(user);
        assertEquals("machi", user.getUsername());
        assertEquals("hashed_password", user.getPassword());
    }

    @Test
    void testFindByUsername() {

        userDAO.save("ravi", "pwd123");

        User user = userDAO.findByUsername("ravi");

        assertNotNull(user);
        assertEquals("ravi", user.getUsername());
    }


    @Test
    void testFindByUsername_NotFound() {

        User user = userDAO.findByUsername("unknown");

        assertNull(user);
    }

    @Test
    void testSaveDuplicateUsername() {

        userDAO.save("dupuser", "pwd1");

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                userDAO.save("dupuser", "pwd2")
        );
        assertTrue(ex.getMessage().contains("Error saving user"));
    }
}
