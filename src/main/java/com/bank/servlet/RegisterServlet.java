package com.bank.servlet;

import com.bank.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class RegisterServlet extends HttpServlet {

    private final AuthService authService = new AuthService();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            Map<String, String> body =
                    mapper.readValue(req.getInputStream(), Map.class);

            boolean ok = authService.register(
                    body.get("username"),
                    body.get("password")
            );

            resp.setContentType("application/json");

            if (!ok) {
                resp.getWriter().write("{\"msg\":\"Username already exists\"}");
                return;
            }

            resp.getWriter().write("{\"msg\":\"Registered successfully\"}");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
