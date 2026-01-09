package com.bank.service;

import com.bank.dao.AccountDAO;
import com.bank.dao.TransactionDAO;
import com.bank.exception.DataException;
import com.bank.model.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccountServiceTest {

    private AccountService service;
    private AccountDAO accountDAO;
    private TransactionDAO transactionDAO;

    @BeforeEach
    void setup() throws Exception {

        service = new AccountService();

        accountDAO = mock(AccountDAO.class);
        transactionDAO = mock(TransactionDAO.class);

        Field accField =
                AccountService.class.getDeclaredField("accountDAO");
        accField.setAccessible(true);
        accField.set(service, accountDAO);

        Field txnField =
                AccountService.class.getDeclaredField("transactionDAO");
        txnField.setAccessible(true);
        txnField.set(service, transactionDAO);
    }

    @Test
    void openAccountSuccess() {

        assertDoesNotThrow(() ->
                service.openAccount("Ravi", 5000)
        );

        verify(accountDAO, times(1))
                .createAccount(any(Account.class));
    }


    @Test
    void depositSuccess() {

        Account acc = new Account();
        acc.setId(1);
        acc.setBalance(2000);

        when(accountDAO.findById(1)).thenReturn(acc);

        service.deposit(1, 1000);

        verify(accountDAO)
                .updateBalance(1, 3000);
        verify(transactionDAO)
                .saveTransaction(1, "DEPOSIT", 1000);
    }

    @Test
    void depositInvalidAccount() {

        when(accountDAO.findById(1)).thenReturn(null);

        assertThrows(InvalidAccountException.class, () ->
                service.deposit(1, 1000)
        );

        verify(transactionDAO, never())
                .saveTransaction(anyInt(), any(), anyDouble());
    }

    @Test
    void withdraw_success() {

        Account acc = new Account();
        acc.setId(1);
        acc.setBalance(3000);

        when(accountDAO.findById(1)).thenReturn(acc);

        service.withdraw(1, 1000);

        verify(accountDAO)
                .updateBalance(1, 2000);
        verify(transactionDAO)
                .saveTransaction(1, "WITHDRAW", 1000);
    }

    @Test
    void withdraw_insufficientBalance() {

        Account acc = new Account();
        acc.setId(1);
        acc.setBalance(500);

        when(accountDAO.findById(1)).thenReturn(acc);

        assertThrows(DataException.class, () ->
                service.withdraw(1, 1000)
        );

        verify(accountDAO, never())
                .updateBalance(anyInt(), anyDouble());
        verify(transactionDAO, never())
                .saveTransaction(anyInt(), any(), anyDouble());
    }

    @Test
    void withdraw_invalidAccount() {

        when(accountDAO.findById(1)).thenReturn(null);

        assertThrows(InvalidAccountException.class, () ->
                service.withdraw(1, 1000)
        );
    }
}
