package com.ecommerce.userservice.services;

import com.ecommerce.userservice.dto.AddUserRequest;
import com.ecommerce.userservice.dto.UpdateRoleRequest;
import com.ecommerce.userservice.entities.User;
import com.ecommerce.userservice.repos.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found");
        }
        if(userRepository.findById(id).get().getRole() == User.Role.admin){
            throw new RuntimeException("Cannot delete admin user");
        }
        userRepository.deleteById(id);
    }

    @Transactional
    public Map<String, Object> addUser(AddUserRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            return Map.of(
                    "message", "fail",
                    "error", "Account Already Exists"
            );
        }

        User.Role role = request.role() == null ? User.Role.user : User.Role.valueOf(request.role());
        if (!isAdmin() && role == User.Role.admin) {
            return Map.of(
                    "message", "fail",
                    "error", "MANAGER cannot create ADMIN users",
                    "status", 403
            );
        }

        User user = User.builder()
                .name(request.name())
                .email(request.email())
                .phone(request.phone())
                .password(passwordEncoder.encode(request.password()))
                .role(role)
                .build();

        User saved = userRepository.save(user);

        return Map.of(
                "message", "success",
                "user", Map.of(
                        "id", saved.getId(),
                        "name", saved.getName(),
                        "email", saved.getEmail(),
                        "phone", saved.getPhone(),
                        "role", saved.getRole().name()
                )
        );
    }

    @Transactional
    public Map<String, Object> updateUserRole(Long id, UpdateRoleRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setRole(User.Role.valueOf(request.getRole()));
        userRepository.save(user);

        return Map.of(
                "message", "success",
                "userId", id,
                "newRole", request.getRole()
        );
    }

    private boolean isAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getAuthorities() == null) return false;
        return auth.getAuthorities().stream().anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority()));
    }
}

