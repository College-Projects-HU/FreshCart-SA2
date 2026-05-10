package com.ecommerce.userservice.util;

import com.ecommerce.userservice.entities.User;
import com.ecommerce.userservice.exception.UnauthorizedException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}") //add this line to read secret from application.yml
    private String secret;

    @Value("${jwt.expiration-ms}") //add this line to read expiration from application.yml
    private long expirationMs;

    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)); // secret is converted to bytes and then encoded to SecretKey 
    }

    public String generateToken(User user) {
        return Jwts.builder()
                .claim("id", user.getId())
                .claim("name", user.getName())
                .claim("role", user.getRole().name())
                .claim("email", user.getEmail())
                .claim("tokenVersion", user.getTokenVersion())  // ← embed version
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(getKey())
                .compact();
    }

    public Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            throw new UnauthorizedException("Token has expired. Please login again");
        } catch (JwtException e) {
            throw new UnauthorizedException("Invalid token. Please login again");
        }
    }

    public Long extractUserId(String token) {
        return extractAllClaims(token).get("id", Long.class);
    }

    public int extractTokenVersion(String token) {
        return extractAllClaims(token).get("tokenVersion", Integer.class); // ← new
    }


    public String extractTokenFromHeader(String tokenHeader) {
        if (tokenHeader == null || !tokenHeader.startsWith("Bearer ")) {
            throw new UnauthorizedException("Token is required. Please provide a valid token.");
        }
        return tokenHeader.substring(7);
    }
}