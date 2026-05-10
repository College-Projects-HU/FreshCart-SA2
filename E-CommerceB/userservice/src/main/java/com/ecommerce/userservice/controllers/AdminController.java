package com.ecommerce.userservice.controllers;

import com.ecommerce.userservice.dto.AddUserRequest;
import com.ecommerce.userservice.dto.UpdateRoleRequest;
import com.ecommerce.userservice.entities.User;

import com.ecommerce.userservice.services.AdminService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;


    @GetMapping("/getAllUsers")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @PostMapping("/addUser")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> addUser(@Valid @RequestBody AddUserRequest request) {
        Map<String, Object> result = adminService.addUser(request);
        if ("fail".equals(result.get("message")) && Integer.valueOf(403).equals(result.get("status"))) {
            return ResponseEntity.status(403).body(result);
        }
        if ("fail".equals(result.get("message"))) {
            return ResponseEntity.badRequest().body(result);
        }
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/deleteUser/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        adminService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
    

    @PutMapping("/updateUserRole/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> updateUserRole(
            @PathVariable Long id,
            @Valid @RequestBody UpdateRoleRequest request) {
        return ResponseEntity.ok(adminService.updateUserRole(id, request));
    }
}
