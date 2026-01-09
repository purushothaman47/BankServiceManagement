package com.bank.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

public class AuthFilter implements Filter {
    private static final Logger log =
            LoggerFactory.getLogger(AuthFilter.class);
//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res,
                         FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("user") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"msg\":\"Unauthorized\"}");
            return;
        }
        log.info("Authorized user access");
        chain.doFilter(req, res);
    }

//    @Override
//    public void destroy() {
//    }
}
