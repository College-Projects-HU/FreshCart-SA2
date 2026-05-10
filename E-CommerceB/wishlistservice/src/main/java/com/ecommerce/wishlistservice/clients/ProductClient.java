package com.ecommerce.wishlistservice.clients;

import com.ecommerce.wishlistservice.dto.ProductResponseWrapper;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// "product-service" must match spring.application.name in productservice's application.yml
@FeignClient(name = "product-service")
public interface ProductClient {

    @GetMapping("/api/v1/products/{id}")
    ProductResponseWrapper getProductById(@PathVariable("id") String productId);
}