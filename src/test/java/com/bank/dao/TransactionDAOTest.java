package com.bank.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class TransactionDAOTest {

    private TransactionDAO transactionDAO;

    @BeforeEach
    void setup() throws Exception {

        transactionDAO = new TransactionDAO();

        try (Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/masterdb",
                "root",
                "1234@Dpp")) {

            Statement stmt = con.createStatement();
            stmt.execute("TRUNCATE TABLE transactions");
        }
    }

    @Test
    void testSaveTransaction() {

        assertDoesNotThrow(() ->
                transactionDAO.saveTransaction(1, "DEPOSIT", 1000)
        );
    }

    @Test
    void testSaveTransactionInvalidAccountId() {

        assertDoesNotThrow(() ->
                transactionDAO.saveTransaction(-1, "WITHDRAW", 500)
        );
    }
}
