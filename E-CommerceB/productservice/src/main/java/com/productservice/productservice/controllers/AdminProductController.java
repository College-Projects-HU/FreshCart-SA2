package com.productservice.productservice.controllers;

import com.productservice.productservice.dto.AdminUpsertProductRequest;
import com.productservice.productservice.dto.ApiResponseDTO;
import com.productservice.productservice.entities.Product;
import com.productservice.productservice.services.ProductAdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/admin/products")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
public class AdminProductController {

    private final ProductAdminService productAdminService;

    @PostMapping
    public ResponseEntity<ApiResponseDTO<Product>> create(
            @Valid @RequestBody AdminUpsertProductRequest request) {

        Product created = productAdminService.create(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponseDTO.success("Product created successfully", created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Product>> update(
            @PathVariable String id,
            @Valid @RequestBody AdminUpsertProductRequest request) {

        Product updated = productAdminService.update(id, request);
        return ResponseEntity
                .ok(ApiResponseDTO.success("Product updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> delete(@PathVariable String id) {
        productAdminService.delete(id);
        return ResponseEntity
                .ok(ApiResponseDTO.success("Product deleted successfully", null));
    }
}