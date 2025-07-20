package org.jetblue.jetblue.Filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.jetblue.jetblue.Service.Implementation.TokenImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class authFilter extends OncePerRequestFilter {

    private TokenImpl token;
    private static final Logger logger = LoggerFactory.getLogger(authFilter.class);
    public authFilter(TokenImpl token) {
        this.token = token;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String path = request.getServletPath();

        // Skip filter for login and error endpoints
        if (path.equals("/login") || path.equals("/error")) {
            filterChain.doFilter(request, response);
            return;
        }

        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            if (this.token.validateToken(token)) {
                logger.info("{} validated", token);
                SecurityContextHolder.getContext().setAuthentication(this.token.getAuthentication(token));
                logger.info("{} authenticated", SecurityContextHolder.getContext().getAuthentication().isAuthenticated());
            }
        }
        filterChain.doFilter(request, response);
    }
}
