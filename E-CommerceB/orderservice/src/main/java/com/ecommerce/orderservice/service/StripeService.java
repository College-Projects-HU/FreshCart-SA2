package com.ecommerce.orderservice.service;

import com.ecommerce.orderservice.model.ShippingAddress;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.model.Event;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Slf4j
public class StripeService {

    @Value("${stripe.secret-key}")
    private String stripeSecretKey;

    @Value("${stripe.webhook-secret:#{null}}")
    private String webhookSecret;

    public String createSession(String cartId, String userId,
                                ShippingAddress shippingAddress,
                                BigDecimal total, String successUrl) {

        log.info("Creating Stripe session: cartId={}, userId={}, total={}", cartId, userId, total);

        try {
            Stripe.apiKey = stripeSecretKey;

            SessionCreateParams params = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl(successUrl + "?session_id={CHECKOUT_SESSION_ID}")
                    .setCancelUrl(successUrl + "/cancel")
                    .addLineItem(
                        SessionCreateParams.LineItem.builder()
                            .setQuantity(1L)
                            .setPriceData(
                                SessionCreateParams.LineItem.PriceData.builder()
                                    .setCurrency("egp")
                                    .setUnitAmount(total.multiply(BigDecimal.valueOf(100)).longValue())
                                    .setProductData(
                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                            .setName("Order #" + cartId)
                                            .build())
                                    .build())
                            .build())
                    .putMetadata("cartId", cartId)
                    .putMetadata("userId", userId)
                    .build();

            return Session.create(params).getUrl();

        } catch (Exception e) {
            log.error("Stripe session creation failed: {}", e.getMessage());
            throw new RuntimeException("Failed to create Stripe checkout session", e);
        }
    }

    /**
     * Verifies the webhook signature and returns the cartId from the event metadata
     * if the event is {@code checkout.session.completed}, otherwise returns {@code null}.
     */
    public String extractVerifiedCartId(String payload, String sigHeader) {
        try {
            Event event = Webhook.constructEvent(payload, sigHeader, webhookSecret);

            if ("checkout.session.completed".equals(event.getType())) {
                Session session = (Session) event.getDataObjectDeserializer()
                        .getObject()
                        .orElseThrow(() -> new RuntimeException("Could not deserialize Stripe event"));
                return session.getMetadata().get("cartId");
            }

            log.debug("Ignoring Stripe event type: {}", event.getType());
            return null;

        } catch (SignatureVerificationException e) {
            log.error("Stripe webhook signature verification failed: {}", e.getMessage());
            throw new RuntimeException("Invalid Stripe webhook signature", e);
        } catch (Exception e) {
            log.error("Stripe webhook processing failed: {}", e.getMessage());
            throw new RuntimeException("Webhook processing failed", e);
        }
    }
}