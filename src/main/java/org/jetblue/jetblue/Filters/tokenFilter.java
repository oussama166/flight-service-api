package org.jetblue.jetblue.Filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class tokenFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
            return;
        }
        String token = header.substring(7); // Remove "Bearer " prefix
        // Here you would typically validate the token
        // For example, check if it's valid, not expired, etc.
        // If valid, proceed with the request
        System.out.println("Token is valid: " + token);
        response.addHeader("Access-Control-Allow-Origin", "*");
        filterChain.doFilter(request, response);

    }
}
