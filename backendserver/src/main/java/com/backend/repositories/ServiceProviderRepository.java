package com.backend.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.backend.entities.ServiceProvider;

public interface ServiceProviderRepository extends JpaRepository<ServiceProvider, Long> {

    /**
     * Find ServiceProvider by User ID.
     */
    Optional<ServiceProvider> findByUserId(Long userId);

    /**
     * Check whether ServiceProvider profile already exists.
     */
    boolean existsByUserId(Long userId);

}