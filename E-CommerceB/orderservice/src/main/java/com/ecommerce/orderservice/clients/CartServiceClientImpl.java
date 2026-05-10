package com.ecommerce.orderservice.clients;

import com.ecommerce.orderservice.dto.GetCartResponse;
import com.ecommerce.orderservice.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class CartServiceClientImpl implements CartServiceClient {

    private final RestClient restClient = RestClient.create();

    @Value("${services.cart-service.base-url:http://localhost:8082}")
    private String cartServiceBaseUrl;

    @Override
    public GetCartResponse getMyCart(String bearerToken) {
        return restClient.get()
                .uri(cartServiceBaseUrl + "/api/v1/cart")
                .header(HttpHeaders.AUTHORIZATION, bearerToken)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {
                    throw new UnauthorizedException("Invalid Token");
                })
                .body(GetCartResponse.class);
    }

    @Override
    public void clearMyCart(String bearerToken) {
        restClient.delete()
                .uri(cartServiceBaseUrl + "/api/v1/cart")
                .header(HttpHeaders.AUTHORIZATION, bearerToken)
                .retrieve()
                .toBodilessEntity();
    }
}

