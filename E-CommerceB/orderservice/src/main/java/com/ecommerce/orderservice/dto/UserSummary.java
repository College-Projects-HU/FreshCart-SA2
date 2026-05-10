package com.ecommerce.orderservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserSummary {
    private String _id;
    private String name;
    private String email;
    private String phone;
}

