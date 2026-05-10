package com.ecommerce.orderservice.service;

import com.ecommerce.orderservice.dto.OrderData;
import com.ecommerce.orderservice.dto.OrderItemResponse;
import com.ecommerce.orderservice.dto.UserSummary;
import com.ecommerce.orderservice.model.Order;
import com.ecommerce.orderservice.model.OrderItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Maps {@link Order} entities to response DTOs.
 *
 * No SecurityContext access – all caller data must be injected at the call site.
 * Product snapshot parsing is delegated to {@link ProductSnapshotParser}.
 */
@Component
@RequiredArgsConstructor
public class OrderMapper {

    private final ProductSnapshotParser snapshotParser;

    /**
     * Maps an {@link Order} to {@link OrderData} for list / user-orders endpoints.
     * The user field is populated from the userId stored on the order entity;
     * enrich it further by passing a resolved user object if your list queries
     * join on the user table.
     */
    public OrderData toOrderData(Order order) {
        List<OrderItemResponse> itemResponses = order.getCartItems().stream()
                .map(this::toItemResponse)
                .collect(Collectors.toList());

        return OrderData.builder()
                .id(order.getOrderId())
                .numericId(order.getId())
                .user(buildUserSummary(order))
                .shippingAddress(order.getShippingAddress())
                .cartItems(itemResponses)
                .taxPrice(order.getTaxPrice())
                .shippingPrice(order.getShippingPrice())
                .totalOrderPrice(order.getTotalOrderPrice())
                .paymentMethodType(order.getPaymentMethodType())
                .isPaid(order.isPaid())
                .isDelivered(order.isDelivered())
                .paidAt(order.getPaidAt())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();
    }

    // ─── Private Helpers ──────────────────────────────────────────────────────

    /**
     * Builds a minimal {@link UserSummary} from what is persisted on the order.
     * If a richer user object (joined from a user service) is available, pass it in instead.
     */
    private UserSummary buildUserSummary(Order order) {
        return UserSummary.builder()
                ._id(order.getUserId())
                .name(order.getUserName())   
                .email(order.getUserEmail()) 
                .phone(order.getUserPhone())  
                .build();
    }
    private OrderItemResponse toItemResponse(OrderItem item) {
        Object productObj = snapshotParser.parse(item.getProductSnapshot());

        return OrderItemResponse.builder()
                ._id(item.getCartItemRef())
                .count(item.getCount())
                .price(item.getPrice())
                .product(productObj != null ? productObj : item.getProductId())
                .build();
    }
}