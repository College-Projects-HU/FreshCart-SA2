package com.ecommerce.userservice.events;

import java.time.Instant;

public record UserDeletedEvent(Long userId, String email, Instant deletedAt) {}
