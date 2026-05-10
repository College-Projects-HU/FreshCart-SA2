package com.ecommerce.cartservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDto {

    @JsonProperty("_id")
    private String id;

    private String title;
    private String slug;
    private Integer quantity;
    private String imageCover;
    private BigDecimal price;

    private List<SubcategoryDto> subcategory;
    private CategoryDto category;
    private BrandDto brand;

    private String description;
    private List<String> images;
    private Double ratingsAverage;
    private Integer ratingsQuantity;
}