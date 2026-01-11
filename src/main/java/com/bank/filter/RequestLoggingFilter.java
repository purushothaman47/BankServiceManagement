package com.bank.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

public class RequestLoggingFilter implements Filter {

    private static final Logger LOG =
            LoggerFactory.getLogger(RequestLoggingFilter.class);


    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {

        LOG.info("Incoming Request");
        chain.doFilter(request,response);
    }

}
