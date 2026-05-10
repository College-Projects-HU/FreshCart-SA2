package com.ecommerce.userservice.config;

import com.ecommerce.userservice.entities.User;
import com.ecommerce.userservice.repos.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@DependsOn("aesEncryptionUtil")   // ← ensures AES bean is ready before seeder runs
public class AdminSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(AdminSeeder.class);

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        boolean adminExists = userRepository.existsByRole(User.Role.admin);

        if (!adminExists) {
            User admin = User.builder()
                    .name("Hannah Admin")
                    .email("admin@ecommerce.com")
                    .password(passwordEncoder.encode("Admin@1234"))
                    .phone("01010000000")
                    .role(User.Role.admin)
                    .build();

            userRepository.save(admin);

            log.info("==============================================");
            log.info("Default admin account created:");
            log.info("Email   : admin@ecommerce.com");
            log.info("Password: Admin@1234");
            log.info("==============================================");
        } else {
            log.info("Admin account already exists — skipping seeder");
        }
    }
}