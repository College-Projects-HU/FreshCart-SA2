package com.ecommerce.orderservice.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ecommerce.orderservice.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
@Slf4j

public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String tokenHeader = request.getHeader("Authorization");
        if (tokenHeader == null || tokenHeader.isBlank()) {
            writeErrorResponse(response, "Token is required. Please provide a valid token.");
            return;
        }

        try {
            String token = jwtUtil.extractTokenFromHeader(tokenHeader);
            Claims claims = jwtUtil.extractAllClaims(token);

            Long userId = claims.get("id", Long.class);
            String userName = claims.get("name", String.class);
            String role = claims.get("role", String.class);
            String email = claims.get("email", String.class);
            log.debug("JwtAuthFilter → userId={}, userName={}, role={}, email={}", userId, userName, role, email);

            if (userId == null || role == null || role.isBlank()) {
                writeErrorResponse(response, "Invalid token. Please login again");
                return;
            }

            String normalizedRole = "ROLE_" + role.toUpperCase();   // "ROLE_ADMIN"

            List<SimpleGrantedAuthority> authorities = List.of(
                    new SimpleGrantedAuthority(normalizedRole)
            );

            AuthenticatedUser authenticatedUser = new AuthenticatedUser(userId, userName, email, normalizedRole); 
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(authenticatedUser, null, authorities);
            authentication.setDetails(Map.of(
                    "id", userId,
                    "username", userName == null ? "" : userName,
                    "email", email == null ? "" : email,
                    "role", role
            ));

            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (JwtException ex) {
            log.error("JWT error: {}", ex.getMessage()); // add this
            writeErrorResponse(response, ex.getMessage());
            return;
        } catch (Exception ex) {
            log.error("JWT error: {}", ex.getMessage()); // add this
            writeErrorResponse(response, "You are not logged in. Please login to get access");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void writeErrorResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getOutputStream(), Map.of(
                "status", "error",
                "error", "Invalid Token"
                ,"message", message
        ));
    }
}

