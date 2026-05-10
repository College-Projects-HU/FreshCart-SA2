package com.productservice.productservice.controllers;

import com.productservice.productservice.dto.*;
import com.productservice.productservice.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<PagedResponseDTO<ProductDTO>> getAllProducts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "40") int limit) {
        return ResponseEntity.ok(productService.getAllProducts(page, limit));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SingleResponseDTO<ProductDTO>> getProductById(@PathVariable String id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }
}
