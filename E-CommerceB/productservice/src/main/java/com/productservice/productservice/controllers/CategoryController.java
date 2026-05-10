package com.productservice.productservice.controllers;

import com.productservice.productservice.dto.*;
import com.productservice.productservice.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<PagedResponseDTO<CategoryDTO>> getAllCategories(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "40") int limit) {
        return ResponseEntity.ok(categoryService.getAllCategories(page, limit));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SingleResponseDTO<CategoryDTO>> getCategoryById(@PathVariable String id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @GetMapping("/{id}/subcategories")
    public ResponseEntity<PagedResponseDTO<SubcategoryDTO>> getSubcategories(
            @PathVariable String id,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "40") int limit) {
        return ResponseEntity.ok(categoryService.getSubcategoriesByCategoryId(id, page, limit));
    }
}
