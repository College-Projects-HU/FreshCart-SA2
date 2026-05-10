package com.productservice.productservice.services;

import com.productservice.productservice.dto.AdminUpsertProductRequest;
import com.productservice.productservice.entities.Brand;
import com.productservice.productservice.entities.Category;
import com.productservice.productservice.entities.Product;
import com.productservice.productservice.entities.Subcategory;
import com.productservice.productservice.exception.ResourceNotFoundException;
import com.productservice.productservice.repos.BrandRepository;
import com.productservice.productservice.repos.CategoryRepository;
import com.productservice.productservice.repos.ProductRepository;
import com.productservice.productservice.repos.SubcategoryRepository;
import com.productservice.productservice.util.IdValidator;
import lombok.RequiredArgsConstructor;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductAdminService {

    private final ProductRepository   productRepository;
    private final CategoryRepository  categoryRepository;
    private final BrandRepository     brandRepository;
    private final SubcategoryRepository subcategoryRepository;

    public Product create(AdminUpsertProductRequest request) {
        Product product = new Product();
        // Set explicit ID if provided, otherwise MongoDB generates one
        if (request.id() != null) {
            product.setId(request.id());
        }
        return productRepository.save(buildProduct(product, request));
    }

    public Product update(String id, AdminUpsertProductRequest request) {
        IdValidator.validate(id, "id");
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        buildProduct(existing, request);
        existing.setUpdatedAt(Instant.now());
        return productRepository.save(existing);
    }

    public void delete(String id) {
        IdValidator.validate(id, "id");
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }
// ── private helpers ──────────────────────────────────────────────────────

    private Product buildProduct(Product product, AdminUpsertProductRequest req) {
        product.setTitle(req.title());
        product.setSlug(req.slug());
        product.setDescription(req.description());
        product.setQuantity(req.quantity());
        product.setPrice(req.price());
        product.setImageCover(req.imageCover());
        product.setImages(req.images());
        product.setSold(0);
        product.setRatingsAverage(0.0); 
        product.setRatingsQuantity(0);
        product.setCreatedAt(Instant.now());
        if (req.CategoryId() != null ) {
            Category cat = categoryRepository.findById(new ObjectId(req.CategoryId()))
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
            
            product.setCategory(new Product.CategoryRef(
                cat.getId().toHexString(), 
                cat.getName(), 
                cat.getSlug(), 
                cat.getImage()
            ));
        }

        if (req.BrandId() != null ) {
            Brand brand = brandRepository.findById(req.BrandId())
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found"));
            
            product.setBrand(new Product.BrandRef(
                brand.getId(), 
                brand.getName(), 
                brand.getSlug(), 
                brand.getImage()
            ));
        }

        // Hydrate Denormalized Subcategories
        if (req.SubcategoryIds() != null) {
            List<Product.SubcategoryRef> hydratedSubs = req.SubcategoryIds().stream()
                .map(subId -> {
                    Subcategory s = subcategoryRepository.findById(new ObjectId(subId))
                        .orElseThrow(() -> new ResourceNotFoundException("Subcategory not found: " + subId));
                    return new Product.SubcategoryRef(
                        s.getId().toHexString(),
                        s.getName(),
                        s.getSlug(), 
                        s.getCategory()
                    );
                })
                .collect(Collectors.toList());
            product.setSubcategory(hydratedSubs);
        }

        return product;
    }

}
