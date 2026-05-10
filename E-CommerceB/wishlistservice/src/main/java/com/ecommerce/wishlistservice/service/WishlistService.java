package com.ecommerce.wishlistservice.service;

import com.ecommerce.wishlistservice.dto.FullWishlistResponse;
import com.ecommerce.wishlistservice.dto.WishlistResponse;

public interface WishlistService {

    WishlistResponse addToWishlist(String productId, String userId);

    WishlistResponse removeFromWishlist(String productId, String userId);

    FullWishlistResponse getWishlist(String userId);
}