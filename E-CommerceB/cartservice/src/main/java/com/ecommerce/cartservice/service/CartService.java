package com.ecommerce.cartservice.service;

import com.ecommerce.cartservice.client.ProductServiceClient;
import com.ecommerce.cartservice.dto.*;
import com.ecommerce.cartservice.entity.Cart;
import com.ecommerce.cartservice.entity.CartItem;
import com.ecommerce.cartservice.exception.ResourceNotFoundException;
import com.ecommerce.cartservice.repository.CartItemRepository;
import com.ecommerce.cartservice.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartService {

    private final CartRepository       cartRepository;
    private final CartItemRepository   cartItemRepository;
    private final ProductServiceClient productServiceClient;
    private final CartMapper           cartMapper;

    // ─── 1. Add Product to Cart ───────────────────────────────────────────────
    @Transactional
    public AddToCartResponse addToCart(String userId, String productId, String token) {
        log.info("Adding product {} to cart for user {}", productId, userId);

        ProductDto product = productServiceClient.validateAndGetProduct(productId, token);
        BigDecimal price   = product.getPrice() != null ? product.getPrice() : BigDecimal.ZERO;

        Cart cart = cartRepository.findByCartOwnerIdWithItems(userId)
                .orElseGet(() -> cartRepository.save(
                        Cart.builder().cartOwnerId(userId).build()));

        cartItemRepository.findByCartIdAndProductId(cart.getId(), productId)
                .ifPresentOrElse(
                        existing -> {
                            existing.setCount(existing.getCount() + 1);
                            cartItemRepository.save(existing);
                        },
                        () -> cart.addItem(CartItem.builder()
                                .productId(productId)
                                .count(1)
                                .price(price)
                                .build())
                );

        cart.recalculateTotal();
        Cart saved = cartRepository.save(cart);

        Map<String, ProductDto> productMap = new HashMap<>();
        productMap.put(productId, product);

        return cartMapper.toAddToCartResponse(saved, productMap);
    }

    // ─── 2. Get Logged User Cart ──────────────────────────────────────────────
    @Transactional(readOnly = true)
    public GetCartResponse getUserCart(String userId, String token) {
        log.info("Fetching cart for user {}", userId);

        Cart cart = findCartByOwner(userId);

        Map<String, ProductDto> productMap = enrichProductMap(cart, token);

        return cartMapper.toGetCartResponse(cart, productMap);
    }

    // ─── 3. Update Cart Item Quantity ─────────────────────────────────────────
    @Transactional
    public UpdateCartResponse updateCartItemQuantity(String userId, Long cartItemId,
                                                     int count, String token) {
        log.info("Updating cart item {} to count {} for user {}", cartItemId, count, userId);

        Cart cart = findCartByOwner(userId);

        CartItem item = cartItemRepository.findByIdAndCartId(cartItemId, cart.getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No Product Cart item found for this id: " + cartItemId));

        item.setCount(count);
        cartItemRepository.save(item);
        cart.recalculateTotal();
        cartRepository.save(cart);

        Map<String, ProductDto> productMap = enrichProductMap(cart, token);

        return cartMapper.toUpdateCartResponse(cart, productMap);
    }

    // ─── 4. Remove One Item from Cart ─────────────────────────────────────────
    @Transactional
    public RemoveItemResponse removeCartItem(String userId, Long cartItemId) {
        log.info("Removing cart item {} for user {}", cartItemId, userId);

        Cart cart = findCartByOwner(userId);

        CartItem item = cartItemRepository.findByIdAndCartId(cartItemId, cart.getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cast to ObjectId failed for value \"" + cartItemId + "\" at path \"wishlist\""));

        cart.removeItem(item);
        cartRepository.save(cart);

        return cartMapper.toRemoveItemResponse(cart);
    }

    // ─── 5. Clear Entire Cart ─────────────────────────────────────────────────
    @Transactional
    public ClearCartResponse clearCart(String userId) {
        log.info("Clearing cart for user {}", userId);

        Cart cart = findCartByOwner(userId);
        cart.getItems().clear();
        cart.recalculateTotal();
        cartRepository.save(cart);

        return cartMapper.toClearCartResponse();
    }

    // ─── Helpers ──────────────────────────────────────────────────────────────

    private Cart findCartByOwner(String userId) {
        return cartRepository.findByCartOwnerIdWithItems(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No cart found for user: " + userId));
    }

    /**
     * Fetches product details for every item in the cart from ProductService.
     * Uses HashMap + forEach instead of Collectors.toMap to avoid
     * type inference issues when the value can be null.
     */
    private Map<String, ProductDto> enrichProductMap(Cart cart, String token) {
        Map<String, ProductDto> productMap = new HashMap<>();
        for (CartItem item : cart.getItems()) {
            ProductDto product = productServiceClient
                    .getProductById(item.getProductId(), token)
                    .orElse(null);
            productMap.put(item.getProductId(), product);
        }
        return productMap;
    }
}