package com.productservice.productservice.services;

import com.productservice.productservice.dto.*;
import com.productservice.productservice.entities.Category;
import com.productservice.productservice.exception.ResourceNotFoundException;
import com.productservice.productservice.repos.CategoryRepository;
import com.productservice.productservice.repos.SubcategoryRepository;
import com.productservice.productservice.util.IdValidator;
import lombok.RequiredArgsConstructor;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final SubcategoryRepository subcategoryRepository;

    public PagedResponseDTO<CategoryDTO> getAllCategories(int page, int limit) {
        PageRequest pageable = PageRequest.of(page - 1, limit);
        Page<Category> result = categoryRepository.findAll(pageable);

        List<CategoryDTO> data = result.getContent().stream()
                .map(this::toDTO).collect(Collectors.toList());

        int totalPages = result.getTotalPages();
        Integer nextPage = page < totalPages ? page + 1 : null;

        PagedResponseDTO.Metadata meta = new PagedResponseDTO.Metadata(page, totalPages, limit, nextPage);
        return new PagedResponseDTO<>((int) result.getTotalElements(), meta, data);
    }

    public SingleResponseDTO<CategoryDTO> getCategoryById(String id) {
    IdValidator.validate(id, "id");                          // ✅ String → validates format
    Category category = categoryRepository.findById(new ObjectId(id))  // ✅ String → ObjectId for repo
            .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
    return new SingleResponseDTO<>(toDTO(category));
}

    public PagedResponseDTO<SubcategoryDTO> getSubcategoriesByCategoryId(String categoryId, int page, int limit) {
        IdValidator.validate(categoryId, "id");
        // Ensure the category exists
        categoryRepository.findById(new ObjectId(categoryId))
                            .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));

        PageRequest pageable = PageRequest.of(page - 1, limit);
        var result = subcategoryRepository.findByCategory(new ObjectId(categoryId), pageable);

        List<SubcategoryDTO> data = result.getContent().stream()
                .map(s -> {
                    SubcategoryDTO dto = new SubcategoryDTO();
                    dto.setId(s.getId().toHexString());
                    dto.setName(s.getName());
                    dto.setSlug(s.getSlug());
                    dto.setCategory(s.getCategory());
                    dto.setCreatedAt(s.getCreatedAt());
                    dto.setUpdatedAt(s.getUpdatedAt());
                    return dto;
                }).collect(Collectors.toList());

        int totalPages = result.getTotalPages();
        Integer nextPage = page < totalPages ? page + 1 : null;
        PagedResponseDTO.Metadata meta = new PagedResponseDTO.Metadata(page, totalPages, limit, nextPage);
        return new PagedResponseDTO<>((int) result.getTotalElements(), meta, data);
    }

    private CategoryDTO toDTO(Category c) {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(c.getId().toHexString()); // ✅ ObjectId → String
        dto.setName(c.getName());
        dto.setSlug(c.getSlug());
        dto.setImage(c.getImage());
        dto.setCreatedAt(c.getCreatedAt());
        dto.setUpdatedAt(c.getUpdatedAt());
        return dto;
    }
}
