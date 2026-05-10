package com.ecommerce.wishlistservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FullWishlistResponse {

    private String status;
    private int count;
    private List<ProductDTO> data;   // full product details from product-service
}