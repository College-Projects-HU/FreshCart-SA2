package com.ecommerce.userservice.util;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Converter
@Component
public class EncryptedAttributeConverter
        implements AttributeConverter<String, String> {

    // ← Remove static, use a different injection approach
    @Autowired
    private AesEncryptionUtil encryptionUtil;

    @Override
    public String convertToDatabaseColumn(String attribute) {
        if (attribute == null) return null;
        return encryptionUtil.encrypt(attribute);
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        return encryptionUtil.decrypt(dbData);
    }
}