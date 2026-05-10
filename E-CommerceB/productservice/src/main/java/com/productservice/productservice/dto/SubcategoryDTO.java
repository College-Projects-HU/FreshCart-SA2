package com.productservice.productservice.dto;

import lombok.Data;
import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonProperty;

@Data
public class SubcategoryDTO {
    @JsonProperty("_id")
    private String id;
    private String name;
    private String slug;
    private String category;
    private Instant createdAt;
    private Instant updatedAt;
}
