package com.productservice.productservice.dto;

import lombok.Data;

import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

@Data
public class ProductResponseDto {
    @JsonProperty("_id")
    private String _id;
    private String id;
    private String title;
    private String slug;
    private String description;
    private Integer quantity;
    private Double price;
    private Integer sold;
    private String imageCover;
    private List<String> images;
    private CategoryDTO category;
    private BrandDTO brand;
    private List<SubcategoryDTO> subcategory;
    private Double ratingsAverage;
    private Integer ratingsQuantity;
    private Instant  createdAt;
    private Instant  updatedAt;
}