package com.bank.dao;

import com.bank.exception.DataException;
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
                "1234@Dpp#")) {

            Statement stmt = con.createStatement();
            stmt.execute("TRUNCATE TABLE accounts");
        }
    }

    @Test
    void testCreateAccount() {
        Account acc = new Account();
        acc.setName("Puru");
        acc.setBalance(5000);

        accountDAO.createAccount(acc);

        Account saved = accountDAO.findById(1);
        assertNotNull(saved, "Saved account should not be null");
        assertEquals("Puru", saved.getName(), "Account name should match");
        assertEquals(5000, saved.getBalance(), "Account balance should match");
    }

    @Test
    void testUpdateBalance() {
        Account acc = new Account();
        acc.setName("Puru");
        acc.setBalance(1000);
        accountDAO.createAccount(acc);

        accountDAO.updateBalance(1, 9000);

        Account updated = accountDAO.findById(1);
        assertNotNull(updated, "Updated account should not be null");
        assertEquals(9000, updated.getBalance(), "Balance should be updated");
    }

    @Test
    void testFindByIdInvalid() {
        Account acc = accountDAO.findById(99);
        assertNull(acc, "Account should be null for invalid id");
    }

    @Test
    void testCreateAccountWithNullName() {
        Account acc = new Account();
        acc.setName(null);
        acc.setBalance(100);

        assertThrows(
                DataException.class,
                () -> accountDAO.createAccount(acc),
                "Creating account with null name should throw DataException"
        );
    }
}
