package com.ecommerce.cartservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

/**
 * Response for DELETE /api/v1/cart — Clear entire cart.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"status", "message", "numOfCartItems", "cartId", "data"})
public class ClearCartResponse {

    private String status;
    private String message;
    private Integer numOfCartItems;
    private String cartId;      // null when cart is cleared
    private CartDto data;
}