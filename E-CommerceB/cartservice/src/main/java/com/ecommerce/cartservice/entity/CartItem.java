package com.ecommerce.cartservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * A single line-item inside a Cart.
 * Stores the productId (from ProductService) and a snapshot of the price at the time it was added.
 */
@Entity
@Table(name = "cart_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** FK to the parent Cart. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    /**
     * The external product ID from ProductService.
     * Stored as a String to match MongoDB ObjectId format used by the external API.
     */
    @Column(name = "product_id", nullable = false)
    private String productId;

    /** Quantity of this product in the cart. */
    @Column(nullable = false)
    @Builder.Default
    private Integer count = 1;

    /**
     * Price snapshot at the moment the item was added.
     * Prevents price changes in ProductService from silently changing cart totals.
     */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;
}
