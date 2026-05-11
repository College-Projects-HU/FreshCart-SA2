package com.ecommerce.cartservice.events;

import java.time.Instant;

public record ProductDeletedEvent(String productId, String productName, Instant deletedAt) {}
