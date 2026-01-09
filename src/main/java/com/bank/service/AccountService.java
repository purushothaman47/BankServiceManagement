package com.bank.service;

import com.bank.dao.AccountDAO;
import com.bank.dao.TransactionDAO;
import com.bank.exception.DataException;
import com.bank.model.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AccountService {

    private static final Logger LOG =
            LoggerFactory.getLogger(AccountService.class);

    private final AccountDAO accountDAO = new AccountDAO();
    private final TransactionDAO transactionDAO = new TransactionDAO();

    public void openAccount(String name, double balance) {

        LOG.info("Opening account");
        Account acc = new Account();
        acc.setName(name);
        acc.setBalance(balance);
        accountDAO.createAccount(acc);
        LOG.info("Account opened successfully");
    }

    public void deposit(int accountId, double amount) {
        LOG.info("Deposit request");
        Account acc = accountDAO.findById(accountId);
        if (acc == null)
            throw new DataException("Account not found");

        double newBalance = acc.getBalance() + amount;
        accountDAO.updateBalance(accountId, newBalance);
        transactionDAO.saveTransaction(accountId, "DEPOSIT", amount);
        LOG.info("Deposit success");
    }

    public void withdraw(int accountId, double amount) {
        LOG.info("Withdraw success");
        Account acc = accountDAO.findById(accountId);
        if (acc == null) throw new DataException("Account not found");

        if (acc.getBalance() < amount)
            throw new DataException("Insufficient Balance");

        double newBalance = acc.getBalance() - amount;
        accountDAO.updateBalance(accountId, newBalance);
        transactionDAO.saveTransaction(accountId, "WITHDRAW", amount);
        LOG.info("Withdraw success");

    }
}
