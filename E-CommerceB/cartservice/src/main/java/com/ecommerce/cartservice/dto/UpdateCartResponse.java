package com.ecommerce.cartservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

/**
 * Response for PUT /api/v1/cart/{cartItemId} — Update cart item quantity.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"status", "message", "numOfCartItems", "cartId", "data"})
public class UpdateCartResponse {

    private String status;
    private String message;
    private Integer numOfCartItems;
    private String cartId;
    private CartDto data;
}