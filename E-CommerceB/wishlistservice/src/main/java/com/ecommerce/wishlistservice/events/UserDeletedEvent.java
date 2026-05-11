package com.ecommerce.wishlistservice.events;

import java.time.Instant;

public record UserDeletedEvent(Long userId, String email, Instant deletedAt) {}
