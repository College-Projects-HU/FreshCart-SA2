package com.ecommerce.userservice.repos;

import com.ecommerce.userservice.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    Optional<User> findByPasswordResetCode(String passwordResetCode);
    Optional<User> findByRefreshToken(String refreshToken);
    boolean existsByRole(User.Role role); 
}