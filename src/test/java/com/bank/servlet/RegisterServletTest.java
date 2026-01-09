package com.bank.servlet;

import com.bank.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.*;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class RegisterServletTest {

    private RegisterServlet servlet;
    private AuthService authService;

    private HttpServletRequest request;
    private HttpServletResponse response;

    private StringWriter responseWriter;

    @BeforeEach
    void setup() throws Exception {

        servlet = new RegisterServlet();
        authService = mock(AuthService.class);

        var field = RegisterServlet.class.getDeclaredField("authService");
        field.setAccessible(true);
        field.set(servlet, authService);

        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);

        responseWriter = new StringWriter();
        when(response.getWriter())
                .thenReturn(new PrintWriter(responseWriter));
    }


    @Test
    void register_success() throws Exception {

        String json = """
                {"username":"machi","password":"secret"}
                """;

        when(request.getInputStream())
                .thenReturn(mockInputStream(json));

        when(authService.register("machi", "secret"))
                .thenReturn(true);

        servlet.doPost(request, response);

        assertTrue(responseWriter.toString()
                .contains("Registered successfully"));
    }


    @Test
    void register_usernameAlreadyExists() throws Exception {

        String json = """
                {"username":"machi","password":"secret"}
                """;

        when(request.getInputStream())
                .thenReturn(mockInputStream(json));

        when(authService.register("machi", "secret"))
                .thenReturn(false);

        servlet.doPost(request, response);

        assertTrue(responseWriter.toString()
                .contains("Username already exists"));
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
