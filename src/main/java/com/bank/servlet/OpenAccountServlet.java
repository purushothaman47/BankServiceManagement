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

public class OpenAccountServlet extends HttpServlet {

    private static final Logger LOG =
            LoggerFactory.getLogger(OpenAccountServlet.class);

    private static final long serialVersionUID = 1L;

    private static final int HTTP_BAD_REQUEST = 400;
    private static final int HTTP_INTERNAL_ERROR = 500;
    private static final String RESPONSE_WRITE_FAILED = "Response write failed";

    private final AccountService service = new AccountService();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {

        resp.setContentType("application/json");

        try {
            JsonNode json = mapper.readTree(req.getInputStream());

            String name = json.get("name").asText();
            double balance = json.get("balance").asDouble();

            service.openAccount(name, balance);

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(
                    "{\"message\":\"Account opened successfully\"}"
            );

        } catch (IOException e) {
            LOG.error("Invalid JSON input", e);
            try {
                resp.setStatus(HTTP_BAD_REQUEST);
                resp.getWriter().write("{\"message\":\"Invalid JSON input\"}");
            } catch (IOException ex) {
                LOG.error(RESPONSE_WRITE_FAILED, ex);
            }

        } catch (Exception e) {
            LOG.error("Open account failed", e);
            try {
                resp.setStatus(HTTP_INTERNAL_ERROR);
                resp.getWriter().write("{\"message\":\"Failed to open account\"}");
            } catch (IOException ex) {
                LOG.error(RESPONSE_WRITE_FAILED, ex);
            }
        }
    }
}
