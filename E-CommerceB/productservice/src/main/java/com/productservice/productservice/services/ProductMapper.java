package com.productservice.productservice.services;

import com.productservice.productservice.dto.*;
import com.productservice.productservice.entities.Product;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductMapper {

    public ProductResponseDto toDto(Product p) {
    ProductResponseDto dto = new ProductResponseDto();
    dto.setId(p.getId());
    dto.setTitle(p.getTitle());
    dto.setSlug(p.getSlug());
    dto.setDescription(p.getDescription());
    dto.setQuantity(p.getQuantity());
    dto.setPrice(p.getPrice());
    dto.setSold(p.getSold());
    dto.setImageCover(p.getImageCover());
    dto.setImages(p.getImages());
    dto.setRatingsAverage(p.getRatingsAverage());
    dto.setRatingsQuantity(p.getRatingsQuantity());
    dto.setCreatedAt(p.getCreatedAt());
    dto.setUpdatedAt(p.getUpdatedAt());

    // ✅ Category — go through p.getCategory() first
    if (p.getCategory() != null) {
        CategoryDTO cat = new CategoryDTO();
        cat.setId(p.getCategory().getId());
        cat.setName(p.getCategory().getName());
        cat.setSlug(p.getCategory().getSlug());
        cat.setImage(p.getCategory().getImage());
        dto.setCategory(cat);
    }

    // ✅ Brand — go through p.getBrand() first
    if (p.getBrand() != null) {
        BrandDTO brand = new BrandDTO();
        brand.setId(p.getBrand().getId());
        brand.setName(p.getBrand().getName());
        brand.setSlug(p.getBrand().getSlug());
        brand.setImage(p.getBrand().getImage());
        dto.setBrand(brand);
    }

    // ✅ Subcategories — iterate over p.getSubcategory()
    if (p.getSubcategory() != null) {
        List<SubcategoryDTO> subs = p.getSubcategory().stream().map(s -> {
            SubcategoryDTO sd = new SubcategoryDTO();
            sd.setId(s.getId());
            sd.setName(s.getName());
            sd.setSlug(s.getSlug());
            sd.setCategory(s.getCategory()); // ✅ field name is 'category' in SubcategoryRef
            return sd;
        }).collect(Collectors.toList());
        dto.setSubcategory(subs);
    }

    return dto;
}
}
