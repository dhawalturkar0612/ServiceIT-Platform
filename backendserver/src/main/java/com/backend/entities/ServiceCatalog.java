package com.backend.entities;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "services_catalog")
@AttributeOverride(name = "id",column = @Column(name = "service_id")
)
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
public class ServiceCatalog extends BaseEntity {

    @NotBlank(message = "Service name is required")
    @Size(max = 100, message = "Service name cannot exceed 100 characters")
    @Column(name = "service_name", nullable = false, unique = true, length = 100)
    private String serviceName;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    @Column(length = 500)
    private String description;

    public ServiceCatalog(String serviceName, String description) {
        this.serviceName = serviceName;
        this.description = description;
    }
}