package com.productservice.productservice.repos;

import com.productservice.productservice.entities.Brand;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandRepository extends MongoRepository<Brand, String> {
}
