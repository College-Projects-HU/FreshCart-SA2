package com.ecommerce.userservice.repos;

import com.ecommerce.userservice.entities.AuditLog;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    List<AuditLog> findByUserId(Long userId);
    List<AuditLog> findByUserIdAndAction(Long userId, String action);
}