package com.ecommerce.userservice.events;

import java.time.Instant;

public record UserRegisteredEvent(Long userId, String name, String email, Instant registeredAt) {}
