package com.productservice.productservice.dto;

import java.util.List;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AdminUpsertProductRequest(
        String id,
        @NotBlank String title,
        @NotBlank String slug,
        @NotNull @Min(0) Double price,
        @NotNull @Min(0) Integer quantity,
        String description,
        String imageCover,
        List<String> images,
        @NotBlank String CategoryId,
        String BrandId,
        // List<SubcategoryDTO> subcategory
        List<String> SubcategoryIds

) {

}

