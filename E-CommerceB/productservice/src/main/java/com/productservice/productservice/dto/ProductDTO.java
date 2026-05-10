package com.productservice.productservice.dto;

import lombok.Data;
import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

@Data
public class ProductDTO {
    @JsonProperty("_id")
    private String _id;
    private String id;
    private String title;
    private String slug;
    private String description;
    private Integer quantity;
    private Double price;
    private String imageCover;
    private List<String> images;
    private Integer sold;
    private Double ratingsAverage;
    private Integer ratingsQuantity;
    private CategoryRefDTO category;
    private BrandRefDTO brand;
    private List<SubcategoryRefDTO> subcategory;
    private List<ReviewDTO> reviews;
    private Instant createdAt;
    private Instant updatedAt;

    @Data
    public static class CategoryRefDTO {
        @JsonProperty("_id")
        private String id;
        private String name;
        private String slug;
        private String image;
    }

    @Data
    public static class BrandRefDTO {
        @JsonProperty("_id")
        private String id;
        private String name;
        private String slug;
        private String image;
    }

    @Data
    public static class SubcategoryRefDTO {
        @JsonProperty("_id")
        private String id;
        private String name;
        private String slug;
        private String category;
    }

    @Data
    public static class ReviewDTO {
        @JsonProperty(value = "_id", access = JsonProperty.Access.READ_ONLY)
        private String id;
        private String review;
        private Integer rating;
        private String product;
        private UserRefDTO user;
        private Instant createdAt;
        private Instant updatedAt;
    }

    @Data
    public static class UserRefDTO {
        @JsonProperty(value = "_id", access = JsonProperty.Access.READ_ONLY)
        private String id;
        private String name;
    }
}
