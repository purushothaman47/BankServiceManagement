package com.bank.servlet;

import com.bank.model.User;
import com.bank.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class LoginServlet extends HttpServlet {

    private static final Logger LOG =
            LoggerFactory.getLogger(LoginServlet.class);

    private static final long serialVersionUID = 1L;

    private static final int HTTP_UNAUTHORIZED = 401;
    private static final int HTTP_BAD_REQUEST = 400;
    private static final int HTTP_INTERNAL_ERROR = 500;
    private static final String RESPONSE_WRITE_FAILED = "Response write failed";

    private final AuthService authService = new AuthService();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {

        resp.setContentType("application/json");

        try {
            Map<String, String> body =
                    mapper.readValue(req.getInputStream(), Map.class);

            User user = authService.login(
                    body.get("username"),
                    body.get("password")
            );

            if (user == null) {
                resp.setStatus(HTTP_UNAUTHORIZED);
                resp.getWriter().write("{\"message\":\"Invalid Credentials\"}");
                return;
            }

            req.getSession(true).setAttribute("user", user);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("{\"message\":\"Login Successful\"}");

        } catch (IOException e) {
            LOG.error("Invalid JSON input", e);
            try {
                resp.setStatus(HTTP_BAD_REQUEST);
                resp.getWriter().write("{\"message\":\"Invalid JSON input\"}");
            } catch (IOException ex) {
                LOG.error(RESPONSE_WRITE_FAILED, ex);
            }

        } catch (Exception e) {
            LOG.error("Login failed", e);
            try {
                resp.setStatus(HTTP_INTERNAL_ERROR);
                resp.getWriter().write("{\"message\":\"Login failed\"}");
            } catch (IOException ex) {
                LOG.error(RESPONSE_WRITE_FAILED, ex);
            }
        }
    }
}
