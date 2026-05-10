package com.productservice.productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagedResponseDTO<T> {
    private int results;
    private Metadata metadata;
    private List<T> data;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Metadata {
        private int currentPage;
        private int numberOfPages;
        private int limit;
        private Integer nextPage;
    }
}
