package com.ecommerce.orderservice.dto;

import com.ecommerce.orderservice.model.ShippingAddress;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderData {

    private ShippingAddress shippingAddress;

    private BigDecimal taxPrice;
    private BigDecimal shippingPrice;

    @JsonProperty("totalOrderPrice")
    private BigDecimal totalOrderPrice;

    private String paymentMethodType;

    private Boolean isPaid;
    private Boolean isDelivered;

    @JsonProperty("_id")
    private String id;

    private UserSummary user;

    private List<OrderItemResponse> cartItems;

    private Instant paidAt;
    private Instant createdAt;
    private Instant updatedAt;

    /**
     * Numeric DB id. The example uses "id": 3711.
     */
    @JsonProperty("id")
    private Long numericId;

    @JsonProperty("__v")
    private Integer version;
    
}

