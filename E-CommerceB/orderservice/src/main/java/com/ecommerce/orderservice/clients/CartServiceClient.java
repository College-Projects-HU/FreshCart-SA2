package com.ecommerce.orderservice.clients;

import com.ecommerce.orderservice.dto.GetCartResponse;

/**
 * Abstraction over the Cart micro-service.
 * Implement with Feign, RestTemplate, or WebClient depending on your stack.
 *
 * A Feign example is shown below; swap in your actual implementation.
 */
public interface CartServiceClient {

    /**
     * Fetch logged-in user's cart from cartservice.
     */
    GetCartResponse getMyCart(String bearerToken);

    /**
     * Clears (empties) the user's active cart after order creation.
     */
    void clearMyCart(String bearerToken);
}
