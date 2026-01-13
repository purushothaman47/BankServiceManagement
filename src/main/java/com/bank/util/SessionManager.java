package com.bank.util;

import com.bank.config.DBConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public final class SessionManager {

    private static final Logger LOG =
            LoggerFactory.getLogger(SessionManager.class);

    private static final String USER_KEY = "user";

    private SessionManager() {
    }

    public static void createUserSession(HttpServletRequest request, Object user) {
        HttpSession session = request.getSession(true);
        session.setAttribute(USER_KEY, user);
        session.setMaxInactiveInterval(30 * 60);
        LOG.info("Session created");
    }

    public static Object getUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null ? session.getAttribute(USER_KEY) : null;
    }
    public static boolean isLoggedIn(HttpServletRequest request) {
        return getUser(request) != null;
    }

}
