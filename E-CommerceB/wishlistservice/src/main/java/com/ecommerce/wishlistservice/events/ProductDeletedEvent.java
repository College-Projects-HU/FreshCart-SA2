package com.ecommerce.wishlistservice.events;

import java.time.Instant;

public record ProductDeletedEvent(String productId, String productName, Instant deletedAt) {}
