package com.ecommerce.userservice.entities;

import com.ecommerce.userservice.util.EncryptedAttributeConverter;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.time.Instant;

@Entity
@Table(name = "users")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Convert(converter = EncryptedAttributeConverter.class)  // ← encrypted
    @Column(nullable = false, length = 512)                  // ← increased for cipher text
    private String name;

    @Column(nullable = false, unique = true, length = 150)
    private String email;                                    // ← NOT encrypted (used for lookup)

    @Column(nullable = false)
    private String password;                                 // ← BCrypt hashed (not AES)

    @Convert(converter = EncryptedAttributeConverter.class)  // ← encrypted
    @Column(nullable = false, length = 512)                  // ← increased for cipher text
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(name = "token_version", nullable = false)
    @Builder.Default
    private int tokenVersion = 0;

    @Column(unique = true)
    private String refreshToken;                             // ← your field kept

    private Instant refreshTokenExpiry;                      // ← your field kept

    // ── Password Reset ────────────────────────────────────────────────────────

    @Column(name = "password_reset_code")
    private String passwordResetCode;

    @Column(name = "password_reset_expires")
    private LocalDateTime passwordResetExpires;

    @Column(name = "password_reset_verified", nullable = false)
    @Builder.Default
    private boolean passwordResetVerified = false;

    // ── Timestamps ────────────────────────────────────────────────────────────

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (role == null) role = Role.user;  // ← your default kept
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum Role {
        guest,
        user,       // ← your enum values kept
        manager,
        admin
    }
}