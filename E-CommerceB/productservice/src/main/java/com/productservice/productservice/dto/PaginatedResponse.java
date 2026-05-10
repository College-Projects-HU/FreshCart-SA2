package com.productservice.productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaginatedResponse<T> {

    private int results;
    private Metadata metadata;
    private List<T> data;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Metadata {
        private int currentPage;
        private int numberOfPages;
        private int limit;
        private Integer nextPage;   // null on last page
        private Integer prevPage;   // null on first page
    }
}
