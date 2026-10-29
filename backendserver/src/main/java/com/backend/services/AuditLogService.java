package com.backend.services;

import java.util.List;

import com.backend.dtos.AuditLogResponseDTO;

public interface AuditLogService {

    void log(String userEmail, String userRole, String action, String details);

    List<AuditLogResponseDTO> getAllLogs();

    List<AuditLogResponseDTO> getRecentLogs();

}
