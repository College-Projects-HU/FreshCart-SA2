package com.ecommerce.orderservice.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Mongo-style string ID stored for cross-service compatibility */
    @Column(name = "order_id", unique = true)
    private String orderId;

    @Column(name = "user_id", nullable = false)
    private String userId;
    @Column(name = "user_email")
    private String userEmail;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "user_phone")
    private String userPhone;
    
    @Embedded
    private ShippingAddress shippingAddress;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "order_id")
    @Builder.Default
    private List<OrderItem> cartItems = new ArrayList<>();

    @Column(nullable = false, precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal taxPrice = BigDecimal.ZERO;

    @Column(nullable = false, precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal shippingPrice = BigDecimal.ZERO;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalOrderPrice;

    @Column(nullable = false)
    @Builder.Default
    private String paymentMethodType = "cash";

    @Builder.Default
    private boolean isPaid = false;

    private Instant paidAt;

    @Builder.Default
    private boolean isDelivered = false;

    private Instant deliveredAt;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;
}
