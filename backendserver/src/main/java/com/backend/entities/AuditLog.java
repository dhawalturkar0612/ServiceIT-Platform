package com.backend.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "audit_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private Long id;

    @Column(name = "user_email", length = 100)
    private String userEmail;

    @Column(name = "user_role", length = 30)
    private String userRole;

    @Column(name = "action", nullable = false, length = 50)
    private String action;

    @Column(name = "details", nullable = false, length = 1000)
    private String details;

    @Column(name = "ip_address", length = 50)
    private String ipAddress;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    public AuditLog(String userEmail, String userRole, String action, String details) {
        this.userEmail = userEmail;
        this.userRole = userRole;
        this.action = action;
        this.details = details;
        this.timestamp = LocalDateTime.now();
    }
}
