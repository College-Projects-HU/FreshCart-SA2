package com.ecommerce.orderservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long itemId;

    /** Matches the cart item _id from the cart service */
    @Column(name = "cart_item_ref")
    private String cartItemRef;

    @Column(name = "product_id", nullable = false)
    private String productId;

    /**
     * Snapshot of product object at order time (JSON).
     * This lets list endpoints return the nested product object without re-calling product-service.
     */
    @Lob
    @Column(name = "product_snapshot", columnDefinition = "TEXT")
    private String productSnapshot;

    @Column(nullable = false)
    private int count;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;
}
