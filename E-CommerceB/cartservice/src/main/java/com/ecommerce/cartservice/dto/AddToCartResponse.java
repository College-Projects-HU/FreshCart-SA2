package com.ecommerce.cartservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

/**
 * Response for POST /api/v1/cart — Add product to cart.
 * Includes a "message" field unlike the other cart responses.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"status", "message", "numOfCartItems", "cartId", "data"})
public class AddToCartResponse {

    private String status;
    private String message;
    private Integer numOfCartItems;
    private String cartId;
    private CartDto data;
}