package com.ecommerce.cartservice.repository;

import com.ecommerce.cartservice.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    /** Find a cart by the owner's user ID (extracted from JWT). */
    Optional<Cart> findByCartOwnerId(String cartOwnerId);

    /** Check if a cart exists for a given user. */
    boolean existsByCartOwnerId(String cartOwnerId);

    /** Delete a user's cart entirely (used by Clear Cart). */
    void deleteByCartOwnerId(String cartOwnerId);

    /** Eager-fetch items in one query to avoid N+1. */
    @Query("SELECT c FROM Cart c LEFT JOIN FETCH c.items WHERE c.cartOwnerId = :ownerId")
    Optional<Cart> findByCartOwnerIdWithItems(String ownerId);
}
