package com.waracle.cake_manager.config;

import com.waracle.cake_manager.service.JwtUserDetailsService;
import io.jsonwebtoken.ExpiredJwtException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.logging.Logger;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = Logger.getGlobal();
    private static final String BEARER = "Bearer ";

    private final JwtUserDetailsService jwtUserDetailsService;

    private final JwtTokenUtil jwtTokenUtil;

    public JwtRequestFilter(JwtUserDetailsService jwtUserDetailsService, JwtTokenUtil jwtTokenUtil) {
        this.jwtUserDetailsService = Objects.requireNonNull(jwtUserDetailsService, () -> "Missing a JWT user details " +
                "service");
        this.jwtTokenUtil = Objects.requireNonNull(jwtTokenUtil, () -> "Missing a JWT token utility");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        LOGGER.info(">>> Running JwtRequestFilter::doFilterInternal()");
        LOGGER.info(String.format("\trequest = [%s]", request));
        LOGGER.info(String.format("\trequest.getRequestURL() = [%s]", request.getRequestURL()));
        LOGGER.info(String.format("\trequest.getRequestURI() = [%s]", request.getRequestURI()));

        final String requestTokenHeader = request.getHeader("Authorization");

        LOGGER.info(String.format("\trequestTokenHeader = [%s]", requestTokenHeader));

        String username = null;
        String jwtToken = null;

        if (requestNeedsAuthentication(request)) {
            // A JWT Token is in the form "Bearer token" so remove "Bearer" as the token is what's important...
            if (StringUtils.isNotBlank(requestTokenHeader) && requestTokenHeader.startsWith(BEARER)) {
                jwtToken = requestTokenHeader.substring(BEARER.length());
                try {
                    username = jwtTokenUtil.getUsernameFromToken(jwtToken);
                } catch (IllegalArgumentException e) {
                    LOGGER.warning("\tUnable to get JWT Token");
                } catch (ExpiredJwtException e) {
                    LOGGER.warning("\tJWT Token has expired!!!");
                }
            } else {
                LOGGER.warning("\tCheck that the JWT Token starts with \"Bearer \"");
            }

            // Once we get the token validate it...
            if (StringUtils.isNotBlank(username) && StringUtils.isNotBlank(jwtToken) && noAuthenticationAvailable()) {
                UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(username);

                // If the token is valid, configure Spring Security to manually set authentication
                if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails, null, userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken
                            .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    /*
                     * Current user has been authenticated, so update the security context to reflect a successful
                     * authentication
                     */
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Flag if the current request needs to be authenticated
     *
     * @param request The HTTP request
     * @return {@code true} when authentication is required, otherwise {@code false}
     */
    private boolean requestNeedsAuthentication(HttpServletRequest request) {
        return Arrays.stream(WebSecurityConfig.REQUESTS_NOT_NEEDING_AUTHENTICATION).noneMatch(s -> s.equals(request.getRequestURI()));
    }

    private boolean noAuthenticationAvailable() {
        return SecurityContextHolder.getContext().getAuthentication() == null;
    }
}
