package com.ecommerce.cartservice.service;

import com.ecommerce.cartservice.dto.*;
import com.ecommerce.cartservice.entity.Cart;
import com.ecommerce.cartservice.entity.CartItem;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Single-responsibility mapper — one method per endpoint response type.
 * Each method builds exactly the shape that endpoint needs.
 */
@Component
public class CartMapper {

    // ─── 1. Add To Cart ───────────────────────────────────────────────────────
    public AddToCartResponse toAddToCartResponse(Cart cart, Map<String, ProductDto> productMap) {
        return AddToCartResponse.builder()
                .status("success")
                .message("Product added successfully to your cart")
                .numOfCartItems(cart.getItems().size())
                .cartId(cart.getId().toString())
                .data(toCartDto(cart, productMap))
                .build();
    }

    // ─── 2. Get User Cart ─────────────────────────────────────────────────────
    public GetCartResponse toGetCartResponse(Cart cart, Map<String, ProductDto> productMap) {
        return GetCartResponse.builder()
                .status("success")
                .numOfCartItems(cart.getItems().size())
                .cartId(cart.getId().toString())
                .data(toCartDto(cart, productMap))
                .build();
    }

    // ─── 3. Update Cart Item ──────────────────────────────────────────────────
    public UpdateCartResponse toUpdateCartResponse(Cart cart, Map<String, ProductDto> productMap) {
        return UpdateCartResponse.builder()
                .status("success")
                .message("Cart updated")
                .numOfCartItems(cart.getItems().size())
                .cartId(cart.getId().toString())
                .data(toCartDto(cart, productMap))
                .build();
    }

    // ─── 4. Remove Cart Item ──────────────────────────────────────────────────
    public RemoveItemResponse toRemoveItemResponse(Cart cart) {
        return RemoveItemResponse.builder()
                .status("success")
                .message("Item removed from cart")
                .numOfCartItems(cart.getItems().size())
                .cartId(cart.getId().toString())
                .data(toCartDto(cart, null))
                .build();
    }

    // ─── 5. Clear Cart ────────────────────────────────────────────────────────
    public ClearCartResponse toClearCartResponse() {
        return ClearCartResponse.builder()
                .status("success")
                .message("Cart cleared")
                .numOfCartItems(0)
                .cartId(null)
                .data(CartDto.builder()
                        .products(List.of())
                        .totalCartPrice(BigDecimal.ZERO)
                        .build())
                .build();
    }

    // ─── Shared CartDto builder ───────────────────────────────────────────────
    private CartDto toCartDto(Cart cart, Map<String, ProductDto> productMap) {
        return CartDto.builder()
                .id(cart.getId().toString())
                .cartOwnerId(cart.getCartOwnerId())
                .products(toItemDtos(cart.getItems(), productMap))
                .totalCartPrice(cart.getTotalCartPrice())
                .createdAt(cart.getCreatedAt())
                .updatedAt(cart.getUpdatedAt())
                .version(0)
                .build();
    }

    private List<CartItemDto> toItemDtos(List<CartItem> items,
                                        Map<String, ProductDto> productMap) {
        return items.stream()
                .map(item -> CartItemDto.builder()
                        .id(item.getId().toString())
                        .count(item.getCount())
                        .price(item.getPrice())
                        .productDetails(productMap != null
                                ? productMap.get(item.getProductId())
                                : null)
                        .build())
                .collect(Collectors.toList());
    }
}