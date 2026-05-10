package com.ecommerce.orderservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderResponse {

    private String status;
    private String message;
    private UserSummary user;
    private PricingSummary pricing;
    private OrderData data;

    // ─── User Summary (top-level) ─────────────────────────────────────────────
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserSummary {
        private String id;
        private String name;
        private String email;
    }

    // ─── Pricing Summary (top-level) ──────────────────────────────────────────
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PricingSummary {
        private BigDecimal cartPrice;
        private BigDecimal taxPrice;
        private BigDecimal shippingPrice;
        private BigDecimal totalOrderPrice;
    }

    // ─── Order Data ───────────────────────────────────────────────────────────
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderData {

        private ShippingAddress shippingAddress;
        private BigDecimal taxPrice;
        private BigDecimal shippingPrice;
        private BigDecimal totalOrderPrice;
        private String paymentMethodType;

        @JsonProperty("isPaid")
        private boolean isPaid;

        @JsonProperty("isDelivered")
        private boolean isDelivered;

        @JsonProperty("_id")
        private Long id;
        private UserDetail user;

        private List<CartItem> cartItems;
        private String createdAt;
        private String updatedAt;

        private String orderId;

        @JsonProperty("__v")
        private int version;
    }

    // ─── Shipping Address ─────────────────────────────────────────────────────
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ShippingAddress {
        private String details;
        private String phone;
        private String city;
        private String postalCode;
    }

    // ─── User Detail (inside data) ────────────────────────────────────────────
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserDetail {

        @JsonProperty("_id")
        private String id;

        private String name;
        private String email;
        private String phone;
    }

    // ─── Cart Item ────────────────────────────────────────────────────────────
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CartItem {

        private int count;

        @JsonProperty("_id")
        private String id;

        private Product product;
        private BigDecimal price;
    }

    // ─── Product ──────────────────────────────────────────────────────────────
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Product {

        private List<Subcategory> subcategory;
        private int ratingsQuantity;

        @JsonProperty("_id")
        private String id;

        private String title;
        private String imageCover;
        private Category category;
        private Brand brand;
        private double ratingsAverage;
    }

    // ─── Subcategory ──────────────────────────────────────────────────────────
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Subcategory {

        @JsonProperty("_id")
        private String id;

        private String name;
        private String slug;
        private String category;
    }

    // ─── Category ─────────────────────────────────────────────────────────────
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Category {

        @JsonProperty("_id")
        private String id;

        private String name;
        private String slug;
        private String image;
    }

    // ─── Brand ────────────────────────────────────────────────────────────────
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Brand {

        @JsonProperty("_id")
        private String id;

        private String name;
        private String slug;
        private String image;
    }
}