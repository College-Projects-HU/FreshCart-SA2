package com.ecommerce.wishlistservice.service;

import com.ecommerce.wishlistservice.dto.FullWishlistResponse;
import com.ecommerce.wishlistservice.dto.ProductDTO;
import com.ecommerce.wishlistservice.dto.ProductResponseWrapper;
import com.ecommerce.wishlistservice.dto.WishlistResponse;
import com.ecommerce.wishlistservice.entity.WishlistItem;
import com.ecommerce.wishlistservice.exception.ResourceNotFoundException;
import com.ecommerce.wishlistservice.repo.WishlistRepository;
import com.ecommerce.wishlistservice.clients.ProductClient;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WishlistServiceImpl implements WishlistService {

    private final WishlistRepository wishlistRepository;
    private final ProductClient productClient;

    @Override
    public WishlistResponse addToWishlist(String productId, String userId) {
        Long userIdLong = Long.parseLong(userId);
        if (wishlistRepository.existsByUserIdAndProductId(userIdLong, productId)) {
            throw new RuntimeException("Product already in wishlist");
        }

        WishlistItem item = new WishlistItem();
        item.setUserId(userIdLong);
        item.setProductId(productId);
        wishlistRepository.save(item);

        List<String> productIds = wishlistRepository.findAllByUserId(userIdLong)
                .stream()
                .map(WishlistItem::getProductId)
                .collect(Collectors.toList());

        return new WishlistResponse("success", "Product added successfully to your wishlist", productIds);
    }

    @Override
    public WishlistResponse removeFromWishlist(String productId, String userId) {

        WishlistItem item = wishlistRepository.findByUserIdAndProductId(Long.parseLong(userId), productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found in wishlist"));

        wishlistRepository.delete(item);

        List<String> remaining = wishlistRepository.findAllByUserId(Long.parseLong(userId))
                .stream()
                .map(WishlistItem::getProductId)
                .collect(Collectors.toList());

        return new WishlistResponse("success", "Product removed successfully from your wishlist", remaining);
    }

    @Override
    public FullWishlistResponse getWishlist(String userId) {

        List<ProductDTO> products = wishlistRepository.findAllByUserId(Long.parseLong(userId))
                .stream()
                .map(item -> {
                    ProductResponseWrapper wrapper = productClient.getProductById(item.getProductId());
                    return wrapper != null ? wrapper.getData() : null;
                })
                .filter(product -> product != null)
                .collect(Collectors.toList());

        return new FullWishlistResponse("success", products.size(), products);
    }
}