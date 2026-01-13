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

    private static final Logger LOG =
            LoggerFactory.getLogger(DepositServlet.class);

    private static final long serialVersionUID = 1L;

    private static final int HTTP_BAD_REQUEST = 400;
    private static final int HTTP_INTERNAL_ERROR = 500;
    private static final String RESPONSE_WRITE_FAILED = "Response write failed";

    private final AccountService service = new AccountService();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {

        LOG.info("Deposit API called");
        resp.setContentType("application/json");

        try {
            JsonNode json = mapper.readTree(req.getInputStream());

            int accountId = json.get("accountId").asInt();
            double amount = json.get("amount").asDouble();

            double newBalance = service.deposit(accountId, amount);

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(
                    "{\"message\":\"Deposit successful\",\"balance\":" + newBalance + "}"
            );

            LOG.info("Deposit API success ");

        } catch (IOException e) {
            LOG.error("Invalid request payload", e);
            try {
                resp.setStatus(HTTP_BAD_REQUEST);
                resp.getWriter().write("{\"message\":\"Invalid JSON input\"}");
            } catch (IOException ex) {
                LOG.error(RESPONSE_WRITE_FAILED, ex);
            }

        } catch (Exception e) {
            LOG.error("Deposit failed", e);
            try {
                resp.setStatus(HTTP_INTERNAL_ERROR);
                resp.getWriter().write("{\"message\":\"Deposit failed\"}");
            } catch (IOException ex) {
                LOG.error(RESPONSE_WRITE_FAILED, ex);
            }
        }
    }
}
