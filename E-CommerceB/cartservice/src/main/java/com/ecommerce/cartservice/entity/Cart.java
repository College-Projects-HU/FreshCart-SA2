package com.ecommerce.cartservice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a user's shopping cart.
 * One cart per user — if a cart already exists for the user, items are added to it.
 */
@Entity
@Table(name = "carts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** The ID of the user who owns this cart (comes from UserService / JWT subject). */
    @Column(name = "cart_owner_id", nullable = false, unique = true)
    private String cartOwnerId;

    /**
     * The list of items in this cart.
     * CascadeType.ALL + orphanRemoval = items are deleted when removed from the list.
     */
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<CartItem> items = new ArrayList<>();

    /** Denormalised total — recalculated on every add / update / remove. */
    @Column(name = "total_cart_price", nullable = false, precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal totalCartPrice = BigDecimal.ZERO;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // ─── Convenience helpers ─────────────────────────────────────────────────

    public void addItem(CartItem item) {
        item.setCart(this);
        this.items.add(item);
        recalculateTotal();
    }

    public void removeItem(CartItem item) {
        this.items.remove(item);
        item.setCart(null);
        recalculateTotal();
    }

    public void recalculateTotal() {
        this.totalCartPrice = items.stream()
                .map(i -> i.getPrice().multiply(BigDecimal.valueOf(i.getCount())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
