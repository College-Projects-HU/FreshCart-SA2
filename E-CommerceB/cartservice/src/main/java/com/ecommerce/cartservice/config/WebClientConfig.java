package com.ecommerce.cartservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;

@Configuration
public class WebClientConfig {

    @Value("${services.product-service.url}")
    private String productServiceUrl;

    @Value("${services.order-service.url}")
    private String orderServiceUrl;

    // ─── Explicitly declare the builder bean ──────────────────────────────────
    // Spring Boot 4.x no longer auto-registers WebClient.Builder in servlet apps.
    // We declare it manually so our two WebClient beans can inject it.
    @Bean
    @LoadBalanced 
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    @Bean("productServiceWebClient")
    public WebClient productServiceWebClient(WebClient.Builder builder) {
        return builder
                .baseUrl(productServiceUrl)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

    @Bean("orderServiceWebClient")
    public WebClient orderServiceWebClient(WebClient.Builder builder) {
        return builder
                .baseUrl(orderServiceUrl)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

}