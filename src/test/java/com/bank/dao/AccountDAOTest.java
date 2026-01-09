package com.bank.dao;

import com.bank.model.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

class AccountDAOTest {

    private AccountDAO accountDAO;

    @BeforeEach
    void setup() throws Exception {

        accountDAO = new AccountDAO();

        try (Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/masterdb",
                "root",
                "1234@Dpp")) {

            Statement stmt = con.createStatement();
            stmt.execute("TRUNCATE TABLE accounts");
        }
    }

    @Test
    void testCreateAccount() {

        Account acc = new Account();
        acc.setName("Ravi");
        acc.setBalance(5000);

        accountDAO.createAccount(acc);

        Account saved = accountDAO.findById(1);

        assertNotNull(saved);
        assertEquals("Ravi", saved.getName());
        assertEquals(5000, saved.getBalance());
    }

    @Test
    void testFindById() {

        Account acc = new Account();
        acc.setName("Kumar");
        acc.setBalance(3000);
        accountDAO.createAccount(acc);

        Account found = accountDAO.findById(1);

        assertNotNull(found);
        assertEquals("Kumar", found.getName());
    }

    @Test
    void testUpdateBalance() {

        Account acc = new Account();
        acc.setName("Suresh");
        acc.setBalance(2000);
        accountDAO.createAccount(acc);

        accountDAO.updateBalance(1, 9000);

        Account updated = accountDAO.findById(1);

        assertNotNull(updated);
        assertEquals(9000, updated.getBalance());
    }

    @Test
    void testFindById_invalid() {
        Account acc = accountDAO.findById(99);
        assertNull(acc);
    }
}
