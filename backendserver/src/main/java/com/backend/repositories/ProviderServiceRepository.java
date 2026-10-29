package com.backend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.backend.entities.ProviderService;

public interface ProviderServiceRepository extends JpaRepository<ProviderService, Long>, JpaSpecificationExecutor<ProviderService> {

    List<ProviderService> findByProviderId(Long providerId);

    List<ProviderService> findByServiceId(Long serviceId);

    boolean existsByProviderIdAndServiceId(Long providerId, Long serviceId);

}