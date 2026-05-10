package com.ecommerce.cartservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubcategoryDto {

    @JsonProperty("_id")
    private String id;

    private String name;
    private String slug;
    private String category;  // just the category id as a string
}