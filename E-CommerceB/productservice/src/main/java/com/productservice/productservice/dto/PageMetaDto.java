package com.productservice.productservice.dto;

import lombok.Data;

@Data
public class PageMetaDto {
    private int currentPage;
    private int numberOfPages;
    private int limit;
    private Integer nextPage;
}