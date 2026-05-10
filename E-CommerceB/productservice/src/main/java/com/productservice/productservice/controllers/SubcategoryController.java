package com.productservice.productservice.controllers;

import com.productservice.productservice.dto.*;
import com.productservice.productservice.services.SubcategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/subcategories")
@RequiredArgsConstructor
public class SubcategoryController {

    private final SubcategoryService subcategoryService;

    @GetMapping
    public ResponseEntity<PagedResponseDTO<SubcategoryDTO>> getAllSubcategories(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "40") int limit) {
        return ResponseEntity.ok(subcategoryService.getAllSubcategories(page, limit));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SingleResponseDTO<SubcategoryDTO>> getSubcategoryById(@PathVariable String id) {
        System.out.println("Received request for subcategory with id: " + id);
        return ResponseEntity.ok(subcategoryService.getSubcategoryById(id));
    }
}
