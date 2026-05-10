package com.productservice.productservice.services;

import com.productservice.productservice.dto.*;
import com.productservice.productservice.entities.Subcategory;
import com.productservice.productservice.exception.ResourceNotFoundException;
import com.productservice.productservice.repos.SubcategoryRepository;
import com.productservice.productservice.util.IdValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import org.bson.types.ObjectId;

@Service
@RequiredArgsConstructor
public class SubcategoryService {

    private final SubcategoryRepository subcategoryRepository;

    public PagedResponseDTO<SubcategoryDTO> getAllSubcategories(int page, int limit) {
        PageRequest pageable = PageRequest.of(page - 1, limit);
        Page<Subcategory> result = subcategoryRepository.findAll(pageable);

        List<SubcategoryDTO> data = result.getContent().stream()
                .map(this::toDTO).collect(Collectors.toList());

        int totalPages = result.getTotalPages();
        Integer nextPage = page < totalPages ? page + 1 : null;
        PagedResponseDTO.Metadata meta = new PagedResponseDTO.Metadata(page, totalPages, limit, nextPage);
        return new PagedResponseDTO<>((int) result.getTotalElements(), meta, data);
    }

    public SingleResponseDTO<SubcategoryDTO> getSubcategoryById(String id) {
        System.out.println("Validating subcategory id: " + id);
        IdValidator.validate(id, "id");
        Subcategory sub = subcategoryRepository.findById(new ObjectId(id))
                .orElseThrow(() -> new ResourceNotFoundException("Subcategory not found with id: " + id));
        System.out.println("Found subcategory: " + sub.getName());
        return new SingleResponseDTO<>(toDTO(sub));
    }

    private SubcategoryDTO toDTO(Subcategory s) {
        System.out.println("Converting subcategory to DTO: " + s.getName());
        if (s == null) return null;
        SubcategoryDTO dto = new SubcategoryDTO();
        dto.setId(s.getId().toHexString());
        dto.setName(s.getName());
        dto.setSlug(s.getSlug());
        dto.setCategory(s.getCategory());
        dto.setCreatedAt(s.getCreatedAt());
        dto.setUpdatedAt(s.getUpdatedAt());
        return dto;
    }
}
