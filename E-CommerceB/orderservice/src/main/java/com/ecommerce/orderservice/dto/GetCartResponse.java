package com.ecommerce.orderservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

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

