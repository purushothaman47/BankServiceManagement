package com.bank.dao;

import com.bank.exception.DataException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TransactionDAOTest {

    private TransactionDAO transactionDAO;

    @BeforeEach
    void setup() throws Exception {
        transactionDAO = new TransactionDAO();

        try (Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/masterdb",
                "root",
                "1234@Dpp#")) {

            Statement stmt = con.createStatement();
            stmt.execute("TRUNCATE TABLE transactions");
        }
    }

    @Test
    void testSaveDepositTransaction() {
        assertDoesNotThrow(() ->
                transactionDAO.saveTransaction(1, "DEPOSIT", 1000)
        );
    }

    @Test
    void testSaveWithdrawTransaction() {
        assertDoesNotThrow(() ->
                transactionDAO.saveTransaction(1, "WITHDRAW", 500)
        );
    }

    @Test
    void testFailWhenTypeIsNull() {
        assertThrows(
                DataException.class,
                () -> transactionDAO.saveTransaction(1, null, 100),
                "Null type should throw DataException"
        );
    }

    @Test
    void testFailWhenAmountIsNegative() {
        assertThrows(
                DataException.class,
                () -> transactionDAO.saveTransaction(1, "DEPOSIT", -10),
                "Negative amount should throw DataException"
        );
    }
}
