package com.ecommerce.wishlistservice.controller;

import com.ecommerce.wishlistservice.dto.FullWishlistResponse;
import com.ecommerce.wishlistservice.dto.WishlistRequest;
import com.ecommerce.wishlistservice.dto.WishlistResponse;
import com.ecommerce.wishlistservice.service.WishlistService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;


import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/wishlist")
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;

    // POST /api/v1/wishlist
    @PostMapping
    public ResponseEntity<WishlistResponse> addToWishlist(
            @Valid @RequestBody WishlistRequest request,
            HttpServletRequest httpRequest) {

        String userId = getAuthenticatedUserId();

        WishlistResponse response = wishlistService.addToWishlist(request.getProductId(), userId);
        return ResponseEntity.ok(response);
    }

    // DELETE /api/v1/wishlist/{productId}
    @DeleteMapping("/{productId}")
    public ResponseEntity<WishlistResponse> removeFromWishlist(
            @PathVariable String productId,
            HttpServletRequest httpRequest) {

        String userId = getAuthenticatedUserId();

        WishlistResponse response = wishlistService.removeFromWishlist(productId, userId);
        return ResponseEntity.ok(response);
    }

    // GET /api/v1/wishlist
    @GetMapping
    public ResponseEntity<FullWishlistResponse> getWishlist(
            HttpServletRequest httpRequest) {

        String userId = getAuthenticatedUserId();

        FullWishlistResponse response = wishlistService.getWishlist(userId);
        return ResponseEntity.ok(response);
    }
    // ─── Helpers ──────────────────────────────────────────────────────────────
    private String getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {
            throw new IllegalStateException("No authenticated user found in SecurityContext");
        }
        return authentication.getPrincipal().toString();
    }

}