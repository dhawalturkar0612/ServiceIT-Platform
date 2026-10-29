package com.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.entities.ServiceCatalog;

public interface ServiceCatalogRepository extends JpaRepository<ServiceCatalog, Long> {

    // Prevent duplicate service names.
    boolean existsByServiceName(String serviceName);

}