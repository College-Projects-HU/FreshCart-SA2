package com.ecommerce.orderservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Mirrors the `cartservice` JSON shape used in its responses.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CartDto {

    @JsonProperty("_id")
    private String id; //cart ID from cartservice, stored in Order.mongoId for cross-service compatibility

    @JsonProperty("cartOwner")
    private String cartOwnerId; // corresponds to userId in Order

    /**
     * Serialized as `products` in JSON.
     */
    private List<CartItemDto> products;

    private BigDecimal totalCartPrice;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @JsonProperty("__v")
    private Integer version;
}
