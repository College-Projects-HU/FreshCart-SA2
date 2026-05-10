package com.ecommerce.cartservice.dto;

import com.ecommerce.cartservice.validation.ValidObjectId;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * Body for POST /api/v1/cart  →  Add product to cart.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddToCartRequest {

    @NotBlank(message = "productId is required")
    @ValidObjectId
    private String productId;
}
