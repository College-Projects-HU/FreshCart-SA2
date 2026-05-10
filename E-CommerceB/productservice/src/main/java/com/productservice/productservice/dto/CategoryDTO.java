package com.productservice.productservice.dto;

import lombok.Data;
import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonProperty;

@Data
public class CategoryDTO {
    @JsonProperty("_id")
    private String id;
    private String name;
    private String slug;
    private String image;
    private Instant createdAt;
    private Instant updatedAt;
}
