package com.productservice.productservice.repos;

import com.productservice.productservice.entities.Subcategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.bson.types.ObjectId;

@Repository
public interface SubcategoryRepository extends MongoRepository<Subcategory, ObjectId> {
    Page<Subcategory> findByCategory(ObjectId categoryId, Pageable pageable);
}
