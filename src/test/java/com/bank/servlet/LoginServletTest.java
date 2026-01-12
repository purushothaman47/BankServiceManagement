package com.bank.servlet;

import com.bank.model.User;
import com.bank.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.*;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoginServletTest {
    private LoginServlet servlet;
    private AuthService authService;

    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;

    private StringWriter responseWriter;

    @BeforeEach
    void setup() throws Exception {

        servlet = new LoginServlet();
        authService = mock(AuthService.class);

        var field = LoginServlet.class.getDeclaredField("authService");
        field.setAccessible(true);
        field.set(servlet, authService);

        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);

        responseWriter = new StringWriter();
        when(response.getWriter())
                .thenReturn(new PrintWriter(responseWriter));
    }


    @Test
    void testLoginSuccess() throws Exception {

        String json = """
                {"username":"Purushothaman","password":"1234"}
                """;

        when(request.getInputStream())
                .thenReturn(mockInputStream(json));

        User user = new User();
        user.setUsername("Purushothaman");

        when(authService.login("Purushothaman", "1234"))
                .thenReturn(user);

        when(request.getSession(true))
                .thenReturn(session);

        servlet.doPost(request, response);

        verify(response).setContentType("application/json");
        verify(session).setAttribute("user", user);

        assertTrue(responseWriter.toString()
                .contains("Login Successful"));
    }


    @Test
    void testLoginInvalidCredentials() throws Exception {

        String json = """
                {"username":"1234","password":"1234"}
                """;

        when(request.getInputStream())
                .thenReturn(mockInputStream(json));

        when(authService.login("1234", "1234"))
                .thenReturn(null);

        servlet.doPost(request, response);

        verify(response).setStatus(401);

        assertTrue(responseWriter.toString()
                .contains("Invalid Credentials"));
    }

    private ServletInputStream mockInputStream(String body) {
        ByteArrayInputStream bis =
                new ByteArrayInputStream(body.getBytes());

        return new ServletInputStream() {
            @Override
            public int read() {
                return bis.read();
            }

            @Override
            public boolean isFinished() {
                return bis.available() == 0;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
            }
        };
    }
}
