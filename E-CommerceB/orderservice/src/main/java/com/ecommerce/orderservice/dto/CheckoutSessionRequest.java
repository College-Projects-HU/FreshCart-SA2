package com.ecommerce.orderservice.dto;

import com.ecommerce.orderservice.model.ShippingAddress;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CheckoutSessionRequest {

    @Valid
    @NotNull(message = "Shipping address is required")
    @JsonProperty("shippingAddress")
    private ShippingAddress shippingAddress;
}
