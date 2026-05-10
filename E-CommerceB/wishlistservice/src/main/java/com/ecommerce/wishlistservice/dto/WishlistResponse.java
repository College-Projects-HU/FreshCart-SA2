package com.ecommerce.wishlistservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WishlistResponse {

    private String status;
    private String message;
    private List<String> data;   // list of remaining productIds
}