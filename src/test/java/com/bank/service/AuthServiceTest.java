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

        when(userDAO.findByUsername("Purushothaman"))
                .thenReturn(null);

        boolean result =
                authService.register("Purushothaman", "1234");

        assertTrue(result);

        verify(userDAO).save(
                eq("Purushothaman"),
                eq(PasswordUtil.hash("1234"))
        );
    }

    @Test
    void registerUserAlreadyExists() {

        when(userDAO.findByUsername("Purushothaman"))
                .thenReturn(new User());

        boolean result =
                authService.register("Purushothaman", "1234");

        assertFalse(result);

        verify(userDAO, never())
                .save(any(), any());
    }

    @Test
    void loginSuccess() {

        User user = new User();
        user.setUsername("Purushothaman");
        user.setPassword(PasswordUtil.hash("1234"));

        when(userDAO.findByUsername("Purushothaman"))
                .thenReturn(user);

        User result =
                authService.login("Purushothaman", "1234");

        assertNotNull(result);
        assertEquals("Purushothaman", result.getUsername());
    }

    @Test
    void loginWrongPassword() {

        User user = new User();
        user.setUsername("Purushothaman");
        user.setPassword(PasswordUtil.hash("4321"));

        when(userDAO.findByUsername("Purushothaman"))
                .thenReturn(user);

        User result =
                authService.login("Purushothaman", "1234");

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
