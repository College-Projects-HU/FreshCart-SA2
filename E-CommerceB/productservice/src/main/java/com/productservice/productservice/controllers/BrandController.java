package com.productservice.productservice.controllers;

import com.productservice.productservice.dto.*;
import com.productservice.productservice.services.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/brands")
@RequiredArgsConstructor
public class BrandController {

    private final BrandService brandService;

    @GetMapping
    public ResponseEntity<PagedResponseDTO<BrandDTO>> getAllBrands(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "40") int limit) {
        return ResponseEntity.ok(brandService.getAllBrands(page, limit));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SingleResponseDTO<BrandDTO>> getBrandById(@PathVariable String id) {
        return ResponseEntity.ok(brandService.getBrandById(id));
    }
}
