package com.ecommerce.cartservice.config;

import com.ecommerce.cartservice.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security configuration for CartService.
 *
 * - All /api/v1/cart/** endpoints require a valid JWT.
 * - Session is stateless (we rely on JWT, not server-side sessions).
 * - CSRF is disabled (REST API, not browser form submission).
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Disable CSRF for stateless REST API
            .csrf(AbstractHttpConfigurer::disable)

            // Stateless sessions — JWT carries all auth state
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // Return 401 (not 302 redirect) for unauthenticated requests
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))

            // Route authorization rules
            .authorizeHttpRequests(auth -> auth
                // All cart endpoints require authentication
                .requestMatchers("/api/v1/cart/**").authenticated()
                // Actuator health check — open for load balancers
                .requestMatchers("/actuator/health").permitAll()
                // Everything else also requires auth
                .anyRequest().authenticated())

            // Place JWT filter before Spring's default username/password filter
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
