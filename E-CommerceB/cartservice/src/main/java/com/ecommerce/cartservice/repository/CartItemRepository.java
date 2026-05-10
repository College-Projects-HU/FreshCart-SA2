package com.ecommerce.cartservice.repository;

import com.ecommerce.cartservice.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    /** Find a specific item by its ID and the cart it belongs to (prevents cross-user access). */
    Optional<CartItem> findByIdAndCartId(Long itemId, Long cartId);

    /** Check if a product already exists in a cart (used before adding to avoid duplicates). */
    Optional<CartItem> findByCartIdAndProductId(Long cartId, String productId);
}
