package com.bank.servlet;

import com.bank.service.AccountService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DepositServlet extends HttpServlet {

    private static final Logger log =
            LoggerFactory.getLogger(DepositServlet.class);

    private final AccountService service = new AccountService();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        log.info("Deposit API called");

        JsonNode json = mapper.readTree(req.getInputStream());

        int accountId = json.get("accountId").asInt();
        double amount = json.get("amount").asDouble();

        service.deposit(accountId, amount);

        resp.setStatus(HttpServletResponse.SC_OK);

        resp.setContentType("application/json");

        resp.getWriter().write("{\"message\":\"Deposit successful\"}");

        log.info("Deposit API success");
    }
}
