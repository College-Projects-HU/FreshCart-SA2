package com.ecommerce.cartservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

/**
 * Response for GET /api/v1/cart — Get logged user cart.
 * No "message" field — matches the external API shape exactly.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"status", "numOfCartItems", "cartId", "data"})
public class GetCartResponse {

    private String status;
    private Integer numOfCartItems;
    private String cartId;
    private CartDto data;
}