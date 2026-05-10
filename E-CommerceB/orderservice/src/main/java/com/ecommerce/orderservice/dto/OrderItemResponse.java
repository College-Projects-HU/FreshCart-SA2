package com.ecommerce.orderservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderItemResponse {
    private Integer count;
    private String _id;
    private Object product;
    private BigDecimal price;
}

