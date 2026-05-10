package com.ecommerce.cartservice.client;

import com.ecommerce.cartservice.dto.ProductDto;
import com.ecommerce.cartservice.dto.ProductResponseWrapper;
import com.ecommerce.cartservice.exception.ExternalServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.Optional;

/**
 * HTTP client for communicating with ProductService.
 *
 * Used to:
 *  1. Validate that a productId exists before adding it to the cart.
 *  2. Fetch product details (title, price, image, etc.) to enrich cart responses.
 */
@Component
@Slf4j
public class ProductServiceClient {

    private final WebClient webClient;

    public ProductServiceClient(@Qualifier("productServiceWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    /**
     * Fetches a single product by ID.
     *
     * @param productId the external product ID (MongoDB ObjectId string)
     * @param token     the JWT to forward so ProductService can authorize the call if needed
     * @return Optional.empty() if the product does not exist or the service is down
     */
    public Optional<ProductDto> getProductById(String productId, String token) {
        try {
            ProductDto product = webClient.get()
                    .uri("/api/v1/products/{id}", productId)
                    .header("token", token)
                    .retrieve()
                    // 404 → return null (not found is not an error for enrichment calls)
                    .onStatus(
                            status -> status.equals(HttpStatus.NOT_FOUND),
                            ClientResponse::createException   // consume body, then signal empty downstream
                    )
                    // any other 4xx / 5xx → throw ExternalServiceException
                    .onStatus(
                            HttpStatusCode::isError,
                            resp -> resp.bodyToMono(String.class)
                                    .map(body -> new ExternalServiceException(
                                            "ProductService error: " + body))
                                    .cast(Throwable.class)
                                    .flatMap(Mono::error)
                    )
                    .bodyToMono(ProductResponseWrapper.class)
                    .mapNotNull(ProductResponseWrapper::getData)
                    // if 404 handler signalled an error, swap it for an empty signal
                    .onErrorResume(
                            WebClientResponseException.NotFound.class,
                            ex -> Mono.empty()
                    )
                    .block();

            return Optional.ofNullable(product);

        } catch (ExternalServiceException e) {
            // re-throw so caller gets a proper 502
            throw e;
        } catch (Exception e) {
            // graceful degradation — cart still works if ProductService is down
            log.warn("ProductService unavailable, continuing without product details: {}", e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Validates that a productId exists.
     * Throws ExternalServiceException if the ID is invalid or not found.
     */
    public ProductDto validateAndGetProduct(String productId, String token) {
        return getProductById(productId, token)
                .orElseThrow(() -> new ExternalServiceException(
                        "Cast to ObjectId failed for value \"" + productId + "\" — product not found"));
    }
}