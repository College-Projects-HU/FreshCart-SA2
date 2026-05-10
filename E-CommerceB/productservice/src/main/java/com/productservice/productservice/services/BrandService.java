package com.productservice.productservice.services;

import com.productservice.productservice.dto.*;
import com.productservice.productservice.entities.Brand;
import com.productservice.productservice.exception.ResourceNotFoundException;
import com.productservice.productservice.repos.BrandRepository;
import com.productservice.productservice.util.IdValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BrandService {

    private final BrandRepository brandRepository;

    public PagedResponseDTO<BrandDTO> getAllBrands(int page, int limit) {
        PageRequest pageable = PageRequest.of(page - 1, limit);
        Page<Brand> result = brandRepository.findAll(pageable);

        List<BrandDTO> data = result.getContent().stream()
                .map(this::toDTO).collect(Collectors.toList());

        int totalPages = result.getTotalPages();
        Integer nextPage = page < totalPages ? page + 1 : null;
        PagedResponseDTO.Metadata meta = new PagedResponseDTO.Metadata(page, totalPages, limit, nextPage);
        return new PagedResponseDTO<>((int) result.getTotalElements(), meta, data);
    }

    public SingleResponseDTO<BrandDTO> getBrandById(String id) {
        IdValidator.validate(id, "id");
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found with id: " + id));
        return new SingleResponseDTO<>(toDTO(brand));
    }

    private BrandDTO toDTO(Brand b) {
        BrandDTO dto = new BrandDTO();
        dto.setId(b.getId());
        dto.setName(b.getName());
        dto.setSlug(b.getSlug());
        dto.setImage(b.getImage());
        dto.setCreatedAt(b.getCreatedAt());
        dto.setUpdatedAt(b.getUpdatedAt());
        return dto;
    }
}
