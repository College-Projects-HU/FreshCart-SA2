package com.ecommerce.orderservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CartItemDto {

    @JsonProperty("_id")
    private String id;

    private Integer count;

    private BigDecimal price;

    /**
     * Serialized as `product` in JSON. This is a full product object coming from product-service
     * through cartservice.
     */
    @JsonProperty("product")
    private Object productDetails;
}

