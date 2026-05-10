package com.ecommerce.orderservice.service;

import com.ecommerce.orderservice.dto.*;
import com.ecommerce.orderservice.model.ShippingAddress;
import com.ecommerce.orderservice.security.AuthenticatedUser;

import java.util.List;

public interface OrderService {

    /**
     * Create a cash order from the given cart.
     *
     * @param cartId          the cart to convert into an order
     * @param caller          the fully resolved authenticated user (from JWT)
     * @param shippingAddress validated shipping address from the request body
     * @param token           raw JWT – forwarded to the cart service
     */
    CreateOrderResponse createCashOrder(String cartId, AuthenticatedUser caller,
                                        ShippingAddress shippingAddress, String token);

    /**
     * List all orders with pagination (admin use).
     */
    ListOrdersResponse getAllOrders(int page, int limit);

    /**
     * Return all orders belonging to a specific user.
     */
    List<OrderData> getUserOrders(String targetUserId);

    /**
     * Create a Stripe checkout session and return the redirect URL.
     *
     * @param cartId          the cart to pay for
     * @param caller          the fully resolved authenticated user (from JWT)
     * @param shippingAddress validated shipping address from the request body
     * @param successUrl      the front-end URL Stripe redirects to after payment
     * @param token           raw JWT – forwarded to the cart service
     */
    CheckoutSessionResponse createCheckoutSession(String cartId, AuthenticatedUser caller,
                                                  ShippingAddress shippingAddress,
                                                  String successUrl, String token);

    /**
     * Handle incoming Stripe webhook events.
     *
     * @param payload   the raw JSON payload from Stripe
     * @param sigHeader the Stripe-Signature header for verifying the event
     */
    void handleStripeWebhook(String payload, String sigHeader);
}