package com.ecommerce.cartservice.controller;

import com.ecommerce.cartservice.dto.*;
import com.ecommerce.cartservice.service.CartService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
@Slf4j
public class CartController {

    private final CartService cartService;

    // ─── 1. Add Product to Cart ───────────────────────────────────────────────
    @PostMapping
    public ResponseEntity<AddToCartResponse> addToCart(
            @Valid @RequestBody AddToCartRequest request,
            HttpServletRequest httpRequest) {

        String userId = getAuthenticatedUserId();
        String token  = extractToken(httpRequest);
        log.debug("addToCart → userId={}, productId={}", userId, request.getProductId());

        return ResponseEntity.ok(cartService.addToCart(userId, request.getProductId(), token));
    }

    // ─── 2. Get Logged User Cart ──────────────────────────────────────────────
    @GetMapping
    public ResponseEntity<GetCartResponse> getUserCart(HttpServletRequest httpRequest) {

        String userId = getAuthenticatedUserId();
        String token  = extractToken(httpRequest);

        return ResponseEntity.ok(cartService.getUserCart(userId, token));
    }

    // ─── 3. Update Cart Item Quantity ─────────────────────────────────────────
    @PutMapping("/{cartItemId}")
    public ResponseEntity<UpdateCartResponse> updateCartItemQuantity(
            @PathVariable Long cartItemId,
            @Valid @RequestBody UpdateCartItemRequest request,
            HttpServletRequest httpRequest) {

        String userId = getAuthenticatedUserId();
        String token  = extractToken(httpRequest);

        return ResponseEntity.ok(
                cartService.updateCartItemQuantity(userId, cartItemId, request.getCount(), token));
    }

    // ─── 4. Remove One Item from Cart ─────────────────────────────────────────
    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<RemoveItemResponse> removeCartItem(@PathVariable Long cartItemId) {

        String userId = getAuthenticatedUserId();
        return ResponseEntity.ok(cartService.removeCartItem(userId, cartItemId));
    }

    // ─── 5. Clear Entire Cart ─────────────────────────────────────────────────
    @DeleteMapping
    public ResponseEntity<ClearCartResponse> clearCart() {

        String userId = getAuthenticatedUserId();
        return ResponseEntity.ok(cartService.clearCart(userId));
    }

    // ─── Helpers ──────────────────────────────────────────────────────────────
    private String getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {
            throw new IllegalStateException("No authenticated user found in SecurityContext");
        }
        return authentication.getPrincipal().toString();
    }

    private String extractToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return request.getHeader("token");
    }
}