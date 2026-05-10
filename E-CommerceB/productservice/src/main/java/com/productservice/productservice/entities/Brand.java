package com.productservice.productservice.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;

@Document(collection = "brands")
@Data @NoArgsConstructor @AllArgsConstructor
public class Brand {
    @Id 
    private String id;
    private String name;
    private String slug;
    private String image;
    private Instant createdAt;
    private Instant updatedAt;
}
