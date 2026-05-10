package com.ecommerce.cartservice.dto;

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
    private String id;          // String to match MongoDB style

    private Integer count;
    private BigDecimal price;

    @JsonProperty("product")
    private ProductDto productDetails;  // serialized as "product" in JSON
}