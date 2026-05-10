package com.ecommerce.cartservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * Body for PUT /api/v1/cart/{cartItemId}  →  Update item quantity.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateCartItemRequest {

    @NotNull(message = "count is required")
    @Min(value = 1, message = "count must be at least 1")
    private Integer count;
}
