package com.ecommerce.cartservice.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String token = extractToken(request);

        log.debug(">>> Incoming request: {} {}", request.getMethod(), request.getRequestURI());

        if (token != null) {
            try {
                // ── Check expiry first to give a specific message ─────────────
                if (jwtUtil.isTokenExpired(token)) {
                    log.warn(">>> Token is EXPIRED");
                    writeErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED,
                            "fail", "Expired Token. please login again");
                    return;  // stop the filter chain — don't process the request
                }

                if (jwtUtil.validateToken(token)) {
                    String userId = jwtUtil.extractUserId(token);
                    log.debug(">>> Extracted userId: {}", userId);

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userId, null, List.of());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    log.debug(">>> SecurityContext set for userId: {}", userId);

                } else {
                    log.warn(">>> Token is INVALID");
                    writeErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED,
                            "fail", "Invalid Token. please login again");
                    return;
                }

            } catch (ExpiredJwtException e) {
                log.warn(">>> ExpiredJwtException: {}", e.getMessage());
                writeErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED,
                        "fail", "Expired Token. please login again");
                return;

            } catch (Exception e) {
                log.warn(">>> JWT Exception [{}]: {}", e.getClass().getSimpleName(), e.getMessage());
                writeErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED,
                        "fail", "Invalid Token. please login again");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    // ─── Write JSON error response directly to the HTTP response ──────────────
    private void writeErrorResponse(HttpServletResponse response,
                                    int status,
                                    String statusMsg,
                                    String message) throws IOException {
        response.setStatus(status);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        Map<String, String> body = Map.of(
                "statusMsg", statusMsg,
                "message", message
        );

        objectMapper.writeValue(response.getWriter(), body);
    }

    // ─── Token extraction ─────────────────────────────────────────────────────
    private String extractToken(HttpServletRequest request) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        String tokenHeader = request.getHeader("token");
        if (StringUtils.hasText(tokenHeader)) {
            return tokenHeader;
        }
        return null;
    }
}