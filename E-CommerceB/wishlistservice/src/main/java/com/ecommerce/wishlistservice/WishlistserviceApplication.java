package com.ecommerce.wishlistservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class WishlistserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(WishlistserviceApplication.class, args);
    }
}