package com.ecommerce.userservice.security;

import com.ecommerce.userservice.entities.User;
import com.ecommerce.userservice.exception.UnauthorizedException;
import com.ecommerce.userservice.repos.UserRepository;
import com.ecommerce.userservice.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
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
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        if (path.startsWith("/api/v1/auth/")) {
            filterChain.doFilter(request, response);
            return;
        }

        String tokenHeader = request.getHeader("Authorization");

        if (tokenHeader == null || tokenHeader.isBlank()) {
            writeErrorResponse(response,
                "Token is required. Please provide a valid token.");
            return;
        }

        try {
            String token = jwtUtil.extractTokenFromHeader(tokenHeader);
            Claims claims = jwtUtil.extractAllClaims(token);
            Long userId = claims.get("id", Long.class);
            int tokenVersion = claims.get("tokenVersion", Integer.class);

            User user = userRepository.findById(userId).orElse(null);

            if (user == null) {
                writeErrorResponse(response,
                    "User not found. Please login again.");
                return;
            }

            if (user.getTokenVersion() != tokenVersion) {
                writeErrorResponse(response,
                    "User recently changed password! Please login again.");
                return;
            }

            List<SimpleGrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority("ROLE_" + user.getRole().name().toUpperCase())
            );

            UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userId, null, authorities);

            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (UnauthorizedException ex) {
            writeErrorResponse(response, ex.getMessage());
            return;
        } catch (Exception ex) {
            writeErrorResponse(response,
                "You are not logged in. Please login to get access");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void writeErrorResponse(HttpServletResponse response,
                                    String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Map<String, String> body = Map.of(
            "statusMsg", "fail",
            "message", message
        );

        objectMapper.writeValue(response.getOutputStream(), body);
    }
}
