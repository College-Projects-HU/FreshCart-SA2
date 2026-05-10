package com.productservice.productservice.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.List;

@Document(collection = "products")
@Data @NoArgsConstructor @AllArgsConstructor
public class Product {
    @Id
    @Field("_id")
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
    private CategoryRef category;
    private BrandRef brand;
    private List<SubcategoryRef> subcategory;
    private List<Review> reviews;
    private Instant createdAt;
    private Instant updatedAt;

    @Data @NoArgsConstructor @AllArgsConstructor
    public static class CategoryRef {
        @Field("_id")
        private String id;
        private String name;
        private String slug;
        private String image;
    }

    @Data @NoArgsConstructor @AllArgsConstructor
    public static class BrandRef {
        @Field("_id")
        private String id;
        private String name;
        private String slug;
        private String image;
    }

    @Data @NoArgsConstructor @AllArgsConstructor
    public static class SubcategoryRef {
        @Field("_id")
        private String id;
        private String name;
        private String slug;
        private String category;
    }

    @Data @NoArgsConstructor @AllArgsConstructor
    public static class Review {
        @Field("_id")
        private String id;
        private String review;
        private Integer rating;
        private String product;
        private UserRef user;
        private Instant createdAt;
        private Instant updatedAt;
    }

    @Data @NoArgsConstructor @AllArgsConstructor
    public static class UserRef {
        @Field("_id")
        private String id;
        private String name;
    }
}
