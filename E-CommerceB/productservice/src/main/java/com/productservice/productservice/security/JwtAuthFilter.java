package com.productservice.productservice.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.productservice.productservice.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;

    private static final List<String> PUBLIC_PATHS = List.of(
            "/api/v1/products",
            "/api/v1/categories",
            "/api/v1/brands",
            "/api/v1/subcategories"
            // ,"/api/v2/admin"          // ← remove when done testing
    );
    

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();
        boolean isPublic = PUBLIC_PATHS.stream().anyMatch(path::startsWith);
        System.out.println(">>> PATH: " + path + " | isPublic: " + isPublic);

        if (isPublic) {
            filterChain.doFilter(request, response);
            return;
        }

        String tokenHeader = request.getHeader("Authorization");
        if (tokenHeader == null || tokenHeader.isBlank()) {
            writeErrorResponse(response, "Token is required. Please provide a valid token.");
            return;
        }

        try {
            String token = jwtUtil.extractTokenFromHeader(tokenHeader);
            Claims claims = jwtUtil.extractAllClaims(token);

            Long userId = claims.get("id", Long.class);
            String role = claims.get("role", String.class);

            if (userId == null || role == null || role.isBlank()) {
                writeErrorResponse(response, "Invalid token. Please login again");
                return;
            }

            List<SimpleGrantedAuthority> authorities = List.of(
                    new SimpleGrantedAuthority("ROLE_" + role.toUpperCase())
            );
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userId, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (JwtException ex) {
            writeErrorResponse(response, ex.getMessage());
            return;
        } catch (Exception ex) {
            writeErrorResponse(response, "You are not logged in. Please login to get access");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void writeErrorResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getOutputStream(), Map.of(
                "statusMsg", "fail",
                "message", message
        ));
    }
}