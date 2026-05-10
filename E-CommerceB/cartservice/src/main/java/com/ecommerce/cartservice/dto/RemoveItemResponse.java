package com.ecommerce.cartservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

/**
 * Response for DELETE /api/v1/cart/{cartItemId} — Remove one item from cart.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"status", "message", "numOfCartItems", "cartId", "data"})
public class RemoveItemResponse {

    private String status;
    private String message;
    private Integer numOfCartItems;
    private String cartId;
    private CartDto data;
}