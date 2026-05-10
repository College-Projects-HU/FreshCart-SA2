package com.ecommerce.userservice.services;

import com.ecommerce.userservice.entities.AuditLog;
import com.ecommerce.userservice.repos.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public void log(Long userId, String action, String ipAddress, String outcome) {
        auditLogRepository.save(
            AuditLog.builder()
                .userId(userId)
                .action(action)
                .ipAddress(ipAddress)
                .outcome(outcome)
                .build()
        );
    }
}