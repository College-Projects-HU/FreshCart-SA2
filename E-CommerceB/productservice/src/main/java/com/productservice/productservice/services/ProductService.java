package com.productservice.productservice.services;

import com.productservice.productservice.dto.*;
import com.productservice.productservice.entities.Product;
import com.productservice.productservice.exception.ResourceNotFoundException;
import com.productservice.productservice.repos.ProductRepository;
import com.productservice.productservice.util.IdValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public PagedResponseDTO<ProductDTO> getAllProducts(int page, int limit) {
        PageRequest pageable = PageRequest.of(page - 1, limit);
        Page<Product> result = productRepository.findAll(pageable);

        List<ProductDTO> data = result.getContent().stream()
                .map(this::toDTO).collect(Collectors.toList());

        int totalPages = result.getTotalPages();
        Integer nextPage = page < totalPages ? page + 1 : null;
        PagedResponseDTO.Metadata meta = new PagedResponseDTO.Metadata(page, totalPages, limit, nextPage);
        return new PagedResponseDTO<>((int) result.getTotalElements(), meta, data);
    }

    public SingleResponseDTO<ProductDTO> getProductById(String id) {
        IdValidator.validate(id, "id");
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        return new SingleResponseDTO<>(toDTO(product));
    }

    private ProductDTO toDTO(Product p) {
        ProductDTO dto = new ProductDTO();
        dto.set_id(p.getId());
        dto.setId(p.getId());
        dto.setTitle(p.getTitle());
        dto.setSlug(p.getSlug());
        dto.setDescription(p.getDescription());
        dto.setQuantity(p.getQuantity());
        dto.setPrice(p.getPrice());
        dto.setImageCover(p.getImageCover());
        dto.setImages(p.getImages());
        dto.setSold(p.getSold());
        dto.setRatingsAverage(p.getRatingsAverage());
        dto.setRatingsQuantity(p.getRatingsQuantity());
        dto.setCreatedAt(p.getCreatedAt());
        dto.setUpdatedAt(p.getUpdatedAt());

        if (p.getCategory() != null) {
            System.out.println("DEBUG category id = " + p.getCategory().getId());
            System.out.println("DEBUG category raw = " + p.getCategory());
            ProductDTO.CategoryRefDTO cat = new ProductDTO.CategoryRefDTO();
            cat.setId(p.getCategory().getId());
            cat.setName(p.getCategory().getName());
            cat.setSlug(p.getCategory().getSlug());
            cat.setImage(p.getCategory().getImage());
            dto.setCategory(cat);
        }

        if (p.getBrand() != null) {
            ProductDTO.BrandRefDTO brand = new ProductDTO.BrandRefDTO();
            brand.setId(p.getBrand().getId());
            brand.setName(p.getBrand().getName());
            brand.setSlug(p.getBrand().getSlug());
            brand.setImage(p.getBrand().getImage());
            dto.setBrand(brand);
        }

        if (p.getSubcategory() != null) {
            List<ProductDTO.SubcategoryRefDTO> subcats = p.getSubcategory().stream().map(s -> {
                ProductDTO.SubcategoryRefDTO sub = new ProductDTO.SubcategoryRefDTO();
                sub.setId(s.getId());
                sub.setName(s.getName());
                sub.setSlug(s.getSlug());
                sub.setCategory(s.getCategory());
                return sub;
            }).collect(Collectors.toList());
            dto.setSubcategory(subcats);
        }

        if (p.getReviews() != null) {
            List<ProductDTO.ReviewDTO> reviews = p.getReviews().stream().map(r -> {
                ProductDTO.ReviewDTO rev = new ProductDTO.ReviewDTO();
                rev.setId(r.getId());
                rev.setReview(r.getReview());
                rev.setRating(r.getRating());
                rev.setProduct(r.getProduct());
                rev.setCreatedAt(r.getCreatedAt());
                rev.setUpdatedAt(r.getUpdatedAt());
                if (r.getUser() != null) {
                    ProductDTO.UserRefDTO user = new ProductDTO.UserRefDTO();
                    user.setId(r.getUser().getId());
                    user.setName(r.getUser().getName());
                    rev.setUser(user);
                }
                return rev;
            }).collect(Collectors.toList());
            dto.setReviews(reviews);
        }

        return dto;
    }
}
