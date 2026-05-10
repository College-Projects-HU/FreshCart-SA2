package com.productservice.productservice.repos;

import com.productservice.productservice.entities.Category;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends MongoRepository<Category, ObjectId> {
}
