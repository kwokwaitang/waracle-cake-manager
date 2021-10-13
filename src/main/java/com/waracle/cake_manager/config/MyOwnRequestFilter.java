package com.waracle.cake_manager.config;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Set up my own HTTP request/response filter
 */
@Component
public class MyOwnRequestFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = Logger.getGlobal();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        LOGGER.info(">>> Running MyOwnRequestFilter::doFilterInternal()");
        LOGGER.info(String.format("\trequest = [%s]", request));
        LOGGER.info(String.format("\trequest.getRequestURL() = [%s]", request.getRequestURL()));
        LOGGER.info(String.format("\trequest.getRequestURI() = [%s]", request.getRequestURI()));

        // Just want to see my own filter being invoked...

        filterChain.doFilter(request, response);
    }
}
