package com.productservice.productservice.config;

import org.bson.types.ObjectId;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.core.convert.converter.Converter;

import java.util.List;

@Configuration
@EnableMongoAuditing
@EnableMongoRepositories(basePackages = "com.productservice.productservice.repos")
public class MongoConfig {
    @Bean
    public MongoCustomConversions customConversions() {
        return new MongoCustomConversions(List.of(
            new ObjectIdToStringConverter()
            // , new StringToObjectIdConverter()
        ));
    }

    // @WritingConverter
    // static class StringToObjectIdConverter implements Converter<String, ObjectId> {
    //     public ObjectId convert(String id) {
    //         if (id != null && ObjectId.isValid(id)) {
    //             return new ObjectId(id);
    //         }
    //         return null;
    //     }
    // }

    @ReadingConverter
    static class ObjectIdToStringConverter implements Converter<ObjectId, String> {
        public String convert(ObjectId id) {
            return id.toHexString();
        }
    }
}
