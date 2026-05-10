package com.ecommerce.orderservice.controller;

import com.ecommerce.orderservice.dto.*;
import com.ecommerce.orderservice.security.AuthenticatedUser;
import com.ecommerce.orderservice.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Handles HTTP concerns only:
 *  - extract the authenticated user and raw token from the request
 *  - delegate everything to OrderService
 *  - return the HTTP response
 *
 * No business logic, no SecurityContext reads beyond resolving the caller.
 */
@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;

    // ─── 1. Create Cash Order ─────────────────────────────────────────────────
    @PostMapping("/{cartId}")
    public ResponseEntity<CreateOrderResponse> createCashOrder(
            @PathVariable String cartId,
            @Valid @RequestBody CreateOrderRequest request,
            HttpServletRequest httpRequest) {

        AuthenticatedUser caller = resolveAuthenticatedUser();
        String token = extractBearerToken(httpRequest);
        log.debug("createCashOrder → userId={}, cartId={}", caller.getId(), cartId);

        return ResponseEntity.ok(
                orderService.createCashOrder(cartId, caller, request.getShippingAddress(), token));
    }

    // ─── 2. List All Orders ───────────────────────────────────────────────────
    @GetMapping
    public ResponseEntity<ListOrdersResponse> getAllOrders(HttpServletRequest httpRequest,
            @RequestParam(defaultValue = "1")  int page,
            @RequestParam(defaultValue = "40") int limit) {

        AuthenticatedUser caller = resolveAuthenticatedUser();
        boolean isAdmin = hasRole(caller, "ROLE_ADMIN");

        if (!isAdmin ) {
            throw new IllegalStateException("Unauthorized");
        }
        log.debug("getAllOrders → page={}, limit={}", page, limit);
        return ResponseEntity.ok(orderService.getAllOrders(page, limit));
    }

    // ─── 3. Get Orders for a Specific User ───────────────────────────────────
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderData>> getUserOrders(@PathVariable String userId) {

        AuthenticatedUser caller = resolveAuthenticatedUser();
        boolean isAdmin = hasRole(caller, "ROLE_ADMIN");

        if (!isAdmin && !caller.getId().toString().equals(userId)) {
            throw new IllegalStateException("Unauthorized");
        }

        log.debug("getUserOrders → userId={}", userId);
        return ResponseEntity.ok(orderService.getUserOrders(userId));
    }

    // ─── 4. Create Checkout Session ───────────────────────────────────────────
    @PostMapping("/checkout-session/{cartId}")
    public ResponseEntity<CheckoutSessionResponse> createCheckoutSession(
            @PathVariable String cartId,
            @RequestParam String url,
            @Valid @RequestBody CheckoutSessionRequest request,
            HttpServletRequest httpRequest) {

        AuthenticatedUser caller = resolveAuthenticatedUser();
        String token = extractBearerToken(httpRequest);
        log.debug("createCheckoutSession → userId={}, cartId={}, url={}", caller.getId(), cartId, url);

        return ResponseEntity.ok(
                orderService.createCheckoutSession(cartId, caller, request.getShippingAddress(), url, token));
    }
    @PostMapping("/webhook")
    public ResponseEntity<Void> stripeWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader) {
        orderService.handleStripeWebhook(payload, sigHeader);
        return ResponseEntity.ok().build();
    }

    // ─── Helpers ──────────────────────────────────────────────────────────────

    /**
     * Resolves the fully typed {@link AuthenticatedUser} from the security context.
     * The JWT filter is responsible for placing this object as the principal.
     */
    private AuthenticatedUser resolveAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof AuthenticatedUser user)) {
            throw new IllegalStateException("No authenticated user found in SecurityContext");
        }
        return user;
    }

    /**
     * Extracts the raw JWT value from the {@code Authorization: Bearer <token>} header.
     */
    private String extractBearerToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        throw new IllegalStateException("Missing Bearer token");
    }

    private boolean hasRole(AuthenticatedUser user, String role) {
        return role.equals(user.getRole());  
    }
}