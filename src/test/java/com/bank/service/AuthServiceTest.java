package com.bank.service;

import com.bank.dao.UserDAO;
import com.bank.model.User;
import com.bank.util.PasswordUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    private AuthService authService;
    private UserDAO userDAO;

    @BeforeEach
    void setup() throws Exception {

        authService = new AuthService();
        userDAO = mock(UserDAO.class);
        Field field = AuthService.class.getDeclaredField("userDAO");
        field.setAccessible(true);
        field.set(authService, userDAO);
    }

    @Test
    void registerSuccess() {

        when(userDAO.findByUsername("machi"))
                .thenReturn(null);

        boolean result =
                authService.register("machi", "secret");

        assertTrue(result);

        verify(userDAO).save(
                eq("machi"),
                eq(PasswordUtil.hash("secret"))
        );
    }

    @Test
    void registerUserAlreadyExists() {

        when(userDAO.findByUsername("machi"))
                .thenReturn(new User());

        boolean result =
                authService.register("machi", "secret");

        assertFalse(result);

        verify(userDAO, never())
                .save(any(), any());
    }

    @Test
    void loginSuccess() {

        User user = new User();
        user.setUsername("machi");
        user.setPassword(PasswordUtil.hash("secret"));

        when(userDAO.findByUsername("machi"))
                .thenReturn(user);

        User result =
                authService.login("machi", "secret");

        assertNotNull(result);
        assertEquals("machi", result.getUsername());
    }

    @Test
    void loginWrongPassword() {

        User user = new User();
        user.setUsername("machi");
        user.setPassword(PasswordUtil.hash("correct"));

        when(userDAO.findByUsername("machi"))
                .thenReturn(user);

        User result =
                authService.login("machi", "wrong");

        assertNull(result);
    }

    @Test
    void loginUserNotFound() {

        when(userDAO.findByUsername("unknown"))
                .thenReturn(null);

        User result =
                authService.login("unknown", "pwd");

        assertNull(result);
    }
}
