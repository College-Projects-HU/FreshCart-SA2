package com.ecommerce.orderservice.service;

import com.ecommerce.orderservice.dto.CreateOrderResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Parses the JSON product snapshot stored on an {@code OrderItem} back into
 * a typed {@link CreateOrderResponse.Product} object.
 *
 * Isolated here so neither the service nor the mapper carries JSON-parsing logic.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ProductSnapshotParser {

    private final ObjectMapper objectMapper;

    /**
     * Deserializes a product JSON snapshot into a {@link CreateOrderResponse.Product}.
     *
     * @param snapshot JSON string stored on the order item; may be {@code null}
     * @return parsed product, or {@code null} if the snapshot is absent or unparseable
     */
    @SuppressWarnings("unchecked")
    public CreateOrderResponse.Product parse(String snapshot) {
        if (snapshot == null) return null;
        try {
            Map<String, Object> map = objectMapper.readValue(snapshot, Map.class);

            return CreateOrderResponse.Product.builder()
                    .id(str(map.get("_id")))
                    .title(str(map.get("title")))
                    .imageCover(str(map.get("imageCover")))
                    .ratingsAverage(toDouble(map.get("ratingsAverage")))
                    .ratingsQuantity(toInt(map.get("ratingsQuantity")))
                    .subcategory(parseSubcategories(map.get("subcategory")))
                    .category(parseCategory(map.get("category")))
                    .brand(parseBrand(map.get("brand")))
                    .build();

        } catch (Exception e) {
            log.warn("Failed to parse product snapshot: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Extracts the {@code _id} field from a raw product-details object.
     * The cart service returns product details as a {@link Map}; this pulls the id out.
     *
     * @param productDetails raw product object from the cart DTO
     * @return the product id string, or {@code null} if absent
     */
    public String extractProductId(Object productDetails) {
        if (productDetails instanceof Map<?, ?> map) {
            Object id = map.get("_id");
            return id == null ? null : id.toString();
        }
        return null;
    }

    // ─── Private Helpers ──────────────────────────────────────────────────────

    @SuppressWarnings("unchecked")
    private List<CreateOrderResponse.Subcategory> parseSubcategories(Object raw) {
        if (!(raw instanceof List<?> list)) return List.of();
        return list.stream()
                .filter(s -> s instanceof Map)
                .map(s -> {
                    Map<String, Object> sm = (Map<String, Object>) s;
                    return CreateOrderResponse.Subcategory.builder()
                            .id(str(sm.get("_id")))
                            .name(str(sm.get("name")))
                            .slug(str(sm.get("slug")))
                            .category(str(sm.get("category")))
                            .build();
                })
                .collect(Collectors.toList());
    }

    private CreateOrderResponse.Category parseCategory(Object raw) {
        if (!(raw instanceof Map<?, ?> cm)) return null;
        return CreateOrderResponse.Category.builder()
                .id(str(cm.get("_id")))
                .name(str(cm.get("name")))
                .slug(str(cm.get("slug")))
                .image(str(cm.get("image")))
                .build();
    }

    private CreateOrderResponse.Brand parseBrand(Object raw) {
        if (!(raw instanceof Map<?, ?> bm)) return null;
        return CreateOrderResponse.Brand.builder()
                .id(str(bm.get("_id")))
                .name(str(bm.get("name")))
                .slug(str(bm.get("slug")))
                .image(str(bm.get("image")))
                .build();
    }

    private String str(Object o)    { return o == null ? null : o.toString(); }
    private double toDouble(Object o) { return o instanceof Number n ? n.doubleValue() : 0.0; }
    private int    toInt(Object o)    { return o instanceof Number n ? n.intValue()    : 0;   }
}