package com.ecommerce.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaginationMetadata {
    private int currentPage;
    private int numberOfPages;
    private int limit;
    private Integer nextPage;   // null on the last page
}
