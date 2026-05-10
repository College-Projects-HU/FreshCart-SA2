package com.productservice.productservice.dto;

import lombok.Data;
import java.util.List;



@Data
public class ProductListResponseDto {
    private int results;
    private PageMetaDto metadata;
    private List<ProductResponseDto> data;
}