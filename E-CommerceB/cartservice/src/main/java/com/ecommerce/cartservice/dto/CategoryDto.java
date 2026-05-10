package com.ecommerce.cartservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDto {

    @JsonProperty("_id")
    private String id;

    private String name;
    private String slug;
    private String image;
}