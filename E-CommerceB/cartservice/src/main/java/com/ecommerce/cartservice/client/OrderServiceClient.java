package com.ecommerce.cartservice.client;

import com.ecommerce.cartservice.dto.CartDto;
import com.ecommerce.cartservice.exception.ExternalServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * HTTP client for communicating with OrderService.
 *
 * CartService notifies OrderService when:
 *  - A user checks out (OrderService pulls the cart data to create an order).
 *
 * CartService exposes cartData to OrderService — OrderService calls CartService,
 * not the other way around. This client is used for any push notifications
 * (e.g. cart cleared after order placed).
 */
@Component
@Slf4j
public class OrderServiceClient {

    private final WebClient webClient;

    public OrderServiceClient(@Qualifier("orderServiceWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    /**
     * Notifies OrderService that a cart has been checked out.
     * OrderService will then fetch the cart details from CartService to create the order.
     *
     * @param cartOwnerId the user whose cart was checked out
     * @param token       JWT forwarded for auth
     */
    public void notifyCartCheckedOut(String cartOwnerId, String token) {
        try {
            webClient.post()
                    .uri("/api/v1/orders/notify-cart-checkout")
                    .header("token", token)
                    .bodyValue(new CartCheckoutNotification(cartOwnerId))
                    .retrieve()
                    .toBodilessEntity()
                    .block();

            log.info("OrderService notified for cartOwner: {}", cartOwnerId);

        } catch (Exception e) {
            // Non-critical — log and continue, don't block the cart operation
            log.warn("Could not notify OrderService: {}", e.getMessage());
        }
    }

    /** Inner DTO just for the notification payload. */
    private record CartCheckoutNotification(String cartOwnerId) {}
}
