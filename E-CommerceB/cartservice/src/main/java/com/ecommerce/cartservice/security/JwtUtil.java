package com.ecommerce.cartservice.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
@Slf4j
public class JwtUtil {

    private final SecretKey secretKey;

    public JwtUtil(@Value("${jwt.secret}") String secret) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.warn("JWT expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.warn("JWT unsupported: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.warn("JWT malformed: {}", e.getMessage());
        } catch (SecurityException e) {
            log.warn("JWT signature invalid: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.warn("JWT claims empty: {}", e.getMessage());
        }
        return false;
    }

    public boolean isTokenExpired(String token) {
        try {
            return parseClaims(token).getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        }
    }

    /**
     * Extracts the user ID from the token.
     *
     * UserService may store the ID in different places depending on implementation:
     *   - sub  (standard JWT subject)
     *   - id   (custom claim)
     *   - _id  (MongoDB style)
     *
     * We try all of them in order and return the first non-null value.
     */
    public String extractUserId(String token) {
        Claims claims = parseClaims(token);

        // DEBUG — log all claims so we can see exactly what's inside
        log.debug(">>> JWT Claims: {}", claims);

        // Try standard subject first
        if (claims.getSubject() != null) {
            log.debug(">>> userId from sub: {}", claims.getSubject());
            return claims.getSubject();
        }

        // Try "id" claim (common in Node.js/Express UserServices)
        Object id = claims.get("id");
        if (id != null) {
            log.debug(">>> userId from 'id' claim: {}", id);
            return id.toString();
        }

        // Try "_id" claim (MongoDB ObjectId style)
        Object _id = claims.get("_id");
        if (_id != null) {
            log.debug(">>> userId from '_id' claim: {}", _id);
            return _id.toString();
        }

        // Try "userId" claim
        Object userId = claims.get("userId");
        if (userId != null) {
            log.debug(">>> userId from 'userId' claim: {}", userId);
            return userId.toString();
        }

        log.warn(">>> Could not find user ID in JWT claims: {}", claims);
        return null;
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}