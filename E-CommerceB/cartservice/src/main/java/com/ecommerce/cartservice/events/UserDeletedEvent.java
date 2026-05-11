package com.ecommerce.cartservice.events;

import java.time.Instant;

public record UserDeletedEvent(Long userId, String email, Instant deletedAt) {}
