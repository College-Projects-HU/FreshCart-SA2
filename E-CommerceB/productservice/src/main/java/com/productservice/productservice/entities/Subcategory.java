package com.productservice.productservice.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;

@Document(collection = "subcategories")
@Data @NoArgsConstructor @AllArgsConstructor
public class Subcategory {
    @Id 
    private ObjectId id;
    private String name;
    private String slug;
    private String category;  // category id reference
    private Instant createdAt;
    private Instant updatedAt;
}
