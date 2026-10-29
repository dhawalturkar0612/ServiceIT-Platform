package com.backend.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.dtos.AuditLogResponseDTO;
import com.backend.services.AuditLogService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/logs")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
@Tag(name = "Audit Log Controller", description = "Admin-only APIs for User Activity & System Audit Logs")
public class AuditLogController {

    private final AuditLogService auditLogService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    @Operation(summary = "Get all system audit logs (Admin only)")
    public ResponseEntity<List<AuditLogResponseDTO>> getAllLogs() {
        return ResponseEntity.ok(auditLogService.getAllLogs());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/recent")
    @Operation(summary = "Get recent 100 system audit logs (Admin only)")
    public ResponseEntity<List<AuditLogResponseDTO>> getRecentLogs() {
        return ResponseEntity.ok(auditLogService.getRecentLogs());
    }

}
