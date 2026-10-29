package com.backend.services;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.backend.dtos.AuditLogResponseDTO;
import com.backend.entities.AuditLog;
import com.backend.repositories.AuditLogRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogRepository auditLogRepository;
    private final ModelMapper mapper;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void log(String userEmail, String userRole, String action, String details) {
        try {
            AuditLog auditLog = new AuditLog(userEmail, userRole, action, details);
            auditLogRepository.save(auditLog);
            log.info("[AUDIT LOG] User: {} ({}) | Action: {} | Details: {}", userEmail, userRole, action, details);
        } catch (Exception e) {
            log.error("Failed to save audit log: {}", e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLogResponseDTO> getAllLogs() {
        return auditLogRepository.findAllByOrderByTimestampDesc()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLogResponseDTO> getRecentLogs() {
        return auditLogRepository.findTop100ByOrderByTimestampDesc()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    private AuditLogResponseDTO mapToDTO(AuditLog log) {
        return mapper.map(log, AuditLogResponseDTO.class);
    }
}
