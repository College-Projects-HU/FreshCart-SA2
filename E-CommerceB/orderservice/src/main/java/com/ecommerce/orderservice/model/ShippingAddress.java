package com.ecommerce.orderservice.model;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShippingAddress {

    @NotBlank(message = "Address details are required")
    private String details;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^01[0-9]{9}$", message = "Phone must be a valid Egyptian number")
    private String phone;

    @NotBlank(message = "City is required")
    private String city;

    private String postalCode;
}
